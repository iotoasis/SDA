package com.pineone.icbms.sda.itf.ci.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.google.gson.JsonObject;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.dto.TemplateReqDTO;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.ci.service.CiService;
import com.pineone.icbms.sda.itf.template.dao.TemplateDAO;
import com.pineone.icbms.sda.itf.template.dto.TemplateDTO;
import com.pineone.icbms.sda.itf.template.service.TemplateService;

import net.sf.json.JSONArray;

@RestController
@RequestMapping(value = "/itf")
public class CiController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "ciService")
	private CiService ciService;
	
	@Resource(name = "templateService")
	private TemplateService templateService;

	// http://localhost:8080/sda/itf/ci/ALL
	@RequestMapping(value = "/ci/ALL", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectList(Map<String, Object> commandMap) {
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		List<CiDTO> list = new ArrayList<CiDTO>();
		log.info("/ci/ALL GET start================>");
		try {
			list = ciService.selectList(commandMap);

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
		log.info("/ci/ALL GET end================>");
		return entity;
	}

	// http://localhost:8080/sda/itf/ci/CI-1-1-011
	@RequestMapping(value = "/ci/{ciid}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResponseMessage> selectOne(@PathVariable String ciid) {
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		Gson gson = new Gson();
		String contents;

		log.info("/ci/{ciid} GET start================>");
		try {
			CiDTO ciDTO = new CiDTO();
			commandMap.put("ciid", ciid);

			ciDTO = ciService.selectOne(commandMap);

			contents = gson.toJson(ciDTO);

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
		log.info("/ci/{ciid} GET end================>");
		return entity;
	}
	
	// http://localhost:8080/sda/itf/ci/CQ-1-1-001
	@RequestMapping(value = "/ci/{ciid}", method = RequestMethod.DELETE)
	public  @ResponseBody ResponseEntity<ResponseMessage> delete(@PathVariable String ciid) {
		int rtn_cnt = 0;
		
		Map<String, Object> commandMap = new HashMap<String, Object>();

		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/ci/{ciid} DELETE start================>");
		try {
			commandMap.put("ciid", ciid);

			rtn_cnt = ciService.delete(commandMap);
			
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
		log.info("/ci/{ciid} DELETE end================>");
		return entity;
	}
	
	/* 기존

	// http://localhost:8080/sda/itf/ci/
	@RequestMapping(value = "/ci/", method = RequestMethod.GET)
	public List<Map<String, Object>> selectList(Map<String, Object> commandMap) {
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		if (log.isDebugEnabled()) {
			log.debug("selectList");
		}
		try {
			lists = ciService.selectList(commandMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lists;
	}
	
	*/

	/* 기존
	// http://localhost:8080/sda/itf/ci/CQ-1-1-001
	@RequestMapping(value = "/ci/{idx}", method = RequestMethod.GET)
	public CiDTO selectOne(@PathVariable String idx) {
		CiDTO list = new CiDTO();
		try {
			list = ciService.selectOne(idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	*/
	
	// http://localhost:8080/sda/itf/ci
	@RequestMapping(value = "/ci", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<ResponseMessage> insert(@RequestBody TemplateReqDTO tempReqDTO) {
		
		int rtn_cnt = 0;
		Map<String, Object> commandMap = new HashMap<String, Object>();
		
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/itf/ci POST test start================>");
		try {
			String ciid = tempReqDTO.getCiid();
			commandMap.put("ciid", ciid);
				
			// id 중복검사 후 select_list 개수와 select_cnt 값 일치 확인
			if(ciService.checkId(ciid) == 0) {
				String select_list = tempReqDTO.getSelect_list();
				String[] strArr;
				strArr = select_list.split(",");
				
				List<String> match = new ArrayList<String>();
				
				for(int i=0; i<strArr.length; i++) {
					strArr[i] = strArr[i].trim();
				}
				if(strArr.length == tempReqDTO.getSelect_cnt()) {
					commandMap.clear();
					commandMap.put("tmid", tempReqDTO.getTmid());
					
					int checkTemplate = templateService.checkId(tempReqDTO.getTmid());
					
					if(checkTemplate != 0) {
						TemplateDTO templateDTO = templateService.selectOne(commandMap);
						if(templateDTO.getArg_cnt() == tempReqDTO.getSelect_cnt()) {
							String tmQuery = templateDTO.getTm_query();
							
							Pattern pattern = Pattern.compile("(@\\{srg)([0-9])+\\}");
							Matcher matcher = pattern.matcher(tmQuery);							
							StringBuffer replacedString = new StringBuffer();
							
							for(int i = 0; i<strArr.length; i++){					
								matcher.find();
								match.add(matcher.group());
								matcher.appendReplacement(replacedString, "?"+strArr[Integer.parseInt(matcher.group(2))]);
				
							}
							matcher.appendTail(replacedString);
							
							String resultTmQuery = replacedString.toString();
							
							commandMap.clear();
							CiDTO ciDTO = new CiDTO();
							ciDTO.setCiid(tempReqDTO.getCiid());
							ciDTO.setCiname(tempReqDTO.getCiname());
							ciDTO.setConditions(tempReqDTO.getConditions().toString());
							ciDTO.setDomain(tempReqDTO.getDomain());
							ciDTO.setSparql(resultTmQuery);
							ciDTO.setCi_remarks(tempReqDTO.getCi_remarks());
							ciDTO.setArg_cnt(templateDTO.getArg_cnt());
							
							commandMap.put("ci", ciDTO);
							rtn_cnt = ciService.insert(commandMap);

							if(rtn_cnt==1) {
								resultMsg.setCode(Utils.OK_CODE);
								resultMsg.setMessage(Utils.OK_MSG);
								resultMsg.setContents("");
								entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
							} else {
								throw new UserDefinedException(HttpStatus.BAD_REQUEST, "INSERT_FAILED");
							}
						} else {
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "TEMPLATE_PARAMETER_COUNT_MISMATCHED");
						}
					} else {
						throw new UserDefinedException(HttpStatus.NOT_FOUND, "TEMPLATE_ID_NOT_FOUND");
					}
				} else {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "ARGUMENT_COUNT_MISMATCHED");
				}
			} else {
				throw new UserDefinedException(HttpStatus.CONFLICT, "ID_DUPLICATED");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/itf/ci POST test end================>");
		return entity;
	}
	
	// http://localhost:8080/sda/itf/ci
	@RequestMapping(value = "/ci", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<ResponseMessage> update(@RequestBody TemplateReqDTO tempReqDTO) {
		
		int rtn_cnt = 0;
		Map<String, Object> commandMap = new HashMap<String, Object>();
		
		ResponseMessage resultMsg = new ResponseMessage();
		ResponseEntity<ResponseMessage> entity = null;
		HttpHeaders responseHeaders = new HttpHeaders();

		log.info("/itf/ci PUT test start================>");
		try {
			String ciid = tempReqDTO.getCiid();
			commandMap.put("ciid", ciid);
				
			// id 존재여부 확인 후 select_list 개수와 select_cnt 값 일치 확인
			if(ciService.checkId(ciid) != 0) {
				String select_list = tempReqDTO.getSelect_list();
				String[] strArr;
				strArr = select_list.split(",");
				
				List<String> match = new ArrayList<String>();
				
				for(int i=0; i<strArr.length; i++) {
					strArr[i] = strArr[i].trim();
				}
				if(strArr.length == tempReqDTO.getSelect_cnt()) {
					commandMap.clear();
					commandMap.put("tmid", tempReqDTO.getTmid());
					
					int checkTemplate = templateService.checkId(tempReqDTO.getTmid());
					
					if(checkTemplate != 0) {
						TemplateDTO templateDTO = templateService.selectOne(commandMap);
						if(templateDTO.getArg_cnt() == tempReqDTO.getSelect_cnt()) {
							String tmQuery = templateDTO.getTm_query();
							
							Pattern pattern = Pattern.compile("(@\\{srg)([0-9])+\\}");
							Matcher matcher = pattern.matcher(tmQuery);							
							StringBuffer replacedString = new StringBuffer();
							
							for(int i = 0; i<strArr.length; i++){					
								matcher.find();
								match.add(matcher.group());
								matcher.appendReplacement(replacedString, "?"+strArr[Integer.parseInt(matcher.group(2))]);
				
							}
							matcher.appendTail(replacedString);
							
							String resultTmQuery = replacedString.toString();
							
							commandMap.clear();
							CiDTO ciDTO = new CiDTO();
							ciDTO.setCiid(tempReqDTO.getCiid());
							ciDTO.setCiname(tempReqDTO.getCiname());
							ciDTO.setConditions(tempReqDTO.getConditions().toString());
							ciDTO.setDomain(tempReqDTO.getDomain());
							ciDTO.setSparql(resultTmQuery);
							ciDTO.setCi_remarks(tempReqDTO.getCi_remarks());
							ciDTO.setArg_cnt(templateDTO.getArg_cnt());
							
							commandMap.put("ci", ciDTO);
							rtn_cnt = ciService.update(commandMap);

							if(rtn_cnt==1) {
								resultMsg.setCode(Utils.OK_CODE);
								resultMsg.setMessage(Utils.OK_MSG);
								resultMsg.setContents("");
								entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders, HttpStatus.OK);
							} else {
								throw new UserDefinedException(HttpStatus.BAD_REQUEST, "UPDATE_FAILED");
							}
						} else {
							throw new UserDefinedException(HttpStatus.BAD_REQUEST, "TEMPLATE_PARAMETER_COUNT_MISMATCHED");
						}
					} else {
						throw new UserDefinedException(HttpStatus.NOT_FOUND, "TEMPLATE_ID_NOT_FOUND");
					}
				} else {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "ARGUMENT_COUNT_MISMATCHED");
				}
			} else {
				throw new UserDefinedException(HttpStatus.NOT_FOUND, "ID_NOT_FOUND");
			}

		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = Utils.makeResponseBody(e);

			responseHeaders.add("ExceptionCause", e.getMessage());
			responseHeaders.add("ExceptionClass", e.getClass().getName());
			entity = new ResponseEntity<ResponseMessage>(resultMsg, responseHeaders,
					HttpStatus.valueOf(resultMsg.getCode()));
		}
		log.info("/itf/ci PUT test end================>");
		return entity;
	}
	
	
	
	
	
	

	// http://localhost:8080/sda/itf/ci/
	// [{"parent_idx":5000, "title":"title5000", "contents":"5000", "hit_cnt":0,
	// "del_gb":"N", "crea_dtm":"aaaa", "crea_id":"Admin"}]
	// [{"parent_idx":100, "title":"title200", "contents":"내용100", "hit_cnt":0,
	// "del_gb":"N", "crea_dtm":"aaaa", "crea_id":"Admin"},{"parent_idx":100,
	// "title":"title201", "contents":"내용100", "hit_cnt":0, "del_gb":"N",
	// "crea_dtm":"aaaa", "crea_id":"Admin"},{"parent_idx":100,
	// "title":"title202", "contents":"내용100", "hit_cnt":0, "del_gb":"N",
	// "crea_dtm":"aaaa", "crea_id":"Admin"}]
	@RequestMapping(value = "/ci/", method = RequestMethod.POST)
	public int insert(@RequestBody CiDTO[] cmDTO) {
		int rtn_cnt = 0;
		List<CiDTO> list = new ArrayList<CiDTO>();
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < cmDTO.length; i++) {
			list.add(cmDTO[i]);
		}

		map.put("list", list);
		try {
			rtn_cnt = ciService.insert(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn_cnt;
	}



}
