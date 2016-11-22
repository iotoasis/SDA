package com.pineone.icbms.sda.subscribe.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackDTO;
import com.pineone.icbms.sda.subscribe.dto.SparqlListDTO;

@Repository("callbackDAO")
public class CallbackDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectList() throws Exception {
		return (List<Map<String, Object>>) selectList("callback.selectList");
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectInsertId() throws Exception {
		return (HashMap<String, Object>) selectOne("callback.selectInsertId");
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectMaxSeq() throws Exception {
		return (HashMap<String, Object>) selectOne("callback.selectmaxseq");
	}

	public int insert(CallbackDTO callbackDTO) throws Exception {
		return (int) update("callback.insert", callbackDTO);
	}

	// sparqlList
	@SuppressWarnings("unchecked")
	public List<SparqlListDTO> selectSparqlList(Map<String, Object> commandMap) throws Exception {
		return (List<SparqlListDTO>) selectList("callback.selectSparqlList", commandMap);
	}

}
