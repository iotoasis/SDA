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
public class PreferTemperatureBySurveyJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	
	// triple로 부터 집계를해서 domain에 값을 넣음을 스케줄링함
	public void runner(JobExecutionContext jec) throws Exception {
		String start_time = "";
		String finish_time = "";
		AggrDAO aggrDAO;
		StringBuffer msg = new StringBuffer();

		log.info("PreferTemperatureBySurveyJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
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
			
			// 대상목록
			List<Map<String, String>> argsResultList = sparqlService.runSparql(aggrList.get(0).getArgsql());
			
			//test
			for(Map<String, String> map : argsResultList) {
				log.debug("map of argsResultList==============>"+map.toString());
			}
			
			// 위해서 구한 대상을 이용하여 aggrql을 수행시켜준다.
			List<Map<String, String>> aggrResultList;		// 결과목록
			for(int m = 0; m < argsResultList.size(); m++) {
				// 결과값은 한개 세트임(여러 row에 존재할 수 있음) 
				aggrResultList = sparqlService.runSparql(aggrList.get(0).getAggrql(), new String[]{argsResultList.get(m).get("loc")});
				
				//test
				for(Map<String, String> map : aggrResultList) {
					log.debug("map of aggrResultList =====>" + map.toString());
				}
				
				String loc = aggrResultList.get(0).get("loc");
				String pre = aggrResultList.get(0).get("pre");
				Float min_value = Float.parseFloat(aggrResultList.get(0).get("min_value"));
				Float max_value = Float.parseFloat(aggrResultList.get(0).get("max_value"));
				
				String hotCondition = "http://www.pineone.com/campus/hotCondition";
				String coldCondition = "http://www.pineone.com/campus/coldCondition";
				String goodCondition = "http://www.pineone.com/campus/goodCondition";
				
				int hotCondition_cnt = 0;
				int coldCondition_cnt = 0;
				int goodCondition_cnt = 0;
				boolean skip_flag = false;
				
				log.debug("loc==>"+loc);
				log.debug("pre==>"+pre);
				log.debug("min_value==>"+min_value);
				log.debug("max_value==>"+max_value);

				int cnt = 0;
				for(int n = 0; n < aggrResultList.size(); n++) {
					String cond = aggrResultList.get(n).get("cond");
					String tmp_cnt = aggrResultList.get(n).get("cnt");
					
					try {
						cnt = Integer.parseInt(tmp_cnt);
					} catch (Exception e) {
						cnt = 0;
					}
					
					if(cond.equals(hotCondition)) {
						hotCondition_cnt = cnt;
					} else if(cond.equals(coldCondition)) {
						coldCondition_cnt = cnt;
					} else if(cond.equals(goodCondition)) {
						goodCondition_cnt = cnt;
					}
				}
				
				log.debug("hotCondition_cnt==>"+hotCondition_cnt);
				log.debug("coldCondition_cnt==>"+coldCondition_cnt);
				log.debug("goodCondition_cnt==>"+goodCondition_cnt);
					
				//동일한 값이거나 good이 hot이나 cold보다 크면 : 선호온도를 변경하지 않음
				if(  (hotCondition_cnt == coldCondition_cnt &&  hotCondition_cnt == goodCondition_cnt)
						|| (goodCondition_cnt > hotCondition_cnt && goodCondition_cnt > coldCondition_cnt)) {
					skip_flag = true;
					log.debug("action : 동일한 값이거나 good이 hot이나 cold보다 큰 경우 : 선호온도를 변경하지 않음");
				}
				
				// hot이 cold보다 크면 -1
				if(hotCondition_cnt > coldCondition_cnt) {
					min_value = min_value - 1.0F;
					max_value = max_value - 1.0F;
					log.debug("action : hot이 cold보다 큰 경우 : min, max모두 -1");
				}
					
				// cold보다 hot이 크면 +1
				if(hotCondition_cnt < coldCondition_cnt) {
					min_value = min_value + 1.0F;
					max_value = max_value + 1.0F;
					log.debug("action : cold보다 hot이 큰 경우 : min, max모두  +1");
				}
				
				// update수행(한번에 한개의 값에 대해서만....)
				if(skip_flag == true) {
					msg.append(" 3 conditions have same count or zero value !");
					msg.append(Utils.NEW_LINE);					
				} else {
					sparqlService.deleteSparql(aggrList.get(0).getUpdateql(), new String[]{pre, String.valueOf(min_value), String.valueOf(max_value)});
				}

				msg.append("loc["+m+"] ==> ");
				msg.append(loc);
				msg.append(Utils.NEW_LINE);
				
				msg.append("pre["+m+"] ==> ");
				msg.append(pre);
				msg.append(Utils.NEW_LINE);	
				

				msg.append("min_value["+m+"] ==> ");
				msg.append(min_value);
				msg.append(Utils.NEW_LINE);	

				msg.append("max_value["+m+"] ==> ");
				msg.append(max_value);
				msg.append(Utils.NEW_LINE);	

				msg.append("------------------------------");
				msg.append(Utils.NEW_LINE);
			} // end of for 
			
			if(argsResultList.size() == 0) {
				msg.append(Utils.NoArg);
			}

			finish_time = Utils.dateFormat.format(new Date());
			updateFinishTime(jec, start_time, finish_time, msg.toString() , Utils.NoTripleFile, Utils.NotCheck);

			// finish_time값을 sch테이블의 last_work_time에 update
			updateLastWorkTime(jec, finish_time);
			log.info("PreferTemperatureBySurveyJobService(id : "+jec.getJobDetail().getName()+") end.......................");			
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
