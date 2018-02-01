package com.pineone.icbms.sda.kb.mapper.rdbms;

/**
 *   Device정보의 Mapper 클래스
 */
public class DeviceInfoMapper implements RDBMSMapper {
	private String str;

	public DeviceInfoMapper(String str) {
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