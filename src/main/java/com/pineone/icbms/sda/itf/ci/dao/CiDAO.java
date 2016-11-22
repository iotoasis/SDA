package com.pineone.icbms.sda.itf.ci.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

@Repository("ciDAO")
public class CiDAO extends AbstractDAO{
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getList_(Map<String, Object> map) throws Exception{
		return (List<Map<String, Object>>)selectList("itf.ci.selectList", map);
	}
	@SuppressWarnings("unchecked") 
	public List<CiDTO> selectSparqlListByCiids(String[] ciids) throws Exception{
		List<CiDTO> ciDTO = new ArrayList<CiDTO>();
		ciDTO = selectList("itf.ci.selectSparqlListByCiids", ciids);
		return ciDTO;
	}
	
}
