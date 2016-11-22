package com.pineone.icbms.sda.kb.mapper.rdbms;

public class UserInfoMapper implements RDBMSMapper {
	private String str;

	public UserInfoMapper(String str) {
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
		
		UserInfoMapper mapper = new UserInfoMapper(s);

		System.out.println("resul from mapper ====>  "+mapper.from());

	}

}
