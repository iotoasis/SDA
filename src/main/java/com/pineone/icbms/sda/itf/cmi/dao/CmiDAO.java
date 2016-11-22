package com.pineone.icbms.sda.itf.cmi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;

@Repository("cmiDAO")
public class CmiDAO extends AbstractDAO{
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectList(Map<String, Object> commandMap) throws Exception{
		return (List<Map<String, Object>>) selectList("itf.cmi.selectList", commandMap);
	}
	
	@SuppressWarnings("unchecked") 
	public int insert(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmiDTO> list = (ArrayList<CmiDTO>)map.get("list");
		cnt = (int)insert("itf.cmi.insert", list);
		return cnt;
	}
	
	@SuppressWarnings("unchecked") 
	public int update(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmiDTO> list = (ArrayList<CmiDTO>)map.get("list");
		cnt = (int)insert("itf.cmi.update", list);
		return cnt;
	}

	@SuppressWarnings("unchecked") 
	public int delete(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmiDTO> list = (ArrayList<CmiDTO>)map.get("list");
		cnt = (int)insert("itf.cmi.delete", list);
		return cnt;
	}

}
