package com.pineone.icbms.sda.comm.conf;

import java.io.Serializable;

/**
 * 환경정보 처리용 인터페이스
 */
public interface Configuration extends Serializable {
	/**
	 * 환경정보 준비
	 * @throws ConfigException
	 * @return void
	 */
	void prepare() throws ConfigException;
	
	/**
	 * 프로젝트별 환경 정보 추가
	 * @throws ConfigException
	 * @return void
	 */
	void addProjectConfiguration()  throws ConfigException;
	
	/**
	 * 프로퍼터 가져오기(문자열)
	 * @param key
	 * @throws ConfigException
	 * @return String
	 */
	String getStringProperty(String key) throws ConfigException;
	
	/**
	 * 프로퍼티 가져오기(문자열)
	 * @param key
	 * @param base
	 * @throws ConfigException
	 * @return String
	 */
	String getStringProperty(String key, String base) throws ConfigException;
	
	/**
	 * 프로퍼티 가져오기(숫자)
	 * @param key
	 * @throws ConfigException
	 * @return int
	 */
	int getIntProperty(String key) throws ConfigException;	
	
	/**
	 * 프로퍼티 가져오기(숫자)
	 * @param key
	 * @param base
	 * @throws ConfigException
	 * @return int
	 */
	int getIntProperty(String key, int base) throws ConfigException;	
	
	/**
	 * key값이 있는지 확인
	 * @param key
	 * @throws ConfigException
	 * @return boolean
	 */
	boolean contains(String key) throws ConfigException;
	
	/**
	 * 프로퍼티 가져오기(boolean)
	 * @param key
	 * @throws ConfigException
	 * @return boolean
	 */
	boolean getBooleanProperty(String key) throws ConfigException;	
	
	/**
	 * 프로퍼티 가져오기(boolean)
	 * @param key
	 * @param base
	 * @throws ConfigException
	 * @return boolean
	 */
	boolean getBooleanProperty(String key, boolean base) throws ConfigException;	
	
	/**
	 * 메인경로 설정
	 * @param path
	 * @throws ConfigException
	 * @return void
	 */
	void setMainPath(String path) throws ConfigException;
	
	/**
	 * 메인경로 가져오기
	 * @throws ConfigException
	 * @return String
	 */
	String getMainPath() throws ConfigException;
	
	/**
	 * Root가져오기
	 * @throws ConfigException
	 * @return String
	 */
	String getRoot() throws ConfigException;
	
	/**
	 * 프로퍼티 추가
	 * @param key
	 * @param value
	 * @return void
	 */
	void putProperty(String key, Object value);
	
	/**
	 * Path구분자 제거
	 * @param path
	 * @throws ConfigException
	 * @return String
	 */
	public String removeEndPathSeperator(String path) throws ConfigException;
	
	/**
	 * Root바꾸기
	 * @param path
	 * @throws ConfigException
	 * @return String
	 */
	public String reverseRoot(String path) throws ConfigException;
	
	/**
	 * Prefix바꾸기
	 * @param str
	 * @throws ConfigException
	 * @return Object
	 */
	public Object reversePrefix(String str) throws ConfigException;
}