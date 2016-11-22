package com.pineone.icbms.sda.sf.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;

public interface SfService { 
	
	// test수행(ciid여러개를 받아서 test)
	public List<Map<String, String>> getContext(RequestDTO requestDTO) throws Exception;

	// test수행(cmid를 받아서 test)
	public List<Map<String, String>> getContext(Map<String, Object> commandMap) throws Exception;

	// test수행(cmid및 args를 받아서 test)
	public List<Map<String, String>> getContext(Map<String, Object> commandMap, String args) throws Exception;
	
	// test수행2(cmid및 args를 받아서 test)
	public List<Map<String, String>> getContext2(String cmid, String args) throws Exception;
	
}
