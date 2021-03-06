package com.pineone.icbms.sda.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;
import com.pineone.icbms.sda.comm.service.Sch2Service;
import com.pineone.icbms.sda.comm.service.Sch2ServiceImpl;
import com.pineone.icbms.sda.comm.util.Utils;

/**
 * 스케줄러 공통 클래스
 */
public class SchComm {
	SchDTO schDTO = null;
	private final Sch2Service sch2Service = new Sch2ServiceImpl();
	private final Log log = LogFactory.getLog(this.getClass());
	private final String user_id =this.getClass().getName();
	
	/**
	 * 스케줄 히스토리 등록
	 * @param task_group_id
	 * @param task_id
	 * @param start_time
	 * @param work_cnt
	 * @param end_time
	 * @throws Exception
	 * @return int
	 */
	public int insertSchHist(String task_group_id, String task_id, String start_time, int work_cnt, String end_time) throws Exception {
		log.debug("insertSchHist in SchComm start...."); 
		int updateCnt = 0;
		
		SchHistDTO schHistDTO = new SchHistDTO();
		
		schHistDTO.setTask_group_id(task_group_id);
		schHistDTO.setTask_id(task_id);
		
		schHistDTO.setStart_time(start_time);
		
		schHistDTO.setTask_class(this.getClass().getName());
		schHistDTO.setTask_expression(Utils.None);

		schHistDTO.setWork_cnt(work_cnt);
		schHistDTO.setWork_time(end_time);
		schHistDTO.setCuser(user_id);
		schHistDTO.setUuser(user_id);

		List<SchHistDTO> list = new ArrayList<SchHistDTO>(); 
		Map<String, List<SchHistDTO>> insertSchHistMap = new HashMap<String, List<SchHistDTO>>();
		list.add(schHistDTO);
		insertSchHistMap.put("list", list);
		try {
			updateCnt = sch2Service.insertSchHist(insertSchHistMap);
		} catch (Exception e) {
			throw e;
		}
		
		log.debug("insertSchHist in SchComm end.....");		
		return updateCnt;
	}

	/**
	 * 최종작업일시를 업데이트
	 * @param task_group_id
	 * @param task_id
	 * @param start_time
	 * @param finish_time
	 * @param work_result
	 * @throws Exception
	 * @return int
	 */
	public int updateFinishTime(String task_group_id, String task_id, String start_time, String finish_time, String work_result) throws Exception {
		return updateFinishTime(task_group_id, task_id, start_time, finish_time, work_result, Utils.None, Utils.None);
	}

	/**
	 * 종료시간 업데이트
	 * @param task_group_id
	 * @param task_id
	 * @param start_time
	 * @param finish_time
	 * @param work_result
	 * @param triple_file_name
	 * @param triple_check_result
	 * @throws Exception
	 * @return int
	 */
	public int updateFinishTime(String task_group_id, String task_id, String start_time, String finish_time, String work_result, 	String triple_file_name, String triple_check_result) throws Exception {
		log.debug("updateFinishTime in SchComm start.....");
		
		int updateCnt = 0;
		SchHistDTO schHistDTO = new SchHistDTO();
		
		schHistDTO.setTask_group_id(task_group_id);
		schHistDTO.setTask_id(task_id);

		schHistDTO.setTask_class(this.getClass().getName());
		schHistDTO.setTask_expression(Utils.None);
		
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
			updateCnt = sch2Service.updateFinishTime(updateMap);
		} catch (Exception e) {
			throw e;
		}
		
		log.debug("updateFinishTime in SchComm end.....");
		return updateCnt;
	}

	/**
	 * 마지막 작업시간 업데이트
	 * @param task_group_id
	 * @param task_id
	 * @param endDate
	 * @throws Exception
	 * @return int
	 */
	public int updateLastWorkTime(String task_group_id, String task_id, String endDate) throws Exception {
		log.debug("updateLastWorkTime in SchComm start.....");
		
		int updateCnt = 0;
		SchDTO schDTO = new SchDTO();
		
		schDTO.setTask_group_id(task_group_id);
		schDTO.setTask_id(task_id);
		
		schDTO.setLast_work_time(endDate);
		schDTO.setUuser(user_id);

		List<SchDTO> list = new ArrayList<SchDTO>();
		Map<String, Object> updateSchMap = new HashMap<String, Object>();
		list.add(schDTO);
		updateSchMap.put("list", list);
		try {
			updateCnt = sch2Service.updateLastWorkTime(updateSchMap);
		} catch (Exception e) {
			throw e;
		}
		log.debug("updateLastWorkTime in SchComm end.....");		
		return updateCnt;
	}

}