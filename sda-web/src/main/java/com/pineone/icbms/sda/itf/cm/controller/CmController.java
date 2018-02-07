package com.pineone.icbms.sda.itf.cm.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.ci.service.CiService;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmReqDTO;
import com.pineone.icbms.sda.itf.cm.service.CmService;
import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;
import com.pineone.icbms.sda.itf.cmi.service.CmiService;

/**
 * CM용 Controller
 */
@RestController
@RequestMapping(value = "/itf")
public class CmController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "cmService")
	private CmService cmService;
	
	@Resource(name = "ciService")
	private CiService ciService;
	
	@Resource(name = "cmiService")
	private CmiService cmiService;
	
	/**
	 * 목록조회
	 * @param commandMap
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cm/all", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectList(Map<String, Object> commandMap) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		List<CmDTO> list = new ArrayList<CmDTO>();
		log.info("/cm/all GET start================>");
		try {
			list = cmService.selectList(commandMap);

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
		log.info("/cm/all GET end================>");
		return entity;
	}
	
	/**
	 * 단건 조회
	 * @param cmid
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cm/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectOne(@PathVariable String cmid) {
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		log.info("/cm/{cmid} GET start================>");
		try {
			CmDTO cmDTO = new CmDTO();
			commandMap.put("cmid", cmid);

			cmDTO = cmService.selectOne(commandMap);

			contents = gson.toJson(cmDTO);

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
		log.info("/cm/{cmid} GET end================>");
		return entity;
	}
	
	/**
	 * 목록조회
	 * @param commandMap
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cmcmici", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectCmCmiCiList(Map<String, Object> commandMap) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		log.info("/cmcmici GET start================>");
		try {
			list = cmService.selectCmCmiCiList(commandMap);

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
		log.info("/cmcmici GET end================>");
		return entity;
	}

	/**
	 * 단건조회
	 * @param cmid
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cmcmici/{cmid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectCmCmiCiOne(@PathVariable String cmid) {
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		log.info("/cmcmici/{cmid} GET start================>");
		try {
			CmCiDTO cmCiDTO = new CmCiDTO();
			commandMap.put("cmid", cmid);

			cmCiDTO = cmService.selectCmCmiCiOne(commandMap);

			contents = gson.toJson(cmCiDTO);

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
		log.info("/cmcmici/{cmid} GET end================>");
		return entity;
	}
	
	/**
	 * 삭제
	 * @param cmid
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cm/{cmid}", method = RequestMethod.DELETE)
	public  @ResponseBody ResponseEntity<ResponseMessage> delete(@PathVariable String cmid) {
		int rtn_cnt = 0;
		
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/cm/{cmid} DELETE start================>");
		try {
			commandMap.put("cmid", cmid);

			rtn_cnt = cmService.delete(commandMap);
			
			if(rtn_cnt==1) {
				entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
				resultMsg.setCode(Utils.OK_CODE);
				resultMsg.setMessage(Utils.OK_MSG);
			} else {		
				throw new UserDefinedException(HttpStatus.NOT_FOUND, "NOT_FOUND");
			}
			resultMsg.setContents("");				
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/cm/{cmid} DELETE end================>");
		return entity;
	}
	
	/**
	 * 등록
	 * @param cmReqDTO
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cm", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<ResponseMessage> insert(@RequestBody CmReqDTO cmReqDTO) {
		
		int cm_rtn_cnt = 0;
		int cmi_rtn_cnt = 0;
		
		Map<String, Object> commandMap = new HashMap<String, Object>();
		
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/itf/cm POST start================>");
		try {
			String cmid = cmReqDTO.getCmid();
			commandMap.put("cmid", cmid);
				
			// cmid 중복검사
			if(cmService.checkId(cmid) == 0) {
				String ci_list = cmReqDTO.getCiid();
				String[] strArr;
				Map<String, Object> ciMap = new HashMap<String, Object>();
				
				strArr = ci_list.split(",");
				for(int i=0; i<strArr.length; i++) {
					strArr[i] = strArr[i].trim();
				}
					
				for(int i=0; i<strArr.length; i++) {
					
					int checkCi = ciService.checkId(strArr[i]);
					
					//ciid 존재 확인 
					if(checkCi==0){
						throw new UserDefinedException(HttpStatus.NOT_FOUND, "CI_ID_NOT_FOUND");
					}
					//ciid 중복 확인
					for(int j=0; j<i; j++){
						if(strArr[i].equals(strArr[j]))
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "CI_ID_DUPLICATED");
					}
					commandMap.clear();
					commandMap.put("ciid", strArr[i]);
					CiDTO ciDTO = ciService.selectOne(commandMap);
					ciMap.put(strArr[i], ciDTO.getArg_cnt());				
				}
				
				// 입력받은 ci의 arg_cnt가 모두 일치하는지 확인
				for(int i=0; i<strArr.length; i++) {
					for(int j=0; j<i; j++) {
						if(!ciMap.get(strArr[i]).toString().equals(ciMap.get(strArr[j]).toString()))
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "CI_ARGUMENT_COUNT_MISMATCHED");
					}			
				}
				
				CmDTO cmDTO = new CmDTO();
				
				cmDTO.setCmid(cmReqDTO.getCmid());
				cmDTO.setCmname(cmReqDTO.getCmname());
				cmDTO.setExecution_type(cmReqDTO.getExecution_type());
				cmDTO.setCm_remarks(cmReqDTO.getCm_remarks());
				cmDTO.setArg_cnt(Integer.parseInt(ciMap.get(strArr[0]).toString()));
				
				commandMap.clear();
				commandMap.put("cm", cmDTO);
				cm_rtn_cnt = cmService.insert(commandMap);
				
				CmiDTO cmiDTO = new CmiDTO();
				
				for(int i=0; i<strArr.length; i++) {
					cmiDTO.setTnsda_context_model_cmid(cmReqDTO.getCmid());
					cmiDTO.setTnsda_context_info_ciid(strArr[i]);
					cmiDTO.setCi_seq(i+1);
					commandMap.clear();
					commandMap.put("cmi", cmiDTO);
					cmi_rtn_cnt = cmiService.insert(commandMap);
					if(cmi_rtn_cnt != 1) {
						throw new UserDefinedException(HttpStatus.BAD_REQUEST, "CMI_INSERT_FAILED");
					}
				}
				
				if(cm_rtn_cnt==1 ) {
					resultMsg.setCode(Utils.OK_CODE);
					resultMsg.setMessage(Utils.OK_MSG);
					resultMsg.setContents("");
					entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
				} else {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "INSERT_FAILED");
				}
			} else {
				throw new UserDefinedException(HttpStatus.CONFLICT, "CM_ID_DUPLICATED");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/itf/cm POST end================>");
		return entity;
	}
	
	/**
	 * 수정
	 * @param cmReqDTO
	 * @return ResponseEntity<ResponseMessage>
	 */
	@RequestMapping(value = "/cm", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<ResponseMessage> update(@RequestBody CmReqDTO cmReqDTO) {
		
		int cm_rtn_cnt = 0;
		int cmi_rtn_cnt = 0;
		
		Map<String, Object> commandMap = new HashMap<String, Object>();
		
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/itf/cm PUT start================>");
		try {
			String cmid = cmReqDTO.getCmid();
			commandMap.put("cmid", cmid);
				
			// cmid 존재 여부 확인 
			if(cmService.checkId(cmid) == 1) {
				String ci_list = cmReqDTO.getCiid();
				String[] strArr;
				Map<String, Object> ciMap = new HashMap<String, Object>();
				
				strArr = ci_list.split(",");
				for(int i=0; i<strArr.length; i++) {
					strArr[i] = strArr[i].trim();
				}
					
				for(int i=0; i<strArr.length; i++) {
					
					int checkCi = ciService.checkId(strArr[i]);
					
					//ciid 존재 확인 
					if(checkCi==0){
						throw new UserDefinedException(HttpStatus.NOT_FOUND, "CI_ID_NOT_FOUND");
					}
					//ciid 중복 확인
					for(int j=0; j<i; j++){
						if(strArr[i].equals(strArr[j]))
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "CI_ID_DUPLICATED");
					}
					commandMap.clear();
					commandMap.put("ciid", strArr[i]);
					CiDTO ciDTO = ciService.selectOne(commandMap);
					ciMap.put(strArr[i], ciDTO.getArg_cnt());				
				}
				
				// 입력받은 ci의 arg_cnt가 모두 일치하는지 확인
				for(int i=0; i<strArr.length; i++) {
					for(int j=0; j<i; j++) {
						if(!ciMap.get(strArr[i]).toString().equals(ciMap.get(strArr[j]).toString()))
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "CI_ARGUMENT_COUNT_MISMATCHED");
					}			
				}
				
				CmDTO cmDTO = new CmDTO();
				
				cmDTO.setCmid(cmReqDTO.getCmid());
				cmDTO.setCmname(cmReqDTO.getCmname());
				cmDTO.setExecution_type(cmReqDTO.getExecution_type());
				cmDTO.setCm_remarks(cmReqDTO.getCm_remarks());
				cmDTO.setArg_cnt(Integer.parseInt(ciMap.get(strArr[0]).toString()));
				
				commandMap.clear();
				commandMap.put("cm", cmDTO);
				cm_rtn_cnt = cmService.update(commandMap);
				
				// update할 cm의 cmi 데이터 삭제 
				cmiService.delete(cmReqDTO.getCmid());
				
				CmiDTO cmiDTO = new CmiDTO();
				
				for(int i=0; i<strArr.length; i++) {
					cmiDTO.setTnsda_context_model_cmid(cmReqDTO.getCmid());
					cmiDTO.setTnsda_context_info_ciid(strArr[i]);
					cmiDTO.setCi_seq(i+1);
					commandMap.clear();
					commandMap.put("cmi", cmiDTO);
					cmi_rtn_cnt = cmiService.insert(commandMap);
					if(cmi_rtn_cnt != 1) {
						throw new UserDefinedException(HttpStatus.BAD_REQUEST, "CMI_INSERT_FAILED");
					}
				}
				
				if(cm_rtn_cnt==1 ) {
					resultMsg.setCode(Utils.OK_CODE);
					resultMsg.setMessage(Utils.OK_MSG);
					resultMsg.setContents("");
					entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
				} else {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "UPDATE_FAILED");
				}
			} else {
				throw new UserDefinedException(HttpStatus.NOT_FOUND, "CM_ID_NOT_FOUND");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/itf/cm PUT end================>");
		return entity;
	}
}