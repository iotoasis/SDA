package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   시간표및 전력량의 Mapper 클래스
 */
public class TimeTableWattageMapper implements RDBMSMapper {	
	private String str;

	public TimeTableWattageMapper(String str) {
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