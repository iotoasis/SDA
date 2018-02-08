package com.pineone.icbms.sda.sf.service;

import java.util.List;
import java.util.Map;

/**
 * 시멘틱프레임용 서비스
 */
public interface SfService { 
	/**
	 * test수행(cmid를 받아서 test)
	 * @param commandMap
	 * @throws Exception
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getContext(Map<String, Object> commandMap) throws Exception;

	/**
	 * test수행(cmid및 args를 받아서 test)
	 * @param commandMap
	 * @param args
	 * @throws Exception
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getContext(Map<String, Object> commandMap, String args) throws Exception;
}
