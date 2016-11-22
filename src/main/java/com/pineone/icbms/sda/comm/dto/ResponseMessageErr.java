package com.pineone.icbms.sda.comm.dto;

public class ResponseMessageErr {

	String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ResponseMessageErr [content=" + content + "]";
	}

}