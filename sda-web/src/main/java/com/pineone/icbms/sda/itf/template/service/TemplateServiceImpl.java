package com.pineone.icbms.sda.itf.template.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.itf.template.dao.TemplateDAO;
import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {
	private Log log = LogFactory.getLog(this.getClass());

	@Resource(name="templateDAO")
	private TemplateDAO templateDAO;

	public List<TemplateDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<TemplateDTO> list = new ArrayList<TemplateDTO>();
		list = templateDAO.selectList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	// /template/{tmid}
	public TemplateDTO selectOne(Map<String, Object> commandMap) throws Exception{
		TemplateDTO templateDTO = new TemplateDTO();
		templateDTO = templateDAO.selectOne(commandMap);
		
		if (templateDTO == null || templateDTO.getTmid() == null) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return templateDTO ;
		
	}
	
	public int checkId(String tmid) throws Exception {
		return templateDAO.checkId(tmid);
	}
	
	

}
