package com.pineone.icbms.sda.subscribe.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.sf.service.TripleService;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.subscribe.service.SubscribeService;

@RestController
@RequestMapping(value = "/subscribe")
public class SubscribeController {
	private final  Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "subscribeService")
	private SubscribeService subscribeService;

	// 지정된 cmid를 기준으로 subscribe 등록
	// http://localhost:8080/sda/subscribe/regist/CM-1-1-001
	@RequestMapping(value = "/regist/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> regist(@PathVariable String cmid) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("subscribe regist begin================>");
		try {
			// 등록(subscription)
			subscribeService.regist(cmid);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("subscribe regist end================>");
		return entity;
	}

	// callback 되었을때 호출되는 메서드
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<ResponseMessage> callback(@RequestBody String requestBody) {
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		ResponseMessage resultMsg = new ResponseMessage();

		log.info("callback process begin================>");
		try {
			// callback처리
			subscribeService.callback(requestBody);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("callback process  end================>");
		return entity;
	}

	// jena 데이타 초기화
	// http://localhost:8080/sda/subscribe/init-jena
	@RequestMapping(value = "/init-jena", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> initJena() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		TripleService tripleService = new TripleService();

		log.info("init jena data begin================>");
		try {
			
			String save_path_file = "/home/pineone/svc/apps/sda/init-jena-data/icbms_basic_triple.ttl";
			// 지우기
			log.debug("init jena data delete begin================>");
			Utils.deleteTripleAll();
			log.debug("init jena data delete end================>");
			
			// 등록하기
			tripleService.sendTripleFile(save_path_file);
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init jena data end================>");
		return entity;
	}
}
