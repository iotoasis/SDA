package com.pineone.icbms.sda.comm.dto;

import java.util.List;
import java.util.Map;

public class ResponseMessageOk {

	List<Map<String, String>> content;

	public List<Map<String, String>> getContent() {
		return content;
	}

	public void setContent(List<Map<String, String>> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ResponseMessageOk [content=" + content + "]";
	}



}