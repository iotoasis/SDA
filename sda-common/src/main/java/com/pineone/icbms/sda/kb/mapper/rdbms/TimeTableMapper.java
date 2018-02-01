package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   시간표의 Mapper 클래스
 */
public class TimeTableMapper implements RDBMSMapper {	
	private String str;

	public TimeTableMapper(String str) {
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