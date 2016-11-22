package com.pineone.icbms.sda.sch.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.sch.dto.SchDTO;

public interface RdbmsService { 

	//db접속
	Connection getConnection() throws Exception;
	
	// 대상 구하기
	List<String> getArgs() throws Exception;
	
	// update할 값 구하기
	List<String> getAggs(String arg) throws Exception;
	
	// 대상 구하기
	int update() throws Exception;

	
}
