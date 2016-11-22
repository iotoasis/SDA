package com.pineone.icbms.sda.kb.mapper.rdbms;

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
	
	public static void main(String[] args) {
		String s = " icbms:u00001 rdf:type foaf:Person . ";
		
		AlarmMapper mapper = new AlarmMapper(s);
		

		System.out.println("resul from mapper ====>  "+mapper.from());

	}

}
