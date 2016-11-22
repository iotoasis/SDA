package com.pineone.icbms.sda.sch.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.sch.dto.SchDTO;

public interface SchService { 

	//sch
	List<SchDTO> selectList() throws Exception;
	
	List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception;
	SchDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	int updateLastWorkTime(Map<String, Object> map) throws Exception;
	
	// sch_hist
	int insertSchHist(Map<String, Object> map) throws Exception;
	int updateFinishTime(Map<String, Object> map) throws Exception;
}
