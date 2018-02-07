package com.pineone.icbms.sda.itf.template.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

/**
 * 템플릿용 DAO
 */
@Repository("templateDAO")
public class TemplateDAO extends AbstractDAO {
	
	/**
	 * 
	 * @param commandMap
	 * @return List<TemplateDTO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TemplateDTO> selectList(Map<String, Object> commandMap) throws Exception {
		return (List<TemplateDTO>)selectList("itf.template.selectList", commandMap);
	}
	
	/**
	 * 단건 조회
	 * @param commandMap
	 * @return TemplateDTO
	 * @throws Exception
	 */
	public TemplateDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (TemplateDTO)selectOne("itf.template.selectOne", commandMap);
	}
	
	/**
	 * ID체크
	 * @param tmid
	 * @return int
	 * @throws Exception
	 */
	public int checkId(String tmid) throws Exception {
		return Integer.parseInt(selectOne("itf.template.checkId", tmid).toString());
	}

}
