package com.pineone.icbms.sda.itf.cmi.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;

/**
 * CMI서비스를 위한 인터페이스
 */
public interface CmiService { 
	
	/**
	 * 목록 조회
	 * @param commandMap
	 * @return List<CmiDTO>
	 * @throws Exception
	 */
	public List<CmiDTO> selectList(Map<String, Object> commandMap)throws Exception;
	
	/**
	 * 등록
	 * @param map
	 * @return int
	 * @throws Exception
	 */
	int insert(Map<String, Object> map) throws Exception;
	
	/**
	 * 수정
	 * @param map
	 * @return int
	 * @throws Exception
	 */
	int update(Map<String, Object> map) throws Exception;
	
	/**
	 * 삭제
	 * @param cmid
	 * @return int
	 * @throws Exception
	 */
	int delete(String cmid) throws Exception;
}