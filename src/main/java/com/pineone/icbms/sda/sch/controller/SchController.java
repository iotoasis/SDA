package com.pineone.icbms.sda.sch.controller;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.exceptions.PersistenceException;
import org.quartz.ObjectAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.service.SchedulerMainService;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.service.SchService;

@RestController
@RequestMapping(value = "/sch")
public class SchController {
	private final Log log = LogFactory.getLog(this.getClass());

	// 스케줄러
	private SchedulerMainService thm = null;

	@Resource(name = "schService")
	private SchService schService;

	// 스케쥴러 기동
	// http://localhost:8080/sda/sch/init
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> inittSch() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("init sch begin================>");
		try {
			// init
			thm = new SchedulerMainService();
			thm.JobInit();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (ObjectAlreadyExistsException e) {
			msg = "scheduler is already initiated...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("init sch end================>");
		return entity;
	}

	// 스케쥴러 종료
	// http://localhost:8080/sda/sch/shutdown
	@RequestMapping(value = "/shutdown", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> shutdownSch() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("shutdown sch begin================>");
		try {
			checkScheduerMainService();
			thm.shutdown();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = "scheduler is not initiated....";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("shutdown sch end ================>");
		return entity;
	}

	// pause(pause후 resume을 실행하면 미실행된 부분은 수행하지 않음)
	// http://localhost:8080/sda/sch/pause
	@RequestMapping(value = "/pause", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> pauseSch() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("pause sch begin================>");
		try {
			checkScheduerMainService();
			thm.pauseSch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = "scheduler is not initiated....";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("pause sch end ================>");
		return entity;

	}

	// resume(pause후 resume을 실행하면 미실행된 부분은 수행하지 않음)
	// http://localhost:8080/sda/sch/resume
	@RequestMapping(value = "/resume", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> resumeSch() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("resume sch begin================>");
		try {
			checkScheduerMainService();
			thm.resumeSch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = "scheduler is not initiated....";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("resume sch end ================>");
		return entity;

	}

	// standby(standby후 start를 실행하면 미실행된 부분은 동시에 실행됨)
	// http://localhost:8080/sda/sch/standby
	@RequestMapping(value = "/standby", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> standbySch() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("standby sch begin================>");
		try {
			checkScheduerMainService();
			thm.standbySch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = "scheduler is not initiated....";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("standby sch end ================>");
		return entity;
	}

	// star(standby후 start를 실행하면 미실행된 부분은 동시에 실행됨)
	// http://localhost:8080/sda/sch/start
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> startSch() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("start sch begin================>");
		try {
			checkScheduerMainService();
			thm.startSch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = "scheduler is not initiated....";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("start sch end ================>");
		return entity;

	}

	// 스케줄러 상태
	// http://localhost:8080/sda/sch/status
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> statusSchList() {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("status sch begin================>");
		try {
			checkScheduerMainService();
			thm.statusSchList();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = "scheduler is not initiated....";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContent(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);
			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("status sch end ================>");
		return entity;
	}

	// 객체상태 확인해서 없으면 만들어줌
	private void checkScheduerMainService() {
		if (thm == null) {
			thm = new SchedulerMainService();
		}
	}

	@PreDestroy
	public void cleanUp() {
		shutdownSch();
		log.info("Scheduler has shutdowned because of Spring Container destroyed");
	}
}
