package com.pineone.icbms.sda.comm.dto;

public class ResponseMessageErr {

	String contents;

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "ResponseMessageErr [contents=" + contents + "]";
	}

}