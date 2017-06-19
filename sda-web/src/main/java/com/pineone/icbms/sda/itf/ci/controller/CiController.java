package com.pineone.icbms.sda.itf.ci.controller;

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
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.ci.service.CiService;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

@RestController
@RequestMapping(value = "/itf")
public class CiController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "ciService")
	private CiService ciService;

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
	
	/* 기

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

	/* 기
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

	// http://localhost:8080/sda/itf/ci/CQ-1-1-001
	@RequestMapping(value = "/ci/{idx}", method = RequestMethod.DELETE)
	public int delete(@PathVariable String idx) {
		int rtn_cnt = 0;
		try {
			rtn_cnt = ciService.delete(idx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn_cnt;
	}

}
