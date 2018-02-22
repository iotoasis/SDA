package com.pineone.icbms.sda.sch.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.comm.SchedulerJobComm;
import com.pineone.icbms.sda.sf.QueryService;
import com.pineone.icbms.sda.sf.QueryServiceFactory;
import com.pineone.icbms.sda.sf.SparqlFusekiQueryImpl;

/**
 * Python3수행 서비스
 */
@Service
public class RunPython3OfStrSubService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private static AtomicInteger ai = new AtomicInteger();
	
	/**
	 * 처리 메서드
	 * @param jec
	 * @throws Exception
	 * @return void
	 */
	public void collect(JobExecutionContext jec) throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		start_time = start_time + "S"+String.format("%010d", ai.getAndIncrement());

		String end_time = Utils.dateFormat.format(new Date());

		log.info("RunPython3OfStrSubService(id : " + jec.getJobDetail().getName() + ") start.......................");

		StringBuffer sql = new StringBuffer();
		
		sql.append(Utils.NEW_LINE);		
		sql.append(" python3.5 /svc/apps/sda/crawl/str_sub.py ");

		log.debug("sql ==>\n"+sql.toString());
		
		int cnt = 0;
		StringBuffer work_result = new StringBuffer();
		
		QueryService sparqlService = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL);
		
		String day_delete_query = " delete { <http://www.iotoasis.org/ontology/day> <http://www.iotoasis.org/ontology/hasStartTime> ?starttime . } "
										     +" where {<http://www.iotoasis.org/ontology/day> <http://www.iotoasis.org/ontology/hasStartTime> ?starttime  . } ";  
		
		String day_insert_query = "	 insert data { <http://www.iotoasis.org/ontology/day> <http://www.iotoasis.org/ontology/hasStartTime>  \"@{arg0}\"^^xsd:string  . } ";

		String night_delete_query = " delete { <http://www.iotoasis.org/ontology/night> <http://www.iotoasis.org/ontology/hasStartTime> ?starttime . } "
			     								+" where {<http://www.iotoasis.org/ontology/night> <http://www.iotoasis.org/ontology/hasStartTime> ?starttime  . } ";  

		String night_insert_query = " insert data { <http://www.iotoasis.org/ontology/night> <http://www.iotoasis.org/ontology/hasStartTime>  \"@{arg0}\"^^xsd:string  . } ";

		try {
			QueryService sqi = QueryServiceFactory.create(Utils.QUERY_GUBUN.SHELL);
			List<Map<String, String>> list  = sqi.runQuery(sql.toString(), new String[]{""});
			cnt = list.size();
			
			// sch_hist테이블에 data insert
			insertSchHist(jec, cnt, start_time, Utils.dateFormat.format(new Date()));

			for(int m = 0; m < list.size(); m++) {
				work_result.append(list.get(m).get("result"));
				String[] split_str = list.get(m).get("result").split(",");
				try {
					// fuseki에 등록 시작
					log.debug("RunPython3Service start ......................");
							
					// 낮시간 update
					((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(day_delete_query, Utils.getSparQlHeader()+day_insert_query, new String[]{split_str[3]}, Utils.QUERY_DEST.ALL.toString());
					
					// 밤시간 update
					((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(night_delete_query, Utils.getSparQlHeader()+night_insert_query, new String[]{split_str[4]}, Utils.QUERY_DEST.ALL.toString());
					
					// fuseki등록 종료
					log.debug("RunPython3Service  end ......................");		
				} catch (Exception e) {
					log.debug("Exception :" + e.getLocalizedMessage());
					work_result.append(e.getMessage());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			work_result.append(e.getMessage());
		}

		String triple_check_result_file = work_result.toString();
		String triple_path_file = Utils.NoTripleFile;
		String triple_check_result = Utils.NotCheck;

		// sch_hist테이블의 finish_time에 날짜및 정보 설정
		String finish_time = Utils.dateFormat.format(new Date());
		updateFinishTime(jec, start_time, finish_time, triple_check_result_file, triple_path_file, triple_check_result);
		
		// endDate값을 sch테이블의 last_work_time에 update
		updateLastWorkTime(jec, end_time);
		
		log.info("RunPython3OfStrSubService(id : " + jec.getJobDetail().getName() + ") end.......................");
	}

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext arg0)  throws JobExecutionException{
		try {
			collect(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception ("+this.getClass()+")  ....................................> "+e.toString());			
			throw new JobExecutionException(e);
		}
	}
}
