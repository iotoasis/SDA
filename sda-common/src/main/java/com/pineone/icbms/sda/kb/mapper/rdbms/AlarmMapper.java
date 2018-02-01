package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   Alarm정보의 Mapper클래스
 */
public class AlarmMapper implements RDBMSMapper {
	private String str;

	public AlarmMapper(String str) {
		this.str = str;
	}
	
	@Override
	public void initResource() {
	}

	@Override
	public String from() {
		initResource();

		return this.str;
	}
}