package com.pineone.icbms.sda.sf.controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.dto.ResponseMessageOk;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sf.service.SfService;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
public class SfController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "sfService")
	private SfService sfService;

/*
	// http://localhost:8080/sda/ctx
	// {"cmid":"","ciid":"CI-1-1-011","name":"이름","conditions":[],"execution_type":"test","schedule":"","domain":"", "remarks":""}
	@RequestMapping(value = "/ctx", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getContext(@RequestBody RequestDTO requestDTO) {
		log.debug("requested parameter for getContext ==>" + requestDTO.toString());

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ctx POST getContext start================>");
		try {

			List<Map<String, String>> returnMsg = sfService.getContext(requestDTO);

			ResponseMessageOk ok = new ResponseMessageOk();
			ok.setContents(returnMsg);
			if(returnMsg.size() == 0) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			ResponseMessageErr str = new ResponseMessageErr();
			str.setContents(resultMsg.getMessage());

			entity = new ResponseEntity<Object>(str, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx POST getContext end================>");
		return entity;
	}
*/
	
	
	//     http://sda1:20080/sda/ctx/cm-announcement-on/?p=, 혹은 http://sda1:20080/sda/ctx/cm-announcement-on?p= 
	@RequestMapping(value = "/ctx/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getContext(@PathVariable String cmid, @RequestParam(value="p")  String args){
		log.debug("requested parameter(cmid) for getContext ==>" + cmid);
		log.debug("requested parameter(p) for getContext ==>" + args);

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
	
	
	// http://sda1:20080/sda/deviceinfo/deviceinfo?p=ONSB_BleScanner01_001
	@RequestMapping(value = "/deviceinfo/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getDeviceInfo(@PathVariable String cmid, @RequestParam(value="p")  String args){
		log.debug("requested parameter(cmid) for getDeviceInfo ==>" + cmid);
		log.debug("requested parameter(p) for getDeviceInfo ==>" + args);

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
			
			// BlankNode Id 移섑솚
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
	
	
	// http://sda1:20080/sda/resourceinfo/resourceinfo?p=http://www.iotoasis.org/herit-in/herit-cse/ONSB_BleScanner01_001
	@RequestMapping(value = "/resourceinfo/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getResourceInfo(@PathVariable String cmid, @RequestParam(value="p")  String args){
		log.debug("requested parameter(cmid) for getResourceInfo ==>" + cmid);
		log.debug("requested parameter(p) for getResourceInfo ==>" + args);

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
			
			// BlankNode Id 변환
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
	
/*	
	@RequestMapping(value = "/ctx2/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getContext2(@PathVariable String cmid, @RequestParam(value="p")  String args) {
		log.debug("requested parameter(cmid) for getContext2 ==>" + cmid);
		log.debug("requested parameter(p) for getContext2 ==>" + args);

		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ctx2/{cmid} GET getContext2 start================>");
		try {

//			commandMap.put("cmid",cmid);
			List<Map<String, String>> returnMsg = sfService.getContext2(cmid, args);

			ResponseMessageOk ok = new ResponseMessageOk();
			ok.setContents(returnMsg);
			if(returnMsg.size() == 0) {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
			} else {
				entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			
			ResponseMessageErr str = new ResponseMessageErr();
			str.setContents(resultMsg.getMessage());

			entity = new ResponseEntity<Object>(str, responseHeaders,	HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/ctx2/{cmid} GET getContext2 end================>");
		return entity;
	}
*/
	
	
	// http://sda1:20080/sda/ctx3/info?p=aaa
	/*
	@RequestMapping(value = "/ctx3/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> getContext3(@PathVariable String cmid, @RequestParam(value="p")  String args){
		log.debug("requested parameter(cmid) for getContext3 ==>" + cmid);
		log.debug("requested parameter(p) for getContext3 ==>" + args);
		
		Gson gson = new Gson();
		
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseEntity<Object> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ctx3/{cmid} GET getContext3 start================>");
		printClientIp();
		
		ResponseMessageOk ok = new ResponseMessageOk();
		ok.setCmd(Utils.CMD_TEST);
		ok.setContextId(cmid);
		ok.setTime(Utils.dateFormat.format(new Date()));

		List<Map<String, String>> returnMsg;
		
		try {
				commandMap.put("cmid",cmid);
				returnMsg = sfService.getContext3(cmid, args);
				ok.setContents(returnMsg);
				
				if(returnMsg.size() == 0) {
					entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.NOT_FOUND);
				} else {
					entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.OK);
				}

				String jsonMsg = gson.toJson(ok);

				log.debug("Request message of Schedule for sending to SO =>  " + jsonMsg);
				ResponseMessage responseMessage = Utils.requestData(Utils.getSdaProperty("com.pineone.icbms.sda.so.callback_result_uri"), jsonMsg); // POST
				log.debug("responseMessage of Schedule from SO => " + responseMessage.toString());
				
				// SO에 오류 통보
				if(responseMessage.getCode() != 200) {
					List<Map<String, String>> msg = new ArrayList<Map<String, String>>();
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("err_msg", responseMessage.toString());
					ok.setContents(msg);
					
					entity = new ResponseEntity<Object>(ok, responseHeaders, HttpStatus.valueOf(responseMessage.getCode()));					
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
		log.info("/ctx3/{cmid} GET getContext3 end================>");
		return entity;
	}	
	*/
	
	private String replaceBlankNodeId(String str) {
		UUID uid;
		String exChanged = str;
		String matchStr;
		for(int i = 0; i < 10000; i ++) {
			matchStr = "_:b"+i;
			log.debug("matchStr :"+matchStr);
			
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
	
	private void printClientIp_bak() {
		// 호출한 쪽의  ip를 찍어본다.
		try { 
			InetAddress Address = InetAddress.getLocalHost();
			System.out.println("로컬 컴퓨터의 이름 : "+Address.getHostName());
			System.out.println("로컬 컴퓨터의 IP 주소 : "+Address.getHostAddress());
			
			HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	        String ip = req.getHeader("X-FORWARDED-FOR");
	        if (ip == null)
	            ip = req.getRemoteAddr();
	         
	        System.out.println("client ip : "+ ip);
		} catch (Exception e) {
			log.debug("/ctx/ call exception : " + e.getMessage());
		}
	}
}