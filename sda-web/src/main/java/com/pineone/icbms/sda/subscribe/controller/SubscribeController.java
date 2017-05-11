package com.pineone.icbms.sda.subscribe.controller;

import java.net.InetAddress;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sf.TripleService;
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
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
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
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("callback process  end================>");
		return entity;
	}

	// jena 데이타 초기화
	// http://localhost:8080/sda/subscribe/init-jena?p=xxxx
	@RequestMapping(value = "/init-jena", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> initJena(@RequestParam(value="p")  String args) {
		log.debug("requested parameter(p) for initJena ==>" + args);
	
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		TripleService tripleService = new TripleService();

		log.info("init jena data begin================>");
		
		try {
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}
			
			String save_path_file = "/home/pineone/svc/apps/sda/init-jena-data/icbms_basic_triple.ttl";
			//String save_path_file = "/home/pineone/svc/apps/sda/init-jena-data/icbms_basic_triple.rdf";
			// 지우기
			log.debug("init jena data delete begin================>");
			Utils.deleteTripleAll();
			log.debug("init jena data delete end================>");
			
			// 등록하기
			tripleService.sendTripleFile(save_path_file);
			
			// data mart서버 초기화하기
			initJena2(args);
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init jena data end================>");
		return entity;
	}
	
	// data mart쪽 서버의 데이타를 초기화한다.
	private ResponseEntity<ResponseMessage> initJena2(String args) throws Exception {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		//TripleService tripleService = new TripleService();

		log.info("init2 jena data begin================>");
		
		try {
			// data mart서버의 url호출 
			resultMsg = Utils.requestData(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dm.ip")+":"+Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dm.port")+"/sda/subscribe/init-jena?p="+args);
			
			//resultMsg.setCode(Utils.OK_CODE);
			//resultMsg.setMessage(Utils.OK_MSG);
			
			/*
			 * if (returnList.size() > 0) { // SO전송함
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
			 */
			
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init2 jena data end================>");
		return entity;
	}
	
	private void printClientIp__() {
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
