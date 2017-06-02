package com.pineone.icbms.sda.sf.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.sf.dto.AwareHistDTO;

public interface AwareService { 
/*
	//sch
	List<SchDTO> selectList() throws Exception;
	
	List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception;
	SchDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	int updateLastWorkTime(Map<String, Object> map) throws Exception;
	*/
	
	// aware_hist
//	int insertSchHist(Map<String, Object> map) throws Exception;
	int insertAwareHist(Map<String, List<AwareHistDTO>> map) throws Exception;
	int updateFinishTime(Map<String, Object> map) throws Exception;
}
