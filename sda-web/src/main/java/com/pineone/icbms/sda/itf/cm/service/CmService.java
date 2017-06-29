package com.pineone.icbms.sda.itf.cm.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

public interface CmService { 

	public List<CmCiDTO> selectCmCmiCiList(Map<String, Object> commandMap) throws Exception;
	public CmCiDTO selectCmCmiCiOne(Map<String, Object> commandMap) throws Exception;
	
	public List<CmDTO> selectList(Map<String, Object> commandMap)throws Exception;
	public CmDTO selectOne(Map<String, Object> commandMap) throws Exception;
		
	
	public int delete(Map<String, Object> commandMap) throws Exception;
	int update(String ciid) throws Exception;
	int checkId(String cmid) throws Exception;
	
	int insert(Map<String, Object> map) throws Exception;

	
	// SO 스케줄에서 사용됨
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception;

	

}
