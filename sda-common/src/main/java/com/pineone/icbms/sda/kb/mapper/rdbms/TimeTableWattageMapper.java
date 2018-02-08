package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   전력량의 Mapper 클래스
 */
public class TimeTableWattageMapper implements RDBMSMapper {	
	private String str;

	public TimeTableWattageMapper(String str) {
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