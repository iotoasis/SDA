package com.pineone.icbms.sda.comm.dto;

public class ConditionDTO {

	private String property;
	private String object;
    
    public ConditionDTO() {
		super();
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ConditionDTO [property=" + property + ", object=" + object + "]";
	}
	
}