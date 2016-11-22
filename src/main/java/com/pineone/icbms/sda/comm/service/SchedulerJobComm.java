package com.pineone.icbms.sda.comm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.pineone.icbms.sda.sch.dto.SchDTO;
import com.pineone.icbms.sda.sch.dto.SchHistDTO;
import com.pineone.icbms.sda.sch.service.SchService;

@Service
public class SchedulerJobComm implements ApplicationContextAware {
	private static ApplicationContext context;
	MongoClient mongoClient;
	DB db = null;
	SchDTO schDTO;

	private Log log = LogFactory.getLog(this.getClass());

	// 사용자id(나중에 적절한 값으로 변경해야함)
	String user_id = this.getClass().getName();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public SchDTO getSchDTO(JobExecutionContext jec) throws Exception {
		Map<String, Object> commandMap = new HashMap<String, Object>();
		commandMap.put("task_group_id", jec.getJobDetail().getGroup());
		commandMap.put("task_id", jec.getJobDetail().getName());
		SchService schService = getContext().getBean(SchService.class);
		try {
			schDTO = (SchDTO) schService.selectOne(commandMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("schDTO in SchedulerJobComm =====>" + schDTO.toString());
		return schDTO;
	}

	// schHist테이블에 데이타 insert
	public int insertSchHist(JobExecutionContext jec, int work_cnt, String start_time, String end_time)
			throws Exception {
		int updateCnt = 0;
		
		//schDTO에 값을 넣기 위함
		if(schDTO == null || schDTO.equals("")) {
			getSchDTO(jec);
		}
		
		SchHistDTO schHistDTO = new SchHistDTO();
		schHistDTO.setTask_group_id(jec.getJobDetail().getGroup());
		schHistDTO.setTask_id(jec.getJobDetail().getName());
		schHistDTO.setStart_time(start_time);
		schHistDTO.setTask_class(schDTO.getTask_class());
		schHistDTO.setTask_expression(schDTO.getTask_expression());
		schHistDTO.setWork_cnt(work_cnt);
		schHistDTO.setWork_time(end_time);
		schHistDTO.setCuser(user_id);
		schHistDTO.setUuser(user_id);

		List<SchHistDTO> list = new ArrayList<SchHistDTO>();
		Map<String, Object> updateSchHistMap = new HashMap<String, Object>();
		list.add(schHistDTO);
		updateSchHistMap.put("list", list);
		try {
			SchService schService = getContext().getBean(SchService.class);
			updateCnt = schService.insertSchHist(updateSchHistMap);
		} catch (DuplicateKeyException e) {
			// pass
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}
		return updateCnt;
	}

	// SchHist에 finish time, work_result를 update
	public int updateFinishTime(JobExecutionContext jec, String start_time, String finish_time, String work_result)
			throws Exception {
		return updateFinishTime(jec, start_time, finish_time, work_result, "", "");
	}

	// SchHist에 finish time, work_result, triple_file_name, triple_check_result를
	// update
	public int updateFinishTime(JobExecutionContext jec, String start_time, String finish_time, String work_result,
			String triple_file_name, String triple_check_result) throws Exception {
		int updateCnt = 0;
		SchHistDTO schHistDTO = new SchHistDTO();
		schHistDTO.setTask_group_id(jec.getJobDetail().getGroup());
		schHistDTO.setTask_id(jec.getJobDetail().getName());
		schHistDTO.setStart_time(start_time);
		schHistDTO.setFinish_time(finish_time);
		schHistDTO.setWork_result(work_result);
		schHistDTO.setTriple_file_name(triple_file_name);
		schHistDTO.setTriple_check_result(triple_check_result);
		
		schHistDTO.setUuser(user_id);

		List<SchHistDTO> list = new ArrayList<SchHistDTO>();
		Map<String, Object> updateMap = new HashMap<String, Object>();
		list.add(schHistDTO);
		updateMap.put("list", list);
		try {
			SchService schService = getContext().getBean(SchService.class);
			updateCnt = schService.updateFinishTime(updateMap);
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}
		return updateCnt;

	}

	// Sch에 last_work_time을 update
	public int updateLastWorkTime(JobExecutionContext jec, String endDate) throws Exception {
		int updateCnt = 0;
		SchDTO schDTO = new SchDTO();
		schDTO.setTask_group_id(jec.getJobDetail().getGroup());
		schDTO.setTask_id(jec.getJobDetail().getName());
		schDTO.setLast_work_time(endDate);
		schDTO.setUuser(user_id);

		List<SchDTO> list = new ArrayList<SchDTO>();
		Map<String, Object> updateSchMap = new HashMap<String, Object>();
		list.add(schDTO);
		updateSchMap.put("list", list);
		try {
			SchService schService = getContext().getBean(SchService.class);
			updateCnt = schService.updateLastWorkTime(updateSchMap);
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}
		return updateCnt;
	}

}