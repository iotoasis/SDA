package com.pineone.icbms.sda.kb.dto;

public class LWM2MDistanceDTO {
	LWM2MDistance_Content content;
	String status;
	
	public LWM2MDistance_Content getContent() {
		return content;
	}

	public void setContent(LWM2MDistance_Content content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public class LWM2MDistance_Content {
		String id;
		String value;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}
}