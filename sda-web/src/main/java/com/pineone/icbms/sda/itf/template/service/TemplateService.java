package com.pineone.icbms.sda.itf.template.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;

public interface TemplateService {
	
	public List<TemplateDTO> selectList(Map<String, Object> commandMap)throws Exception;
	public TemplateDTO selectOne(Map<String, Object> commandMap) throws Exception;


}
