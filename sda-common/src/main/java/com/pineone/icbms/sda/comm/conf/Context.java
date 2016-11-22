package com.pineone.icbms.sda.comm.conf;

import java.io.Serializable;

public class Context implements Serializable {
	private static final long serialVersionUID = -7263835548888822548L;
	private String context = "";
	private String connKey = "AAA";
	private boolean isSlave = false;
	private boolean isNewIndex = false;
	private boolean useSail = true;
	private long ctxId = -1;

	public long getCtxId() {
		return ctxId;
	}

	public void setCtxId(long ctxId) {
		this.ctxId = ctxId;
	}

	public Context(String context) {
		this.context = context;
	}

	public String getConnectionKey() {
		return connKey;
	}

	public void setConnectionKey(String key) {
		this.connKey = key;
	}

	public void setUseSAIL(boolean use) {
		this.useSail = use;
	}

	public boolean useSAIL() {
		return useSail;
	}

	public void setSlave(boolean slave) {
		this.isSlave = slave;
	}

	public boolean isSlave() {
		return this.isSlave;
	}

	@Deprecated
	public String getContext() {
		return context;
	}

	public String name() {
		return context;
	}

	public boolean isNewIndex() {
		return isNewIndex;
	}
}
