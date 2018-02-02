package com.pineone.icbms.sda.itf.ci.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

/**
 * CI용 interface
 */
public interface CiService {

	/**
	 * 한건조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	String selectOne(String idx) throws Exception;
	
	/**
	 * 입력
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int insert(Map<String, Object> map) throws Exception;
	
	/**
	 * 수정
	 * @param ciid
	 * @return
	 * @throws Exception
	 */
	int update(String ciid) throws Exception;
	
	/**
	 * 수정
	 * @param ciDTO
	 * @return
	 * @throws Exception
	 */
	int update(CiDTO[] ciDTO) throws Exception;
	
	/**
	 * 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int update(Map<String, Object> map) throws Exception;
	
	/**
	 * 삭제
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int delete(Map<String, Object> map) throws Exception;
	
	/**
	 * 삭제
	 * @param ciDTO
	 * @return
	 * @throws Exception
	 */
	int delete(CiDTO[] ciDTO) throws Exception;
	
	/**
	 * 여러건 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<CiDTO> selectList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 단건 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public CiDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * id체크
	 * @param ciid
	 * @return
	 * @throws Exception
	 */
	int checkId(String ciid) throws Exception;

}
