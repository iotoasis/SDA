package com.pineone.icbms.sda.comm.dto;

import java.util.List;
import java.util.Map;

public class ResponseMessageOk {

	String cmd;
	String contextId;
	String time;
	List<Map<String, String>> contents;
	
	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<Map<String, String>> getContents() {
		return contents;
	}

	public void setContents(List<Map<String, String>> contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "ResponseMessageOk [cmd=" + cmd + ", contextId=" + contextId + ", time=" + time + ", contents="
				+ contents + "]";
	}

}