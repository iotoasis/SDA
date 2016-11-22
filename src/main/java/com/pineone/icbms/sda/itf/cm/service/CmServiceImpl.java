package com.pineone.icbms.sda.itf.cm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.sf.service.SparqlService;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.cm.dao.CmDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;

@Service("cmService")
public class CmServiceImpl implements CmService{ 
	private Log log = LogFactory.getLog(this.getClass());
	private SparqlService sparqlService = new SparqlService();
	
	@Resource(name="cmDAO")
	private CmDAO cmDAO;
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO;
	
	// 목록조회
	@Override
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
	@Override
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
