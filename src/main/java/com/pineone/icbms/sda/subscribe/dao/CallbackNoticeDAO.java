package com.pineone.icbms.sda.subscribe.dao;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackNoticeDTO;

@Repository("callbackNoticeDAO")
public class CallbackNoticeDAO extends AbstractDAO {

	public int updateSoNoticeTime(CallbackNoticeDTO callbackNoticeDTO) throws Exception {
		return (int) update("callback.notice.updateSoNoticeTime", callbackNoticeDTO);
	}

	public int insert(CallbackNoticeDTO callbackNoticeDTO) throws Exception {
		return (int) insert("callback.notice.insert", callbackNoticeDTO);
	}
}
