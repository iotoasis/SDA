package com.pineone.icbms.sda.itf.cm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.cm.dao.CmDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

/**
 * CM관련 서비스 구현체
 */
@Service("cmService")
public class CmServiceImpl implements CmService{ 
	@Resource(name="cmDAO")
	private CmDAO cmDAO;
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO;
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#selectList(java.util.Map)
	 */
	public List<CmDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<CmDTO> list = new ArrayList<CmDTO>();
		list = cmDAO.selectList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#selectOne(java.util.Map)
	 */
	public CmDTO selectOne(Map<String, Object> commandMap) throws Exception{
		CmDTO cmDTO = new CmDTO();
		cmDTO = cmDAO.selectOne(commandMap);
		
		// 데이타가 없으면 오류발생시킴
		if (cmDTO == null || cmDTO.getCmid() == null) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return cmDTO ;
		
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#selectCmCmiCiList(java.util.Map)
	 */
	public List<CmCiDTO> selectCmCmiCiList(Map<String, Object> commandMap) throws Exception {
		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		list = cmDAO.selectCmCmiCiList(commandMap);

		// 데이타가 없으면 오류발생시킴
		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#selectCmCmiCiOne(java.util.Map)
	 */
	public CmCiDTO selectCmCmiCiOne(Map<String, Object> commandMap) throws Exception{
		CmCiDTO cmCiDTO = new CmCiDTO();
		cmCiDTO = cmDAO.selectCmCmiCiOne(commandMap);
		
		// 데이타가 없으면 오류발생시킴
		if (cmCiDTO == null || cmCiDTO.getTnsda_context_model_cmid() == null) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return cmCiDTO ;
		
	}
 
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#selectCmCiList(java.util.Map)
	 */
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception {
		return cmDAO.selectCmCiList(commandMap);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#delete(java.util.Map)
	 */
	public int delete(Map<String, Object> map) throws Exception {
		return Integer.parseInt(cmDAO.update("itf.cm.delete", map).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#update(java.lang.String)
	 */
	public int update(String cmid) throws Exception{
		return Integer.parseInt(cmDAO.update("update", cmid).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#checkId(java.lang.String)
	 */
	public int checkId(String cmid) throws Exception {
		return cmDAO.checkId(cmid);
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#insert(java.util.Map)
	 */
	public int insert(Map<String, Object> map) throws Exception{
		return Integer.parseInt(cmDAO.insert("itf.cm.insert", map).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.cm.service.CmService#update(java.util.Map)
	 */
	public int update(Map<String, Object> commandMap) throws Exception {
		return Integer.parseInt(cmDAO.update("itf.cm.update", commandMap).toString());	
	}
}
