package com.pineone.icbms.sda.sch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.service.SchedulerJobComm;
import com.pineone.icbms.sda.sf.service.SparqlService;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.dao.AggrDAO;
import com.pineone.icbms.sda.sch.dto.AggrDTO;

@Service
public class OptimumTemperatureJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	
	// triple로 부터 집계를해서 domain에 값을 넣음을 스케줄링함
	public void runner(JobExecutionContext jec) throws Exception {
		String start_time = "";
		String finish_time = "";
		AggrDAO aggrDAO;
		StringBuffer msg = new StringBuffer();

		log.debug("OptimumTemperatureJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
		try {
			start_time = Utils.dateFormat.format(new Date());

			// AggrDTO정보
			List<AggrDTO> aggrList = new ArrayList<AggrDTO>();
			aggrDAO = getContext().getBean(AggrDAO.class);
			Map<String, String> commandMap = new HashMap<String, String>();
			//commandMap.put("task_group_id", jec.getJobDetail().getGroup());
			commandMap.put("aggr_id", jec.getJobDetail().getName());
			
			// 집계정보 가져오기
			aggrList = (List<AggrDTO>)aggrDAO.selectList(commandMap);
			
			
			//test
			for(AggrDTO aggrDTO : aggrList) {
				log.debug("aggrDTO =====>" + aggrDTO.toString());
			}

			// sch_hist테이블에 data insert(work_cnt는 aggrList목록의 개수로 설정함)
			insertSchHist(jec, aggrList.size(), start_time, start_time);
			
			// aggr테이블의 aggr_id에 설정된 개수만큼 아래를 수행한다.(1개만 있다..)
			SparqlService sparqlService = new SparqlService();
			List<Map<String, String>> argsResultList;		// 대상목록
			List<Map<String, String>> aggrResultList;
			// argsql로 대상을 구함
			argsResultList = sparqlService.runSparql(aggrList.get(0).getArgsql());
			
			//test
			for(Map<String, String> map : argsResultList) {
				log.debug("map of argsResultList==============>"+map.toString());
			}
			
			int today = Integer.parseInt(Utils.MMddFormat.format(new Date()));
			log.debug("today ======> "+today);
			msg.append("today ==> "+today);
			msg.append("------------------------------");
			msg.append(Utils.NEW_LINE);
			
			String maxValue = "0";
			String minValue = "0";

			// 사계절 판단
			if(today >= 302 && today <= 624) {  // spring
				maxValue = "25";
				minValue = "21";
			} else if(today >= 625 && today <= 907) { // summer
				maxValue = "28";
				minValue = "26";
			} else if(today >= 908 && today <= 1126) { // fall
				maxValue = "25";
				minValue = "21";
			}else if(today >= 1127 && today <= 1231) { // winter
				maxValue = "25";
				minValue = "18";
			}else if(today >= 101 && today <= 301) { // winter
				maxValue = "20";
				minValue = "18";
			}
			
			for(int m = 0; m < argsResultList.size(); m++) {
				if(jec.getJobDetail().getName().equals("AG-2-1-001")) {
					sparqlService.updateSparql(aggrList.get(0).getDeleteql(), aggrList.get(0).getInsertql(), new String[]{argsResultList.get(m).get("prefer_value"), minValue});
					msg.append("prefer_uri["+m+"] ==>  ");
					msg.append(argsResultList.get(m).get("prefer_value"));
					msg.append(Utils.NEW_LINE);
					msg.append("min(");
					msg.append(minValue);
					msg.append(") value updated.");
					
					log.debug("min("+minValue+") value updated...");
				} else if(jec.getJobDetail().getName().equals("AG-2-1-002")) {
					sparqlService.updateSparql(aggrList.get(0).getDeleteql(), aggrList.get(0).getInsertql(), new String[]{argsResultList.get(m).get("prefer_value"), maxValue});
					msg.append("prefer_uri["+m+"] ==>  ");
					msg.append(argsResultList.get(m).get("prefer_value"));
					msg.append(Utils.NEW_LINE);
					msg.append("max(");
					msg.append(maxValue);
					msg.append(") value updated.");

					log.debug("max("+maxValue+") value updated...");
				} else {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST);
				}
				msg.append(Utils.NEW_LINE);				
				msg.append("------------------------------");
				msg.append(Utils.NEW_LINE);
			}

			finish_time = Utils.dateFormat.format(new Date());
			updateFinishTime(jec, start_time, finish_time, msg.toString() , Utils.NoTripleFile, Utils.NotCheck);

			// finish_time값을 sch테이블의 last_work_time에 update
			updateLastWorkTime(jec, finish_time);
			log.debug("OptimumTemperatureJobService(id : "+jec.getJobDetail().getName()+") end.......................");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) {
		try {
			runner(arg0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
