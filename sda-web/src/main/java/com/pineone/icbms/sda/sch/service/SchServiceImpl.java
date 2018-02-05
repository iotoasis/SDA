package com.pineone.icbms.sda.sch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;
import com.pineone.icbms.sda.sch.dao.SchDAO;
import com.pineone.icbms.sda.sch.dao.SchHistDAO;

/**
 * 스케줄 서비스 구현체
 */
@Service("schService")
public class SchServiceImpl implements SchService{ 
	@Resource(name="schDAO")
	private SchDAO schDAO;
	
	@Resource(name="schHistDAO")
	private SchHistDAO schHistDAO;
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sch.service.SchService#selectList()
	 */
	public List<SchDTO> selectList() throws Exception {
		return schDAO.selectList();
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sch.service.SchService#select(java.util.Map)
	 */
	public List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sch.service.SchService#selectOne(java.util.Map)
	 */
	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		return (SchDTO)schDAO.selectOne(commandMap);
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sch.service.SchService#updateLastWorkTime(java.util.Map)
	 */
	public int updateLastWorkTime(Map<String, Object> map) throws Exception {
		int cnt = -1; 
		@SuppressWarnings("unchecked")
		List<SchDTO> list = (ArrayList<SchDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchDTO schDTO = (SchDTO)list.get(i);
			cnt = schDAO.updateLastWorkTime(schDTO);
		}
		return cnt;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sch.service.SchService#insertSchHist(java.util.Map)
	 */
	public int insertSchHist(Map<String, Object> map) throws Exception {
		int cnt = -1; 
		@SuppressWarnings("unchecked")
		List<SchHistDTO> list = (ArrayList<SchHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			cnt = schHistDAO.insert(schHistDTO);
		}
		return cnt;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sch.service.SchService#updateFinishTime(java.util.Map)
	 */
	public int updateFinishTime(Map<String, Object> map) throws Exception {
		int cnt = -1; 
		@SuppressWarnings("unchecked")
		List<SchHistDTO> list = (ArrayList<SchHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			cnt = schDAO.updateFinishTime(schHistDTO);
		}
		return cnt;
	}
}