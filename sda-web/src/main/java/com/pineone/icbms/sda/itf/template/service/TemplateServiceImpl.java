package com.pineone.icbms.sda.itf.template.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.template.dao.TemplateDAO;
import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

	@Resource(name="templateDAO")
	private TemplateDAO templateDAO;

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.template.service.TemplateService#selectList(java.util.Map)
	 */
	public List<TemplateDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<TemplateDTO> list = new ArrayList<TemplateDTO>();
		list = templateDAO.selectList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.template.service.TemplateService#selectOne(java.util.Map)
	 */
	public TemplateDTO selectOne(Map<String, Object> commandMap) throws Exception{
		TemplateDTO templateDTO = new TemplateDTO();
		templateDTO = templateDAO.selectOne(commandMap);
		
		if (templateDTO == null || templateDTO.getTmid() == null) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return templateDTO ;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.template.service.TemplateService#checkId(java.lang.String)
	 */
	public int checkId(String tmid) throws Exception {
		return templateDAO.checkId(tmid);
	}
}