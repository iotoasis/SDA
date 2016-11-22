package com.pineone.icbms.sda.itf.ci.service;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

public interface CiService {

	List<Map<String, Object>> selectList(Map<String, Object> map) throws Exception;
	CiDTO selectOne(String idx) throws Exception;
	int insert(Map<String, Object> map) throws Exception;
	int update(String idx) throws Exception;
	int update(CiDTO[] ciDTO) throws Exception;
	int delete(String idx) throws Exception;
	int delete(CiDTO[] ciDTO) throws Exception;

}
