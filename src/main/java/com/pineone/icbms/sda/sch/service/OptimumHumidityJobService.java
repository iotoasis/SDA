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
public class OptimumHumidityJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	
	// triple로 부터 집계를해서 domain에 값을 넣음을 스케줄링함
	public void runner(JobExecutionContext jec) throws Exception {
		String start_time = "";
		String finish_time = "";
		AggrDAO aggrDAO;
		StringBuffer msg = new StringBuffer();

		log.debug("OptimumHumidityJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
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
			
			String maxValue = "0";
			String minValue = "0";
			
			// 위해서 구한 대상에 값을 설정한다.
			for(int m = 0; m < argsResultList.size(); m++) {
				// 결과값은 한개만..
				aggrResultList = sparqlService.runSparql(aggrList.get(0).getAggrql(), new String[]{argsResultList.get(m).get("uri")});

				//test
				for(Map<String, String> map : aggrResultList) {
					log.debug("map of aggrResultList =====>" + map.toString());
				}
				
				if(aggrResultList.size() > 1) {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "aggrValue have too many values or not ! ");
				}
				
				// update수행(한번에 한개의 값에 대해서만....)
				String aggrValue = aggrResultList.get(0).get("aggrValue");
				if(aggrValue == null || aggrValue.equals("") || aggrValue.equals("null")) {
					aggrValue = "0";
				}
				float aggValue = Float.parseFloat(aggrValue);
				log.debug("aggValue =======>"+aggValue);
				msg.append("aggrValue ==> ");
				msg.append(aggrValue);
				msg.append(Utils.NEW_LINE);
				
				// 적정온도 파악
				if(aggValue <= 17.0F) {
					minValue = "60";
					maxValue = "70";
				} else if(aggValue >= 18.0F && aggValue <= 20.0F) {
					minValue = "50";
					maxValue = "60";
				} else if(aggValue >= 21.0F && aggValue <= 23.0F) {
					minValue = "40";
					maxValue = "50";
				} else if(aggValue >= 24.0F) {
					minValue = "39";
					maxValue = "40";
				}

				if(jec.getJobDetail().getName().equals("AG-2-2-001")) {
					sparqlService.updateSparql(aggrList.get(0).getUpdateql(), new String[]{argsResultList.get(m).get("prefer_value"), minValue});
					msg.append("prefer_uri["+m+"] ==>  ");
					msg.append(argsResultList.get(m).get("prefer_value"));
					msg.append(Utils.NEW_LINE);
					msg.append("min(");
					msg.append(minValue);
					msg.append(") value updated.");

					log.debug("min("+minValue+") value updated...");
				} else if(jec.getJobDetail().getName().equals("AG-2-2-002")) {
					sparqlService.updateSparql(aggrList.get(0).getUpdateql(), new String[]{argsResultList.get(m).get("prefer_value"), maxValue});
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
			updateFinishTime(jec, start_time, finish_time, msg.toString(), Utils.NoTripleFile, Utils.NotCheck);

			// finish_time값을 sch테이블의 last_work_time에 update
			updateLastWorkTime(jec, finish_time);
			log.debug("OptimumHumidityJobService(id : "+jec.getJobDetail().getName()+") end.......................");			
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
