package com.pineone.icbms.sda.comm.dto;

public class ResponseMessage {

	int code;
	String message;
	String content;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "ResponseMessage [code=" + code + ", message=" + message + ", content=" + content + "]";
	}
	
}