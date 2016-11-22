package com.pineone.icbms.sda.subscribe.dao;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackNoticeDTO;

@Repository("callbackNoticeDAO")
public class CallbackNoticeDAO extends AbstractDAO {

	public int updateSoNoticeTime(CallbackNoticeDTO callbackNoticeDTO) throws Exception {
		return Integer.parseInt(update("callback.notice.updateSoNoticeTime", callbackNoticeDTO).toString());
	}

	public int insert(CallbackNoticeDTO callbackNoticeDTO) throws Exception {
		return Integer.parseInt( insert("callback.notice.insert", callbackNoticeDTO).toString());
	}
}
