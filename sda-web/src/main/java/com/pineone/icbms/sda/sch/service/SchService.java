package com.pineone.icbms.sda.sch.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;

/**
 * 스케줄 서비스 인터페이스
 */
public interface SchService { 

	/**
	 * 목록 조회
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
	SchDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 마지막 수행시간 수정
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
	int insertSchHist(Map<String, Object> map) throws Exception;
	
	/**
	 * 종료시간 수정 
	 * @param map
	 * @throws Exception
	 * @return int
	 */
	int updateFinishTime(Map<String, Object> map) throws Exception;
}
