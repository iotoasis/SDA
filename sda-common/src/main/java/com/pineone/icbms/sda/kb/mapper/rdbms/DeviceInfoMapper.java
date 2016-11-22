package com.pineone.icbms.sda.kb.mapper.rdbms;

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
	
	public static void main(String[] args) {
		String s = " icbms:u00001 rdf:type foaf:Person . ";
		
		DeviceInfoMapper mapper = new DeviceInfoMapper(s);
		

		System.out.println("resul from mapper ====>  "+mapper.from());

	}

}
