package com.pineone.icbms.sda.itf.cm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.cm.dao.CmDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

@Service("cmService")
public class CmServiceImpl implements CmService{ 
	private Log log = LogFactory.getLog(this.getClass());

	@Resource(name="cmDAO")
	private CmDAO cmDAO;
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO;
	
	// CMALL
	public List<CmDTO> selectCMList(Map<String, Object> commandMap) throws Exception {
		List<CmDTO> list = new ArrayList<CmDTO>();
		list = cmDAO.selectCMList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	// 목록조회
	public List<CmCiDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		list = cmDAO.selectList(commandMap);

		// 데이타가 없으면 오류발생시킴
		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}

	// 한건조회
	public CmCiDTO selectOne(Map<String, Object> commandMap) throws Exception{
		CmCiDTO cmCiDTO = new CmCiDTO();
		cmCiDTO = cmDAO.selectOne(commandMap);
		
		// 데이타가 없으면 오류발생시킴
		if (cmCiDTO == null || cmCiDTO.getTnsda_context_model_cmid() == null) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return cmCiDTO ;
		
	}

	// SO스케줄등에서 사용됨 
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception {
		return cmDAO.selectCmCiList(commandMap);
	}

}
