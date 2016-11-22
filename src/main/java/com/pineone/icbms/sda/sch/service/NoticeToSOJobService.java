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
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.service.SchedulerJobComm;
import com.pineone.icbms.sda.sf.service.SparqlService;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.service.CmService;
import com.pineone.icbms.sda.sch.dto.SchDTO;

@Service
public class NoticeToSOJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());

	// SO요청을 스케줄링함
	public void runner(JobExecutionContext jec) throws Exception {
		String start_time = "";
		String finish_time = "";
		Gson gson = new Gson();
		String callback_result_uri = Utils.getSdaProperty("com.pineone.icbms.sda.so.callback_result_uri");

		String so_notice_time = "";
		Map<String, Object> map;
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		SparqlService sparqlService = new SparqlService();

		log.debug("NoticeToSOJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
		try {
			start_time = Utils.dateFormat.format(new Date());

			// task_group_id, task_id에 대한 schDTO정보

			// cmid를 기준으로 ciid에 설정된 sparql을 가져온다.
			String cmid = jec.getJobDetail().getName();
			List<CmCiDTO> list = new ArrayList<CmCiDTO>();

			Map<String, Object> commandMap = new HashMap<String, Object>();
			commandMap.put("cmid", cmid);

			CmService cmService = getContext().getBean(CmService.class);
			list = cmService.selectCmCiList(commandMap);

			// sch_hist테이블에 data insert(work_cnt는 list목록의 개수(ci개수)로 설정함)
			insertSchHist(jec, list.size(), start_time, start_time);

			// getSparqlResult()를 사용하기위한 data준비(실행에 필요한 sparql을 모두 넘겨줌)
			// 여러 sparql을 실행후 각 결과값에서 공통적으로 존재하는 값을 추출함
			List<String> sparqlList = new ArrayList<String>();
			for (CmCiDTO cmCiDTO : list) {
				sparqlList.add(cmCiDTO.getSparql());
			}

			returnList = sparqlService.runSparqlUniqueResult(sparqlList);

			log.debug("returnList.toString() =================> " + returnList.toString());
			
			ResponseMessage responseMessage = new ResponseMessage();

			String jsonMsg = "";
			if(returnList.size() > 0) {
				// SO에 결과 전송 시작
				so_notice_time = Utils.dateFormat.format(new Date());
				map = new HashMap<String, Object>();
				map.put("cmd", Utils.CALLBACK_SCHEDULE);
				map.put("contextId", cmid);
				map.put("time", so_notice_time);
				map.put("domains", returnList);
		
				jsonMsg = gson.toJson(map);
	
				log.debug("Request message of Schedule for sending to SO =>  " + jsonMsg);
				responseMessage = Utils.requestData(callback_result_uri, jsonMsg); // POST
				log.debug("responseMessage of Schedule from SO => " + responseMessage.toString());
				// SO에 결과 전송 끝
			} else {
				jsonMsg =Utils.NotSendToSo;
			}

			finish_time = Utils.dateFormat.format(new Date());
			if(returnList.size() > 0) {			// SO에 전송함
					// sch_hist테이블의 finish_time에 날짜 설정
					// (start_time은 key3에 해당되므로 key값인 start_time을 보내준다.)
					if (responseMessage.getCode() == 200) {
						updateFinishTime(jec, start_time, finish_time,
								"so_notice_time: " + so_notice_time + ", msg:  " + returnList.toString(), Utils.NoTripleFile, Utils.NotCheck);
					} else {
						updateFinishTime(jec, start_time, finish_time, responseMessage.toString(), Utils.NoTripleFile, Utils.NotCheck);
					}
			} else {			// SO전송하지 않음
				updateFinishTime(jec, start_time, finish_time, jsonMsg , Utils.NoTripleFile, Utils.NotCheck);
			}

			// finish_time값을 sch테이블의 last_work_time에 update
			updateLastWorkTime(jec, finish_time);
			
			log.debug("NoticeToSOJobService(id : "+jec.getJobDetail().getName()+") end.......................");			
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
