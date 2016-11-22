package com.pineone.icbms.sda.sch.service;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.service.SchedulerJobComm;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.mapper.rdbms.AlarmMapper;
import com.pineone.icbms.sda.kb.mapper.service.AttendanceServiceMapper;
import com.pineone.icbms.sda.sch.dao.AggrDAO;
import com.pineone.icbms.sda.sch.dto.AggrDTO;
import com.pineone.icbms.sda.sch.dto.SchDTO;
import com.pineone.icbms.sda.sf.service.SparqlService;
import com.pineone.icbms.sda.sf.service.TripleService;

@Service
public class MakeAttendanceJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());

	public void runner(JobExecutionContext jec, String save_path) throws Exception {		
		String start_time = Utils.dateFormat.format(new Date());;
		AggrDAO aggrDAO;
		StringBuffer msg = new StringBuffer();
		
		log.info(
				"MakeAttendanceJobService(id : " + jec.getJobDetail().getName() + ") start.......................");

		String riot_mode = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");

		StringBuffer sb = new StringBuffer();
		String startDate = "";
		String endDate = "";
		String triple_path_file = "";
		String triple_check_result = "";
		TripleService tripleService = new TripleService();

		startDate = start_time;
		endDate = start_time;

		log.debug("startDate : " + startDate);
		log.debug("endDate : " + endDate);

		try {
			// AggrDTO정보
			List<AggrDTO> aggrList = new ArrayList<AggrDTO>();
			aggrDAO = getContext().getBean(AggrDAO.class);
			Map<String, String> commandMap = new HashMap<String, String>();
			commandMap.put("aggr_id", jec.getJobDetail().getName());
			
			// 집계정보 가져오기
			aggrList = (List<AggrDTO>)aggrDAO.selectList(commandMap);
			
			//test
			for(AggrDTO aggrDTO : aggrList) {
				log.debug("aggrDTO =====>" + aggrDTO.toString());
			}

			// sch_hist테이블에 data insert(work_cnt는 aggrList목록의 개수로 설정함)
			insertSchHist(jec, aggrList.size(), start_time, start_time);
			
			SparqlService sparqlService = new SparqlService();
			List<Map<String, String>> argsResultList;		// 대상목록
			// argsql로 대상을 구함
			argsResultList = sparqlService.runSparql(aggrList.get(0).getArgsql());
			
			//test
			for(Map<String, String> map : argsResultList) {
				log.debug("map of argsResultList==============>"+map.toString());
			}
			
			Date now = new Date();
		
			// 위해서 구한 대상을 이용하여 수행한다.
			for(int m = 0; m < argsResultList.size(); m++) {
				String lectureLoc = argsResultList.get(m).get("lectureLoc");
			    String curLoc = argsResultList.get(m).get("curLoc");
			    String student = argsResultList.get(m).get("student");
			    String lecture = argsResultList.get(m).get("lecture");
				String attendance = "";
				
			    if(lectureLoc.equals(curLoc)) { // 현재위치가 강의실인 경우
			    	attendance = "attend";
			    } else {
			    	attendance = "late";
			    }
			    
			    // 모델 만들기
			    Model model = ModelFactory.createDefaultModel();
			    
			    //AttendanceServiceMapper map = new AttendanceServiceMapper("cm001","2015-12-12T00:00:00","absent","u00001");
		    	AttendanceServiceMapper map = new AttendanceServiceMapper(lecture,Utils.dateFormat2.format(now), attendance, student);
			    List<Statement> result = map.from();
			    Iterator<Statement> it = result.iterator();
			    
			    // 모델에 statement값 넣음
			    while (it.hasNext()) {
				    model.add(it.next());
			    }
			    StringWriter sw = new StringWriter();
			    
			    // 모델.write할때 포멧을 지정함
			    //model.write(sw, "TURTLE");
			    model.write(sw, "N-TRIPLE");
			    
			    log.debug("sw.toString() ===============================> " + sw.toString());
			    sb.append(sw.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		// endDate값을 sch테이블의 last_work_time에 update
		updateLastWorkTime(jec, endDate);

		// 스트링 버퍼에 있는 값을 파일에 기록한다.
		String file_name = jec.getJobDetail().getName() + "_WRK" + endDate + "_TT" + start_time;
		String triple_check_result_file = "";

		// 결과값이 있을때만 triple파일을 만듬
		if (sb.length() > 0) {
			triple_path_file = save_path + "/" + file_name + ".ttl";
			sb.insert(0, Utils.getHeaderForTripleFile());
			tripleService.makeTripleFile(triple_path_file, sb);

			// triple파일 체크
			if (!riot_mode.equals("--skip")) {
				String[] check_result = tripleService.checkTripleFile(triple_path_file, file_name);

				// 점검결과를 파일로 저장한다.(체크결과 오류가 있는 경우만 파일로 만듬)
				if (!check_result[1].trim().equals("")) {
					triple_check_result_file = file_name + ".bad";
					tripleService.makeResultFile(triple_check_result_file, check_result);

					// triple파일 체크결과값
					if (check_result[1].length() > 0) {
						triple_check_result = check_result[1];
					} else {
						triple_check_result = Utils.Valid;
					}
				}
			}
			// 파일 전송
			tripleService.sendTripleFile(triple_path_file);
			log.debug("MakeAttendanceJobService(id : " + jec.getJobDetail().getName()
					+ ") end.......................");
		}

		// sch_hist테이블의 finish_time에 날짜 설정
		String finish_time = Utils.dateFormat.format(new Date());
		if (triple_check_result_file.equals(""))
			triple_check_result_file = Utils.None;
		updateFinishTime(jec, start_time, finish_time, "triple_check_result_file : " + triple_check_result_file,
				triple_path_file, triple_check_result);
	}

	@Override
	public void execute(JobExecutionContext arg0) {
		String save_path = Utils.getSdaProperty("com.pineone.icbms.sda.triple.save_path");
		
		// 폴더가 없으면 생성
		save_path = Utils.makeSavePath(save_path);

		try {
			runner(arg0, save_path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
