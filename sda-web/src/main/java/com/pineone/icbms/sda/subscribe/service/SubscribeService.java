package com.pineone.icbms.sda.subscribe.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

public interface SubscribeService {

	// regist
	public void regist(String cmid) throws Exception;

	// subscribe
	public List<CiDTO> selectList() throws Exception;

	// callback
	public void callback(String bodyStr) throws Exception;

	/*
	public int insertCallback_(Map<String, Object> map) throws Exception;
	*/
}
