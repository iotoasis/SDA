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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.comm.SchedulerMainService;
import com.pineone.icbms.sda.sch.service.SchService;
import com.pineone.icbms.sda.sf.TripleService;

@RestController
@RequestMapping(value = "/sch")
public class SchController {
	private final Log log = LogFactory.getLog(this.getClass());

	// 스케줄러
	private SchedulerMainService thm = null;

	@Resource(name = "schService")
	private SchService schService;

	// 스케쥴러 기동
	// http://localhost:8080/sda/sch/init?p=XXXX
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> inittSch(@RequestParam(value="p")  String args) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("init sch begin================>");
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}
			
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}

			// init
			thm = new SchedulerMainService();
			thm.JobInit();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (ObjectAlreadyExistsException e) {
			msg = "scheduler is already initiated...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
		log.info("init sch end================>");
		return entity;
	}

	// 스케쥴러 종료
	// http://localhost:8080/sda/sch/shutdown?p=xxxx
	@RequestMapping(value = "/shutdown", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> shutdownSch(@RequestParam(value="p")  String args) {
		log.debug("requested parameter(p) for shutdownSch ==>" + args);
		
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("shutdown sch begin================>");
	
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}

			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}
			
			checkScheduerMainService();
			thm.shutdown();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = Utils.SchNotInit;
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
		log.info("shutdown sch end ================>");
		return entity;
	}

	// pause(pause후 resume을 실행하면 미실행된 부분은 수행하지 않음)
	// http://localhost:8080/sda/sch/pause?p=xxxx
	@RequestMapping(value = "/pause", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> pauseSch(@RequestParam(value="p")  String args) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("pause sch begin================>");
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}
			
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}

			checkScheduerMainService();
			thm.pauseSch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = Utils.SchNotInit;
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
		log.info("pause sch end ================>");
		return entity;

	}

	// resume(pause후 resume을 실행하면 미실행된 부분은 수행하지 않음)
	// http://localhost:8080/sda/sch/resume?=xxxx
	@RequestMapping(value = "/resume", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> resumeSch(@RequestParam(value="p")  String args) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("resume sch begin================>");
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}
			
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}

			checkScheduerMainService();
			thm.resumeSch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = Utils.SchNotInit;
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
		log.info("resume sch end ================>");
		return entity;

	}

	// standby(standby후 start를 실행하면 미실행된 부분은 동시에 실행됨)
	// http://localhost:8080/sda/sch/standby?p=xxxx
	@RequestMapping(value = "/standby", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> standbySch(@RequestParam(value="p")  String args) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("standby sch begin================>");
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}
			
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}

			checkScheduerMainService();
			thm.standbySch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = Utils.SchNotInit;
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
		log.info("standby sch end ================>");
		return entity;
	}

	// star(standby후 start를 실행하면 미실행된 부분은 동시에 실행됨)
	// http://localhost:8080/sda/sch/start?p=xxxx
	@RequestMapping(value = "/start", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> startSch(@RequestParam(value="p")  String args) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("start sch begin================>");
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}
			
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}

			checkScheduerMainService();
			thm.startSch();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = Utils.SchNotInit;
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
		log.info("start sch end ================>");
		return entity;

	}

	// 스케줄러 상태
	// http://localhost:8080/sda/sch/status
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> statusSchList(@RequestParam(value="p")  String args) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String msg = "";

		log.info("status sch begin================>");
		try {
			// dm인지 dw인지 확인(scheduler는 dw에서만 가능함)
			if(! Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw.hostname"))) {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "This action are allowd at only Data Warehouse server ! ");
			}
			
			if( ! Utils.checkPass(args)) {
				log.debug("p("+args+") is not valid... ");
				throw new UserDefinedException(HttpStatus.BAD_REQUEST);
			}

			checkScheduerMainService();
			thm.statusSchList();
			msg = thm.getStatusList();
			
			resultMsg.setCode(Utils.OK_CODE);
			resultMsg.setMessage(Utils.OK_MSG);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
		} catch (PersistenceException e) {
			msg = "db connection error...";
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (NullPointerException e) {
			msg = Utils.SchNotInit;
			resultMsg = Utils.makeResponseBody(e);
			resultMsg.setContents(gson.toJson(msg));
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		} catch (Exception e) {
			resultMsg = Utils.makeResponseBody(e);
			log.debug("Exception : "+resultMsg.getMessage());			
			responseHeaders.add("ExceptionCause", resultMsg.getMessage());
			responseHeaders.add("ExceptionClass", resultMsg.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.debug("return entity : "+entity.toString());
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
		try {
			checkScheduerMainService();			
			thm.shutdown();
		} catch(Exception e) {
			log.debug("cleanUp() exception : "+e.getMessage());
		}
		log.info("Scheduler has shutdowned because of Spring Container destroyed");
	}
}
