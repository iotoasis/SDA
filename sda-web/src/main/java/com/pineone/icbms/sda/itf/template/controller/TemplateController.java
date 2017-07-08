package com.pineone.icbms.sda.itf.template.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;
import com.pineone.icbms.sda.itf.template.service.TemplateService;

@RestController
@RequestMapping(value = "/itf")
public class TemplateController {
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "templateService")
	private TemplateService templateService;

	// http://localhost:8080/sda/itf/template/all
	@RequestMapping(value = "/template/all", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectList(Map<String, Object> commandMap) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;
		List<TemplateDTO> list = new ArrayList<TemplateDTO>();
		
		log.info("/template/all GET start================>");
		
		try {	
			list = templateService.selectList(commandMap);

			contents = gson.toJson(list);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(contents);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/template/all GET end================>");
		return entity;
	}
	
	// http://localhost:8080/sda/itf/template/T1000
	@RequestMapping(value = "/template/{tmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectOne(@PathVariable String tmid) {
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		log.info("/template/{tmid} GET start================>");
		try {
			TemplateDTO templateDTO = new TemplateDTO();
			commandMap.put("tmid", tmid);

			templateDTO = templateService.selectOne(commandMap);

			contents = gson.toJson(templateDTO);

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(contents);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/template/{tmid} GET end================>");
		return entity;
	}


	

}
