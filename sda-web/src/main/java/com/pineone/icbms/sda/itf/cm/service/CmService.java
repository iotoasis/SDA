package com.pineone.icbms.sda.itf.cm.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;

public interface CmService { 

	public List<CmCiDTO> selectList(Map<String, Object> commandMap) throws Exception;
	public CmCiDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	// SO 스케줄에서 사용됨
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception;

}
