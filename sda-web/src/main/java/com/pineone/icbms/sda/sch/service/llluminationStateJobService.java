package com.pineone.icbms.sda.sch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.sch.dto.AggrDTO;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.comm.SchedulerJobComm;
import com.pineone.icbms.sda.sch.dao.AggrDAO;
import com.pineone.icbms.sda.sf.QueryService;
import com.pineone.icbms.sda.sf.QueryServiceFactory;
import com.pineone.icbms.sda.sf.SparqlFusekiQueryImpl;

@Service
public class llluminationStateJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private static AtomicInteger ai = new AtomicInteger();	
	
	// triple로 부터 집계를해서 domain에 스케줄링함
	public void runner(JobExecutionContext jec) throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		start_time = start_time + "S"+String.format("%010d", ai.getAndIncrement());

		AggrDAO aggrDAO;
		StringBuffer msg = new StringBuffer();

		log.info("llluminationStateJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
		try {
			List<AggrDTO> aggrList = new ArrayList<AggrDTO>();
			aggrDAO = getContext().getBean(AggrDAO.class);
			Map<String, String> commandMap = new HashMap<String, String>();
			commandMap.put("aggr_id", jec.getJobDetail().getName());
			
			aggrList = (List<AggrDTO>)aggrDAO.selectList(commandMap);

			insertSchHist(jec, aggrList.size(), start_time, Utils.dateFormat.format(new Date()));
			
			
			QueryService sparqlService = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL);
			
			List<Map<String, String>> argsResultList;
			argsResultList = sparqlService.runQuery(aggrList.get(0).getArgsql());
			
			for(int m = 0; m < argsResultList.size(); m++) {
				String avg_val = argsResultList.get(m).get("avg_val");
				String location = argsResultList.get(m).get("loc");
				String condition = argsResultList.get(m).get("cond");
				
				//delete->insert
				((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(aggrList.get(0).getDeleteql(), aggrList.get(0).getInsertql(), new String[]{location, condition}, Utils.QUERY_DEST.ALL.toString());
					msg.append(Utils.NEW_LINE);				
					msg.append("location["+m+"] ==>  ");
					msg.append(location);
					msg.append(" , value(");
					msg.append(avg_val);
					msg.append(") by ");
					msg.append(condition);
					msg.append(" updated.");

					log.debug(msg.toString());

			}
			
			if(argsResultList.size() == 0) {
				msg.append("No Result !");
			}

			String finish_time = Utils.dateFormat.format(new Date());
			updateFinishTime(jec, start_time, finish_time, msg.toString() , Utils.NoTripleFile, Utils.NotCheck);

			updateLastWorkTime(jec, finish_time);
			log.info("llluminationStateJobService(id : "+jec.getJobDetail().getName()+") end.......................");			
		} catch (Exception e) {
			updateFinishTime(jec, start_time, Utils.dateFormat.format(new Date()), e.getMessage());
			throw e;
		}
	}

	public void execute(JobExecutionContext arg0)  throws JobExecutionException{
		try {
			runner(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception ("+this.getClass()+")  ....................................> "+e.toString());			
			throw new JobExecutionException(e);
		}
	}

}
