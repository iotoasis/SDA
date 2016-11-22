package com.pineone.icbms.sda.subscribe.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.subscribe.dto.CallbackDTO;
import com.pineone.icbms.sda.subscribe.dto.SubscribeDTO;

@Repository("subscribeDAO")
public class SubscribeDAO extends AbstractDAO{
	
	@SuppressWarnings("unchecked")
	public List<CiDTO> selectList() throws Exception{
		return selectList("subscribe.selectList");
	}

	@SuppressWarnings("unchecked")
	public List<CiDTO> selectList(Map<String, Object> commandMap) throws Exception{
		return selectList("subscribe.selectList", commandMap);
	}

	@SuppressWarnings("unchecked")
	public List<SubscribeDTO> selectListByUri(Map<String, Object> commandMap) throws Exception{
		return selectList("subscribe.selectListByUri", commandMap);
	}

	public int insert(SubscribeDTO subscribeDTO) throws Exception{
		return (int)insert("subscribe.insert", subscribeDTO);
	}	
	
	public int updateLastWorkTime(SubscribeDTO subscribeDTO) throws Exception{
		return (int)update("subscribe.updateLastWorkTime", subscribeDTO);
	}
	
	public int updateFinishTime(CallbackDTO callbackDTO) throws Exception{
		return (int)update("callback.updateFinishTime", callbackDTO);
	}
	
	public int deleteByCmid(Map<String, Object> commandMap) throws Exception{
		return (int)delete("subscribe.deleteByCmid", commandMap);
	}	


}
