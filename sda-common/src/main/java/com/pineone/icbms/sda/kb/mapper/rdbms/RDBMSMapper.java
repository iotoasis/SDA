package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 * RDBMS용 Mapper클래스
 */
public interface RDBMSMapper {
	/**
	 * 초기화
	 * @return void
	 */
	public void initResource();
	
	
	/**
	 * 변환값
	 * @return String
	 */
	public String  from();
}