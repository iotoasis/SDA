package com.pineone.icbms.sda.sch.service;

import java.sql.Connection;
import java.util.List;

public interface RdbmsService { 

	/**
	 * db접속
	 * @throws Exception
	 * @return Connection
	 */
	Connection getConnection() throws Exception;
	
	/**
	 * 대상 구하기
	 * @throws Exception
	 * @return List<String>
	 */
	List<String> getArgs() throws Exception;
	
	/**
	 * update할 값 구하기
	 * @param arg
	 * @throws Exception
	 * @return List<String>
	 */
	List<String> getAggs(String arg) throws Exception;
	
	/**
	 * 대상 구하기
	 * @throws Exception
	 * @return int
	 */
	int update() throws Exception;

	
}
