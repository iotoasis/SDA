package com.pineone.icbms.sda.subscribe.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackDTO;
import com.pineone.icbms.sda.subscribe.dto.SparqlListDTO;

/**
 * Callback용 DAO
 */
@Repository("callbackDAO")
public class CallbackDAO extends AbstractDAO {

	/**
	 * 목록조회
	 * @throws Exception
	 * @return List<Map<String,Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectList() throws Exception {
		return (List<Map<String, Object>>) selectList("callback.selectList");
	}
	
	/**
	 * ID확인
	 * @throws Exception
	 * @return HashMap<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectInsertId() throws Exception {
		return (HashMap<String, Object>) selectOne("callback.selectInsertId");
	}

	/**
	 * 최대 sequence값 
	 * @throws Exception
	 * @return HashMap<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectMaxSeq() throws Exception {
		return (HashMap<String, Object>) selectOne("callback.selectmaxseq");
	}

	/**
	 * 등록
	 * @param callbackDTO
	 * @throws Exception
	 * @return int
	 */
	public int insert(CallbackDTO callbackDTO) throws Exception {
		return Integer.parseInt( update("callback.insert", callbackDTO).toString());
	}

	/**
	 * sparql 목록 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @return List<SparqlListDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<SparqlListDTO> selectSparqlList(Map<String, Object> commandMap) throws Exception {
		return (List<SparqlListDTO>) selectList("callback.selectSparqlList", commandMap);
	}

}
