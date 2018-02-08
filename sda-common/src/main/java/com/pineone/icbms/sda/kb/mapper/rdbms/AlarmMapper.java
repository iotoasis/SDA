package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   Alarm정보의 Mapper클래스
 */
public class AlarmMapper implements RDBMSMapper {
	private String str;

	public AlarmMapper(String str) {
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