package com.pineone.icbms.sda.itf.ci.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.ci.service.CiService;

@RestController
@RequestMapping(value = "/itf")
public class CiController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource(name = "ciService")
	private CiService ciService;

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
