package com.pineone.icbms.sda.itf.cmi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.cmi.dao.CmiDAO;
import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;

/**
 * CMI서비스 구현체
 */
@Service("cmiService")
public class CmiServiceImpl implements CmiService{ 
	@Resource(name="cmiDAO")
	private CmiDAO cmiDAO;
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cmi.service.CmiService#selectList(java.util.Map)
	 */
	public List<CmiDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<CmiDTO> list = new ArrayList<CmiDTO>();

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cmi.service.CmiService#insert(java.util.Map)
	 */
	public int insert(Map<String, Object> map) throws Exception{
		return Integer.parseInt(cmiDAO.insert("itf.cmi.insert", map).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cmi.service.CmiService#update(java.util.Map)
	 */
	public int update(Map<String, Object> commandMap) throws Exception {
		return Integer.parseInt(cmiDAO.update("itf.cmi.update", commandMap).toString());	
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cmi.service.CmiService#delete(java.lang.String)
	 */
	public int delete(String cmid) throws Exception {
		return Integer.parseInt(cmiDAO.delete("itf.cmi.delete", cmid).toString()) ;
	}
}