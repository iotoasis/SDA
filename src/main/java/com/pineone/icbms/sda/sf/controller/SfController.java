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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.dto.ResponseMessageOk;
import com.pineone.icbms.sda.comm.dto.ResponseMessageErr;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sf.service.SfService;

@RestController
public class SfController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "sfService")
	private SfService sfService;


	// 여러개의 ci를 묶어서 상황인지수행
	// http://localhost:8080/sda/ctx
	// {"cmid":"","ciid":"CI-1-1-011","name":"임박강의실테스트","conditions":[],"execution_type":"test","schedule":"","domain":"", "remarks":""}
	@RequestMapping(value = "/ctx", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getContext(@RequestBody RequestDTO requestDTO) {
		log.debug("requested parameter for getContext ==>" + requestDTO.toString());

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ctx POST getContext start================>");
		try {

			List<Map<String, String>> returnMsg = sfService.getContext(requestDTO);

			// 응답객체
			ResponseMessageOk ok = new ResponseMessageOk();
			ok.setContent(returnMsg);
			if(returnMsg.size() == 0) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			// 응답객체
			ResponseMessageErr str = new ResponseMessageErr();
			str.setContent(resultMsg.getMessage());

			entity = new ResponseEntity<Object>(str, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx POST getContext end================>");
		return entity;
	}
	
	// cmid및 쿼리조건을 지정하여 상황인지 수행
	@RequestMapping(value = "/ctx/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getContext(@PathVariable String cmid, @RequestParam(value="p")  String args) {
		log.debug("requested parameter(cmid) for getContext ==>" + cmid);
		log.debug("requested parameter(p) for getContext ==>" + args);

		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ctx/{cmid} GET getContext start================>");
		try {

			commandMap.put("cmid",cmid);
			List<Map<String, String>> returnMsg = sfService.getContext(commandMap, args);

			// 응답객체
			ResponseMessageOk ok = new ResponseMessageOk();
			ok.setContent(returnMsg);
			if(returnMsg.size() == 0) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			// 응답객체
			ResponseMessageErr str = new ResponseMessageErr();
			str.setContent(resultMsg.getMessage());

			entity = new ResponseEntity<Object>(str, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx/{cmid} GET getContext end================>");
		return entity;
	}
	
	
	// cmid및 쿼리조건을 지정하여 상황인지 수행
	@RequestMapping(value = "/ctx2/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getContext2(@PathVariable String cmid, @RequestParam(value="p")  String args) {
		log.debug("requested parameter(cmid) for getContext2 ==>" + cmid);
		log.debug("requested parameter(p) for getContext2 ==>" + args);

		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ctx2/{cmid} GET getContext start================>");
		try {

//			commandMap.put("cmid",cmid);
			List<Map<String, String>> returnMsg = sfService.getContext2(cmid, args);

			// 응답객체
			ResponseMessageOk ok = new ResponseMessageOk();
			ok.setContent(returnMsg);
			if(returnMsg.size() == 0) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			// 응답객체
			ResponseMessageErr str = new ResponseMessageErr();
			str.setContent(resultMsg.getMessage());

			entity = new ResponseEntity<Object>(str, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx2/{cmid} GET getContext end================>");
		return entity;
	}
}
