package com.pineone.icbms.sda.itf.cm.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

/**
 * CM서비스용 인터페이스
 */
public interface CmService { 

	/**
	 * 목록조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<CmCiDTO> selectCmCmiCiList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 단건 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public CmCiDTO selectCmCmiCiOne(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 목록 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<CmDTO> selectList(Map<String, Object> commandMap)throws Exception;
	
	/**
	 * 단건조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public CmDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int delete(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 수정
	 * @param ciid
	 * @return
	 * @throws Exception
	 */
	int update(String ciid) throws Exception;
	
	/**
	 * ID확인
	 * @param cmid
	 * @return
	 * @throws Exception
	 */
	int checkId(String cmid) throws Exception;
	
	/**
	 * 등록
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int insert(Map<String, Object> map) throws Exception;
	
	/**
	 * 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int update(Map<String, Object> map) throws Exception;

	/**
	 * 목록조회(SO 스케줄에서 사용됨)
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception;

	

}
