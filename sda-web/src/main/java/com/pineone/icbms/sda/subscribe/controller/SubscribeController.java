package com.pineone.icbms.sda.subscribe.controller;

import java.io.File;
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
import com.pineone.icbms.sda.sf.sd.UpdateSemanticDescriptor;
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

	// 지정된 cmid를 기준으로 subscribe 등록해제
	// http://localhost:8080/sda/subscribe/regist/CM-1-1-001
	@RequestMapping(value = "/unregist/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> unregist(@PathVariable String cmid) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("subscribe unregist begin================>");
		
		try {
			// 등록해제(unsubscription)
			subscribeService.unregist(cmid);

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
		log.info("subscribe unregist end================>");
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

	// jena 데이타 초기화(DW를 초기화)
	// http://localhost:8080/sda/subscribe/init-jena?p=xxxx
	@RequestMapping(value = "/init-jena", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> initJena(@RequestParam(value="p")  String args) {
		log.debug("requested parameter(p) for initJena ==>" + args);
	
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> resultMsgFromInit2 = null;
		
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		TripleService tripleService = new TripleService();

		log.info("init jena data begin================>");
		
		try {
			// dm인지 dw인지 확인(init-jena는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}

			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}
			
			String save_path_file = "/home/pineone/svc/apps/sda/init-jena-data/icbms_basic_triple.ttl";
			
			// triple데이타 지우기
			log.debug("init jena data delete begin================>");
			Utils.deleteDWTripleAll();
			log.debug("init jena data delete end================>");
			
			// 서버 데이타 초기화(DW)
			log.debug("init jena sendTripleFile begin================>");
			tripleService.sendTripleFileToDW(save_path_file);
			log.debug("init jena sendTripleFile end================>");
			

			// 서버 데이타 초기화(Halyard)
			log.debug("init jena sendTripleFileToHalyard begin================>");
			try {
				tripleService.sendTripleFileToHalyard(new File(save_path_file));
			} catch (Exception e) {
				log.debug("sendTripleFileToHalyard exceptionin SubscribeController.java : "+e.getLocalizedMessage());
				log.debug("save_path_file : "+save_path_file);
			}
			log.debug("init jena sendTripleFileToHalyard end================>");


		
			/* */
			//DM서버 초기화하기
			resultMsgFromInit2 = initJena2(args);
			
			if(resultMsgFromInit2.getBody().getCode() == 200) {
				resultMsg.setCode(Utils.OK_CODE);
				resultMsg.setMessage(Utils.OK_MSG);
				entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
			} else {
				resultMsg.setCode(resultMsgFromInit2.getBody().getCode());
				resultMsg.setMessage(resultMsgFromInit2.getBody().getMessage());

				responseHeaders.add("ExceptionCause", resultMsg.getMessage());
				responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
				entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
						HttpStatus.valueOf(resultMsg.getCode()));
			}
			/* */
			
			/*
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
			*/
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception 1 : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init jena data end================>");
		return entity;
	}
	
	// jena 데이타 초기화(DM서버쪽 초기화에 사용됨)
	@RequestMapping(value = "/init-jena2", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> runInitJenaDM(@RequestParam(value="p")  String args) {
		log.debug("requested parameter(p) for initJena ==>" + args);
	
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		TripleService tripleService = new TripleService();

		log.info("init jena2 data begin================>");
		
		try {
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}
			
			String save_path_file = "/home/pineone/svc/apps/sda/init-jena-data/icbms_basic_triple.ttl";

			// triple데이타 모두 지우기
			log.debug("init jena2 data delete begin================>");
			Utils.deleteDMTripleAll();
			log.debug("init jena2 data delete end================>");
			
			// 서버 데이타 초기화
			log.debug("init jena2 sendTripleFile begin================>");
			tripleService.sendTripleFileToDM(save_path_file);
			log.debug("init jena2 sendTripleFile end================>");

			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception 2: "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init jena2 data end================>");
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
			resultMsg = Utils.requestGet("http://"+Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dm.ip")+":"+Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dm.port")+"/sda/subscribe/init-jena2?p="+args);
			log.debug("InitJena2 result  : "+resultMsg.getMessage());
			
			if (resultMsg.getCode() == 200) {
				resultMsg.setCode(Utils.OK_CODE);
				resultMsg.setMessage(Utils.OK_MSG);
				entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
			} else {
				//resultMsg = resultMsg.getMessage();
				responseHeaders.add("ExceptionCause", resultMsg.getMessage());
				responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
				entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
						HttpStatus.valueOf(resultMsg.getCode()));
			}
			
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			
			log.debug("Exception 3: "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init2 jena data end================>");
		return entity;
	}

	// http://localhost:8080/sda/subscribe/update-jena?p=날짜8자리,db명
	@RequestMapping(value = "update-jena", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> updateJena(@RequestParam(value="p")  String args) {
		log.debug("requested parameter(p) for update Jena ==>" + args);
		
		
		ResponseMessage resultMsg = new ResponseMessage();
		
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		
		String[] argArr;

		log.info("update jena data begin================>");
		
		try {
			argArr = args.split(",");

			if(argArr.length != 2){ 
				log.debug("p(" + args + ") count mismatched");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}
			
			if( ! Utils.checkPass(argArr[0])) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}
			
			// Make Semantic Descriptor File - device
			UpdateSemanticDescriptor updateSemanticDescriptor = new UpdateSemanticDescriptor();
			updateSemanticDescriptor.makeUpdateJena(argArr[1]);
			

			//tripleService.sendTripleFileToDW("filepath");
			//tripleService.sendTripleFileToDM("filepath");
			// 리턴값 확인 정상 일때 아래 resultMsg
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents("");
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
			
		
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception 1 : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("update jena data end================>");
		return entity;
	}
	
	
}
