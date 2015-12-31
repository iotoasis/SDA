package com.pineone.icbms.sda.sf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sf.service.SfService;

@RestController
public class SfController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "sfService")
	private SfService sfService;


	// 여러개의 ci를 묶어서 test수행
	// http://localhost:8080/sda/ctx
	// {"cmid":"","ciid":"CI-1-1-011","name":"임박강의실테스트","conditions":[],"execution_type":"test","schedule":"","domain":"", "remarks":""}
	@RequestMapping(value = "/ctx", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<ResponseMessage> test(@RequestBody RequestDTO requestDTO) {
		log.debug("requested parameter for test ==>" + requestDTO.toString());

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String content;

		log.info("/ctx POST test start================>");
		try {

			List<Map<String, String>> returnMsg = sfService.test(requestDTO);

			content = gson.toJson(returnMsg);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(content);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx POST test end================>");
		return entity;
	}
	
	
	
	// cmid를 지정하여 test수행
	@RequestMapping(value = "/ctx/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> test(@PathVariable String cmid) {
		log.debug("requested parameter(cmid) for test ==>" + cmid);

		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String content;

		log.info("/ctx/{cmid} GET test start================>");
		try {

			commandMap.put("cmid",cmid);
			List<Map<String, String>> returnMsg = sfService.test(commandMap);

			content = gson.toJson(returnMsg);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(content);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx/{cmid} GET test end================>");
		return entity;
	}
	
	// cmid및 쿼리조건을 지정하여 test수행
	@RequestMapping(value = "/ctx/{cmid}/{args}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> test(@PathVariable String cmid, @PathVariable String args) {
		log.debug("requested parameter(cmid) for test ==>" + cmid);
		log.debug("requested parameter(args) for test ==>" + args);

		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String content;

		log.info("/ctx/{cmid}/{args} GET test start================>");
		try {

			commandMap.put("cmid",cmid);
			List<Map<String, String>> returnMsg = sfService.test(commandMap, args);

			content = gson.toJson(returnMsg);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(content);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx/{cmid}/{args} GET test end================>");
		return entity;
	}
}
