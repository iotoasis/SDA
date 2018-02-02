package com.pineone.icbms.sda.itf.cmi.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

/**
 * CMI정보를 처리하는 DAO
 */
@Repository("cmiDAO")
public class CmiDAO extends AbstractDAO{
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectList(Map<String, Object> commandMap) throws Exception{
		return (List<Map<String, Object>>) selectList("itf.cmi.selectList", commandMap);
	}
	
	/**
	 * 등록
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public int insert(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmiDTO> list = (ArrayList<CmiDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cmi.insert", list).toString());
		return cnt;
	}
	
	/**
	 * 수정
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public int update(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmiDTO> list = (ArrayList<CmiDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cmi.update", list).toString());
		return cnt;
	}

	/**
	 * 삭제
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public int delete(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmiDTO> list = (ArrayList<CmiDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cmi.delete", list).toString());
		return cnt;
	}

}
