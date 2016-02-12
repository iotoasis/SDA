package com.pineone.icbms.sda.kb.dto;

public class SurveyInfoDTO {
	String val;
	String class_code;
	String user_id;
	String zone;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getClass_code() {
		return class_code;
	}
	public void setClass_code(String class_code) {
		this.class_code = class_code;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String toString(){
		return 
				this.val+" : "+this.class_code+" : "+this.user_id+this.zone;
	}
	
}
