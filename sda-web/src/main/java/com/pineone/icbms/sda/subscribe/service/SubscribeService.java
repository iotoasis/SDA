package com.pineone.icbms.sda.subscribe.service;

import java.util.List;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

/**
 * subscribe용 service
 */
public interface SubscribeService {

	/**
	 * 등록
	 * @param cmid
	 * @throws Exception
	 * @return void
	 */
	public void regist(String cmid) throws Exception;

	/**
	 * 등록해제
	 * @param cmid
	 * @throws Exception
	 * @return void
	 */
	public void unregist(String cmid) throws Exception;

	/**
	 * subscribe 목록
	 * @return
	 * @throws Exception
	 * @return List<CiDTO>
	 */
	public List<CiDTO> selectList() throws Exception;

	/**
	 * callback 대응
	 * @param bodyStr
	 * @throws Exception
	 * @return void
	 */
	public void callback(String bodyStr) throws Exception;
}