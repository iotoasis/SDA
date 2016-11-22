package com.pineone.icbms.sda.itf.cm.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

@Repository("cmDAO")
public class CmDAO extends AbstractDAO{
	@SuppressWarnings("unchecked")
	public List<CmCiDTO> selectList(Map<String, Object> commandMap) throws Exception{
		return (List<CmCiDTO>) selectList("itf.cm.selectList", commandMap);
	}

	public CmCiDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (CmCiDTO)selectOne("itf.cm.selectOne", commandMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception{
		return (List<Map<String, Object>>)selectList("itf.cm.select",  commandMap);
	}

	// SO �뒪耳�伊댁뿉�꽌 �궗�슜�맖
	@SuppressWarnings("unchecked")
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception{
		return (List<CmCiDTO>)selectList("itf.cm.selectCmCiList",  commandMap);
	}

	
	@SuppressWarnings("unchecked") 
	public int insert(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmDTO> list = (ArrayList<CmDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cm.insert", list).toString());
		return cnt;
	}
	
	@SuppressWarnings("unchecked") 
	public int update(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmDTO> list = (ArrayList<CmDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cm.update", list).toString());
		return cnt;
	}

	@SuppressWarnings("unchecked") 
	public int delete(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmDTO> list = (ArrayList<CmDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cm.delete", list).toString());
		return cnt;
	}

}
