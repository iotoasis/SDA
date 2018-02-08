package com.pineone.icbms.sda.sf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.dto.ResponseMessageOk;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sf.service.SfService;

/**
 * 시멘틱프레임용 Controller
 */
@RestController
public class SfController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "sfService")
	private SfService sfService;

	/**
	 * ctx용 API 
	 * @param cmid
	 * @param args
	 * @return ResponseEntity<Object>
	 */
	@RequestMapping(value = "/ctx/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getContext(@PathVariable String cmid, @RequestParam(value="p")  String args){
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		
		List<Map<String, String>> returnMsg  = new ArrayList<Map<String, String>>();
				
		log.info("/ctx/{cmid} GET getContext start================>");
		
		ResponseMessageOk ok = new ResponseMessageOk();
		ok.setCmd(Utils.CMD);
		ok.setContextId(cmid);
		ok.setTime(Utils.dateFormat.format(new Date()));
		
		try {
			commandMap.put("cmid",cmid);
			returnMsg = sfService.getContext(commandMap, args);
			ok.setContents(returnMsg);
			
			if(returnMsg.size() == 0) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			ResponseMessage resultMsg = Utils.makeResponseBody(e);
			log.debug("ExceptionCause : "+resultMsg.getMessage());

			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			List<Map<String, String>> msg = new ArrayList<Map<String, String>>();
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("err_msg", resultMsg.getMessage() );

			ok.setContents(msg);

			entity = new ResponseEntity<Object>(msg, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx/{cmid} GET getContext end================>");
		return entity;
	}
	
	/**
	 * deviceinfo용 API
	 * @param cmid
	 * @param args
	 * @return ResponseEntity<Object>
	 */
	@RequestMapping(value = "/deviceinfo/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getDeviceInfo(@PathVariable String cmid, @RequestParam(value="p")  String args){
		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/deviceinfo/{cmid} GET getDeviceInfo start================>");
		List<Map<String, String>> returnMsg = new ArrayList<Map<String, String>>();
		
		ResponseMessageOk ok = new ResponseMessageOk();
		ok.setCmd(Utils.CMD);
		ok.setContextId(cmid);
		ok.setTime(Utils.dateFormat.format(new Date()));
		
		try {
			if(args.equals("")) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not Valid Argument.");
			}

			String rtnStr = Utils.getDeviceInfo("<"+Utils.PREF+args+">");
			
			if( ! rtnStr.contains("rdf:resource")) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}

			Map<String, String > msgMap = new HashMap<String, String>();
			
			rtnStr = replaceBlankNodeId(rtnStr);
			msgMap.put("device_information", rtnStr);
			returnMsg.add(msgMap);
			
			ok.setContents(returnMsg);
		} catch (Exception e) {
			ResponseMessage resultMsg = Utils.makeResponseBody(e);
			log.debug("ExceptionCause : "+resultMsg.getMessage());

			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			List<Map<String, String>> msg = new ArrayList<Map<String, String>>();
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("err_msg", resultMsg.getMessage() );

			ok.setContents(msg);

			entity = new ResponseEntity<Object>(msg, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/deviceinfo/{cmid} GET getDeviceInfo end================>");
		return entity;
	}
	
	
	/**
	 * resourceinfo용 API
	 * @param cmid
	 * @param args
	 * @return ResponseEntity<Object>
	 */
	@RequestMapping(value = "/resourceinfo/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getResourceInfo(@PathVariable String cmid, @RequestParam(value="p")  String args){
		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/resourceinfo/{cmid} GET getResourceInfo start================>");

		List<Map<String, String>> returnMsg = new ArrayList<Map<String, String>>();
		
		ResponseMessageOk ok = new ResponseMessageOk();
		ok.setCmd(Utils.CMD);
		ok.setContextId(cmid);
		ok.setTime(Utils.dateFormat.format(new Date()));
		
		try {
			if(args.equals("")) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not Valid Argument.");
			}

			String rtnStr = Utils.getDeviceInfo("<"+args+">");
			
			if( ! rtnStr.contains("rdf:resource")) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}

			Map<String, String > msgMap = new HashMap<String, String>();
			
			rtnStr = replaceBlankNodeId(rtnStr);
			msgMap.put("resource_information", rtnStr);
			returnMsg.add(msgMap);
			
			ok.setContents(returnMsg);
		} catch (Exception e) {
			ResponseMessage resultMsg = Utils.makeResponseBody(e);
			log.debug("ExceptionCause : "+resultMsg.getMessage());

			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			List<Map<String, String>> msg = new ArrayList<Map<String, String>>();
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("err_msg", resultMsg.getMessage() );

			ok.setContents(msg);

			entity = new ResponseEntity<Object>(msg, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/resourceinfo/{cmid} GET getResourceInfo end================>");
		return entity;
	}

	/**
	 * blank node 치환
	 * @param str
	 * @return String
	 */
	private String replaceBlankNodeId(String str) {
		UUID uid;
		String exChanged = str;
		String matchStr;
		for(int i = 0; i < 10000; i ++) {
			matchStr = "_:b"+i;
			
			try {
				if(exChanged.contains(matchStr)) {
					uid = UUID.randomUUID();
					log.debug("uid.toString() : "+uid.toString());
					exChanged = exChanged.replaceAll(matchStr, "_:"+uid.toString());
				} else {
					break;
				}
			} catch (Exception e) {
				log.debug("Exception : "+e.getMessage());
			}
		}
		return exChanged;
	}
}