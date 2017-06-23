package com.pineone.icbms.sda.itf.template.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

@Repository("templateDAO")
public class TemplateDAO extends AbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<TemplateDTO> selectList(Map<String, Object> commandMap) throws Exception {
		return (List<TemplateDTO>)selectList("itf.template.selectList", commandMap);
	}
	
	public TemplateDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (TemplateDTO)selectOne("itf.template.selectOne", commandMap);
	}

}
