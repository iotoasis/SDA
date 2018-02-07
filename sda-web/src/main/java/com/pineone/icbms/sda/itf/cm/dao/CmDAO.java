package com.pineone.icbms.sda.itf.cm.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

/**
 * CM용 DAO
 */
@Repository("cmDAO")
public class CmDAO extends AbstractDAO{
	/**
	 * 목록 조회
	 * @param commandMap
	 * @returnList<CmCiDTO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CmCiDTO> selectCmCmiCiList(Map<String, Object> commandMap) throws Exception{
		return (List<CmCiDTO>) selectList("itf.cm.selectCmCmiCiList", commandMap);
	}

	/**
	 * 단건 조회
	 * @param commandMap
	 * @return CmCiDTO
	 * @throws Exception
	 */
	public CmCiDTO selectCmCmiCiOne(Map<String, Object> commandMap) throws Exception{
		return (CmCiDTO)selectOne("itf.cm.selectCmCmiCiOne", commandMap);
	}
	
	/**
	 * 조회
	 * @param commandMap
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception{
		return (List<Map<String, Object>>)selectList("itf.cm.select",  commandMap);
	}
 
	/**
	 * 목록조회
	 * @param commandMap
	 * @return List<CmCiDTO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception{
		return (List<CmCiDTO>)selectList("itf.cm.selectCmCiList",  commandMap);
	}
	
	/**
	 * 등록
	 * @param map
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") 
	public int insert(Map<String, Object> map) throws Exception{
		int cnt = 0; 
		List<CmDTO> list = (ArrayList<CmDTO>)map.get("list");
		cnt = Integer.parseInt(insert("itf.cm.insert", list).toString());
		return cnt;
	}
	
	/**
	 * 삭제
	 * @param commandMap
	 * @return int
	 * @throws Exception
	 */
	public int delete(Map<String, Object> commandMap) throws Exception {
		return Integer.parseInt( (String) update("itf.cm.delete", commandMap));
	}
	
	/**
	 * 목록 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CmDTO> selectList(Map<String, Object> commandMap) throws Exception {
		return (List<CmDTO>)selectList("itf.cm.selectList", commandMap);
	}
	
	/**
	 * 단건조히
	 * @param commandMap
	 * @return CmDTO
	 * @throws Exception
	 */
	public CmDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (CmDTO)selectOne("itf.cm.selectOne", commandMap);
	}

	/**
	 * ID체크
	 * @param cmid
	 * @return int
	 * @throws Exception
	 */
	public int checkId(String cmid) throws Exception {
		return Integer.parseInt(selectOne("itf.cm.checkId", cmid).toString());
	}
}