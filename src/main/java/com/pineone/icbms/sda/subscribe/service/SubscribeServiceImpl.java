package com.pineone.icbms.sda.subscribe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.RemoteSIException;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.service.CmService;
import com.pineone.icbms.sda.kb.dto.OneM2MContainerDTO;
import com.pineone.icbms.sda.sf.service.SparqlService;
import com.pineone.icbms.sda.sf.service.TripleService;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MSubscribeUriMapper;
import com.pineone.icbms.sda.subscribe.dao.CallbackDAO;
import com.pineone.icbms.sda.subscribe.dao.CallbackNoticeDAO;
import com.pineone.icbms.sda.subscribe.dao.SubscribeDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackDTO;
import com.pineone.icbms.sda.subscribe.dto.CallbackNoticeDTO;
import com.pineone.icbms.sda.subscribe.dto.SubscribeDTO;

@Service("subscribeService")
public class SubscribeServiceImpl implements SubscribeService {
	private final Log log = LogFactory.getLog(this.getClass());

	private final String user = this.getClass().getName();
	
	@Resource(name = "subscribeDAO")
	private SubscribeDAO subscribeDAO;

	@Resource(name = "callbackDAO")
	private CallbackDAO callbackDAO;

	@Resource(name = "callbackNoticeDAO")
	private CallbackNoticeDAO callbackNoticeDAO;

	@Resource(name = "cmService")
	private CmService cmService;

	// subscribe 등록
	public void regist(String cmid) throws Exception {
		String notification_uri = Utils.getSdaProperty("com.pineone.icbms.sda.si.notification_uri");
		String subscription_uri = Utils.getSdaProperty("com.pineone.icbms.sda.si.subscription_uri");
		Map<String, Object> commandMap;

		// 확인
		commandMap = new HashMap<String, Object>();
		commandMap.put("cmid", cmid);
		List<CiDTO> list = subscribeDAO.selectList(commandMap);

		// 데이타가 없으면 오류발생시킴
		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		Gson gson = new Gson();
		CiDTO[] ciDTO = new CiDTO[list.size()];

		// subscribe테이블에 중복된 값이 있을 수 있으므로 삭제함(cmid를 이용함)
		commandMap = new HashMap<String, Object>();
		commandMap.put("cmid", cmid);
		subscribeDAO.deleteByCmid(commandMap);
		
		// SI에 등록준비(list의 값이 CiDTO테이블과 같으므로 CiDTO에 담는다)
		for (int i = 0; i < list.size(); i++) {
			ciDTO[i] = list.get(i);
		}

		// 처리시작
		for (int i = 0; i < ciDTO.length; i++) {
			// log.debug("val["+i+"]"+ciDTO[i].toString());

			// condition을 JENA에 요청하여 uri목록을 얻음
			List<String> uriList = new ArrayList<String>();
			OneM2MSubscribeUriMapper mapper = new OneM2MSubscribeUriMapper(ciDTO[i].getDomain(),
					ciDTO[i].getConditions());

			uriList = mapper.getSubscribeUri();
			log.debug("uriList to regist ==> \n" + uriList);

			for (int k = 0; k < uriList.size(); k++) {
				// tnsda_subscribe에 등록 시작
				SubscribeDTO subscribeDTO = new SubscribeDTO();
				String subscribe_time = Utils.dateFormat.format(new Date());

				subscribeDTO.setCmid(cmid);
				subscribeDTO.setCiid(ciDTO[i].getCiid());
				subscribeDTO.setUri(uriList.get(k));
				subscribeDTO.setNotification_uri(notification_uri);
				subscribeDTO.setSubscribe_time(subscribe_time);
				subscribeDTO.setCuser(user);
				subscribeDTO.setUuser(user);

				subscribeDAO.insert(subscribeDTO);
				// tnsda_subscribe에 등록 끝

				// SI에 등록 시작
				Map<String, String> map = new HashMap<String, String>();
				map.put("_uri", uriList.get(k));
				map.put("_notificationUri", notification_uri);

				String jsonMsg = gson.toJson(map);
				log.debug("Request message for subscribing  =>  " + jsonMsg);

				ResponseMessage responseMessage = Utils.requestData(subscription_uri, jsonMsg); // POST
				// ResponseMessage responseMessage =
				// Utils.getMessageFromResponse(response);

				log.debug("result of responseMessage : " + responseMessage.toString());
				if (responseMessage.getCode() != 200) {
					throw new RemoteSIException(HttpStatus.valueOf(responseMessage.getCode()),
							responseMessage.getMessage());
				}
				// SI에 등록 끝
			}
		}
		// 처리끝
	}

	// callback 되었을때 호출되는 메서드
	public void callback(String bodyStr) throws Exception {
		String tripleStr = "";
		Gson gson = new Gson();
		StringBuffer sb = new StringBuffer();
		String save_path = Utils.getSdaProperty("com.pineone.icbms.sda.triple.save_path");
		save_path = Utils.makeSavePath(save_path);
		String callback_result_uri = Utils.getSdaProperty("com.pineone.icbms.sda.so.callback_result_uri");
		String riot_mode = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");
		String triple_path_file = "";
		String callback_uri = "";
		String triple_check_result = "";
		String user = this.getClass().getName();
		int callback_seq = 0;
		TripleService tripleService = new TripleService();
		SparqlService sparqlService = new SparqlService();
		Map<String, Object> commandMap;

		log.info("callback process begin================>");
		try {
			// 메세지 확인
			log.debug("callback msg  in subscribeserviceimpl : " + bodyStr);
			String start_time = Utils.dateFormat.format(new Date());

			// tnsda_callback테이블에 등록 시작
			CallbackDTO callbackDTO = new CallbackDTO();
			String callback_time = Utils.dateFormat.format(new Date());

			// uri값을 확인하기 위해서 특정 객체에 매핑해봄
			OneM2MContainerDTO containerDTO = gson.fromJson(bodyStr, OneM2MContainerDTO.class);
			callback_uri = containerDTO.get_uri();
			if (callback_uri == null || callback_uri.equals(""))
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not Valid Callback Uri");

			// callback_ur(예, ../Data/AAA_1234)에서 .../Data까지만 취함
			callback_uri = callback_uri.substring(0, callback_uri.lastIndexOf("/"));
			callbackDTO.setCallback_uri(callback_uri);
			callbackDTO.setCallback_time(callback_time);
			callbackDTO.setCallback_msg(bodyStr);
			callbackDTO.setCuser(user);
			callbackDTO.setUuser(user);

synchronized(this) {			
			callbackDAO.insert(callbackDTO);
			// tnsda_callback테이블에 등록 끝

			// 가끔 잘못된 이전 값을 리턴하는 버그가 있음
			//HashMap<String, Object>	lastVal = callbackDAO.selectInsertId();
			HashMap<String, Object>	lastVal = callbackDAO.selectMaxSeq();
			
			if(lastVal == null || lastVal.get("callback_seq").equals("")) {
				throw new UserDefinedException(HttpStatus.NOT_FOUND);
			}
			
			log.debug("last value of callback_uri : " + lastVal);
			callback_seq = Integer.parseInt(lastVal.get("callback_seq").toString());
			// tnsda_callback에서 uri별 callback_seq의 마지막 값을 가져옴 끝
		}			

			// callback메세지를 triple로 변환
			tripleStr = tripleService.getTriple(bodyStr);
			sb.append(tripleStr);

			// 스트링 버퍼에 있는 값을 파일에 기록한다.
			String file_name = "Callback_"+String.format("%010d", callback_seq)+"_WRK" + start_time + "_BT" + start_time;
			triple_path_file = save_path + "/" + file_name + ".nt";
			String triple_check_result_file = "";

			// 결과값이 있을때만 triple파일을 만듬
			if (sb.length() > 0) {
				tripleService.makeTripleFile(triple_path_file, sb);
				// triple파일 체크
				if (!riot_mode.equals("--skip")) {
					String[] check_result = tripleService.checkTripleFile(triple_path_file, file_name);
					// 점검결과를 파일로 저장한다.(체크결과 오류가 있는 경우만 파일로 만듬)
					if (!check_result[1].trim().equals("")) {
						triple_check_result_file = file_name + ".bad";
						tripleService.makeResultFile(triple_check_result_file, check_result);

						// triple파일 체크결과값
						if (check_result[1].length() > 0) {
							triple_check_result = check_result[1];
						} else {
							triple_check_result = Utils.Valid;
						}
					}
				}
				// triple파일 전송
				tripleService.sendTripleFile(triple_path_file);
			}

			// callback_uri를 기준으로 subscribe테이블과 context_info테이블에서 query할
			// cmid단위의 distinct한 sparql목록을 가져온다.
			commandMap = new HashMap<String, Object>();
			commandMap.put("uri", callback_uri);
			List<SubscribeDTO> subscribeList = subscribeDAO.selectListByUri(commandMap);

			// sparql을 이용하여 JENA에 쿼리를 수행후 결과값을 subscribe_notice테이블에 insert한다. 시작
			SubscribeDTO subscribeDTO;
			List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

			for (int i = 0; i < subscribeList.size(); i++) {
				subscribeDTO = (SubscribeDTO) subscribeList.get(i);

				// cmid를 기준으로 ciid에 설정된 sparql을 가져온다.
				List<CmCiDTO> list = new ArrayList<CmCiDTO>();

				commandMap = new HashMap<String, Object>();
				commandMap.put("cmid", subscribeDTO.getCmid());

				// CmService cmService = getContext().getBean(CmService.class);
				list = cmService.selectCmCiList(commandMap);

				// 상황인지행위 시작(차후 추가)
				// 상황인지행위 끝

				// 실행할 sparql을 추출하며 인수 개수(cm과 ci의 개수 확인)가 동일한지 확인하고 다르면 exception을 발생시킨다.
				// 여러 sparql을 실행후 각 결과값에서 공통적으로 존재하는 값을 추출함
				List<String> sparqlList = new ArrayList<String>();
				for (CmCiDTO cmCiDTO : list) {
					if(cmCiDTO.getCm_arg_cnt() == 0 && cmCiDTO.getCi_arg_cnt() == 0) {
						// pass
					} else {
						if(cmCiDTO.getCm_arg_cnt() != cmCiDTO.getCi_arg_cnt()) {
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not valid argument count ! ");
						}
					}
					sparqlList.add(cmCiDTO.getSparql());
				}

				returnList = sparqlService.runSparqlUniqueResult(sparqlList);

				// callback_notice테이블에 insert한다. 시작
				CallbackNoticeDTO callbackNoticeDTO = new CallbackNoticeDTO();
				callbackNoticeDTO.setCallback_seq(callback_seq);
				callbackNoticeDTO.setCmid(subscribeDTO.getCmid());
				callbackNoticeDTO.setCuser(user);
				callbackNoticeDTO.setUuser(user);

				callbackNoticeDAO.insert(callbackNoticeDTO);
				// sparql을 이용하여 JENA에 쿼리를 수행후 결과값을 subscribe_notice테이블에
				// insert한다. 끝

				String so_notice_time = "";
				String jsonMsg = "";
				ResponseMessage responseMessage = new ResponseMessage();

				if (returnList.size() > 0) {
					// JENA에 쿼리 결과 값을 SO에 전송시작
					so_notice_time = Utils.dateFormat.format(new Date());
					Map<String, Object> map = new HashMap<String, Object>();

					map.put("cmd", Utils.CALLBACK_EMERGENCY);
					map.put("contextId", subscribeDTO.getCmid());
					map.put("time", so_notice_time);
					map.put("domains", returnList);

					jsonMsg = gson.toJson(map);

					log.debug("Request message[" + i + "] of emergency for sending to SO  =>  " + jsonMsg);

					responseMessage = Utils.requestData(callback_result_uri, jsonMsg); // POST

					log.debug("responseMessage[" + i + "] of emergency from SO : " + responseMessage.toString());
					// JENA에 쿼리 결과 값을 SO에 전송끝
				} else {
					jsonMsg = Utils.NotSendToSo;
				}

				// SO전송완료 시간및 SO return값을 callback테이블의 so_notice_time,
				// so_notice_msg, work_result, triple_file_name,
				// triple_check_result에 update함, 시작
				callbackNoticeDTO = new CallbackNoticeDTO();
				callbackNoticeDTO.setCallback_seq(callback_seq);
				callbackNoticeDTO.setSo_notice_time(so_notice_time);
				callbackNoticeDTO.setSo_notice_msg(jsonMsg);
				callbackNoticeDTO.setTriple_file_name(triple_path_file);
				callbackNoticeDTO.setTriple_check_result(triple_check_result);

				if (returnList.size() > 0) { // SO전송함
					if (responseMessage.getCode() == 200) {
						if (triple_check_result_file.equals("")) {
							triple_check_result_file = Utils.None;
						}
						callbackNoticeDTO.setWork_result("triple_check_result_file : " + triple_check_result_file);
					} else {
						callbackNoticeDTO
								.setWork_result(responseMessage.getCode() + " " + responseMessage.getMessage());
					}
				} else { // SO전송안함
					if (triple_check_result_file.equals("")) {
						triple_check_result_file = Utils.None;
					}
					callbackNoticeDTO.setWork_result("triple_check_result_file : " + triple_check_result_file);
				}

				callbackNoticeDTO.setUuser(user);

				callbackNoticeDAO.updateSoNoticeTime(callbackNoticeDTO);
				// SO전송완료 시간및 SO return값을 callback테이블의 so_notice_time,
				// so_notice_msg, work_result, triple_file_name,
				// triple_check_result에 update함, 끝
			} // end of for
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.info("callback process  end================>");
	}

	// 목록조회
	@Override
	public List<CiDTO> selectList() throws Exception {
		return subscribeDAO.selectList();
	}

	// callback
	/*
	public int insertCallback_(Map<String, Object> map) throws Exception {
		int cnt = 0;
		@SuppressWarnings("unchecked")
		List<CallbackDTO> list = (ArrayList<CallbackDTO>) map.get("list");
		for (int i = 0; i < list.size(); i++) {
			CallbackDTO callbackDTO = (CallbackDTO) list.get(i);
			cnt = callbackDAO.insert(callbackDTO);
		}
		return cnt;
	}
	*/
}
