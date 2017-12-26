package com.pineone.icbms.sda.kb.dto;

public class DomappTempStudentDTO {
	String user_id;
	int temperature;
	String inhouse;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public String getInhouse() {
		return inhouse;
	}
	public void setInhouse(String inhouse) {
		this.inhouse = inhouse;
	}
}
