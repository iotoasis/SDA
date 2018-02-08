package com.pineone.icbms.sda.comm.dto;

/**
 * 응답메세지
 */
public class ResponseMessage {

	int code;
	String message;
	String contents;
	
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
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	@Override
	public String toString() {
		return "ResponseMessage [code=" + code + ", message=" + message + ", contents=" + contents + "]";
	}
	
}