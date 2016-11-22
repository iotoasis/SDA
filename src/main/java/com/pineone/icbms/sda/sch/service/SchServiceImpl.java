package com.pineone.icbms.sda.sch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.sch.dao.SchDAO;
import com.pineone.icbms.sda.sch.dao.SchHistDAO;
import com.pineone.icbms.sda.sch.dto.SchDTO;
import com.pineone.icbms.sda.sch.dto.SchHistDTO;

@Service("schService")
public class SchServiceImpl implements SchService{ 
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="schDAO")
	private SchDAO schDAO;
	
	@Resource(name="schHistDAO")
	private SchHistDAO schHistDAO;
	
	// 목록조회
	@Override
	public List<SchDTO> selectList() throws Exception {
		return schDAO.selectList();
	}

	@Override
	public List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		return (SchDTO)schDAO.selectOne(commandMap);
	}
	
	// last_work_time컬럼에 값을 업데이트한다.
	public int updateLastWorkTime(Map<String, Object> map) throws Exception {
		int cnt = 0; 
		@SuppressWarnings("unchecked")
		List<SchDTO> list = (ArrayList<SchDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchDTO schDTO = (SchDTO)list.get(i);
			cnt = schDAO.updateLastWorkTime(schDTO);
		}
		return cnt;
	}
	
	// sch_hist
	public int insertSchHist(Map<String, Object> map) throws Exception {
		int cnt = 0; 
		@SuppressWarnings("unchecked")
		List<SchHistDTO> list = (ArrayList<SchHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			cnt = schHistDAO.insert(schHistDTO);
		}
		return cnt;
	}
	
	public int updateFinishTime(Map<String, Object> map) throws Exception {
		int cnt = 0; 
		@SuppressWarnings("unchecked")
		List<SchHistDTO> list = (ArrayList<SchHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			cnt = schDAO.updateFinishTime(schHistDTO);
		}
		return cnt;
	}

}
