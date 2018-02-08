package com.pineone.icbms.sda.itf.template.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;

/**
 * 템플릿용 서비스용 인터페이스
 */
public interface TemplateService {
	
	/**
	 * 목록조회
	 * @param commandMap
	 * @return List<TemplateDTO>
	 * @throws Exception
	 */
	public List<TemplateDTO> selectList(Map<String, Object> commandMap)throws Exception;
	
	/**
	 * 단건조회
	 * @param commandMap
	 * @return TemplateDTO
	 * @throws Exception
	 */
	public TemplateDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * ID체크
	 * @param tmid
	 * @return int
	 * @throws Exception
	 */
	int checkId(String tmid) throws Exception;


}
