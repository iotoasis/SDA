package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   사용자 정보의 Mapper클래스
 */
public class UserInfoMapper implements RDBMSMapper {
	private String str;

	public UserInfoMapper(String str) {
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