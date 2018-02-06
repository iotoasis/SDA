package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   시간표의 Mapper 클래스
 */
public class TimeTableMapper implements RDBMSMapper {	
	private String str;

	public TimeTableMapper(String str) {
		this.str = str;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.rdbms.RDBMSMapper#initResource()
	 */
	@Override
	public void initResource() {
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.rdbms.RDBMSMapper#from()
	 */
	@Override
	public String from() {
		initResource();

		return this.str;
	}
}