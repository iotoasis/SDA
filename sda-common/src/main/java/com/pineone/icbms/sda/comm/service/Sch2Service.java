package com.pineone.icbms.sda.comm.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

/**
 * 스케줄용 서비스
 */
public interface Sch2Service { 

	/**
	 * 목록조회
	 * @throws Exception
	 * @return List<SchDTO>
	 */
	List<SchDTO> selectList() throws Exception;
	
	/**
	 * 조회
	 * @param commandMap
	 * @throws Exception
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 단건조회
	 * @param commandMap
	 * @throws Exception
	 * @return SchDTO
	 */
	SchDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 마지막 작업시간 update
	 * @param map
	 * @throws Exception
	 * @return int
	 */
	int updateLastWorkTime(Map<String, Object> map) throws Exception;
	
	/**
	 * 스케줄 히스토리 등록
	 * @param map
	 * @throws Exception
	 * @return int
	 */
	int insertSchHist(Map<String, List<SchHistDTO>> map) throws Exception;
	
	/**
	 * 중료시간 업데이트
	 * @param map
	 * @throws Exception
	 * @return int
	 */
	int updateFinishTime(Map<String, Object> map) throws Exception;
}
