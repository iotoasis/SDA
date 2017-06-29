package com.pineone.icbms.sda.itf.cmi.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;

public interface CmiService { 

	
	public List<CmiDTO> selectList(Map<String, Object> commandMap)throws Exception;
//	public CmiDTO selectOne(Map<String, Object> commandMap) throws Exception;
	
	int insert(Map<String, Object> map) throws Exception;
		
	

}
