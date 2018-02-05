package com.pineone.icbms.sda.subscribe.dao;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackNoticeDTO;

/**
 * Callback정보를 SO에 전송용 DAO
 */
@Repository("callbackNoticeDAO")
public class CallbackNoticeDAO extends AbstractDAO {

	/**
	 * notce 시간 수정
	 * @param callbackNoticeDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateSoNoticeTime(CallbackNoticeDTO callbackNoticeDTO) throws Exception {
		return Integer.parseInt(update("callback.notice.updateSoNoticeTime", callbackNoticeDTO).toString());
	}

	/**
	 * 전송정보 등록
	 * @param callbackNoticeDTO
	 * @throws Exception
	 * @return int
	 */
	public int insert(CallbackNoticeDTO callbackNoticeDTO) throws Exception {
		return Integer.parseInt( insert("callback.notice.insert", callbackNoticeDTO).toString());
	}
}
