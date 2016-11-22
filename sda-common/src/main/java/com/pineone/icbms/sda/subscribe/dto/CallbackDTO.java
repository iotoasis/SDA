package com.pineone.icbms.sda.subscribe.dto;

public class CallbackDTO {

	private int callback_seq;
	private String callback_uri;
	private String callback_time;
	private String callback_msg;
	private String use_yn;
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
    
    public CallbackDTO() {
		super();
	}

	public int getCallback_seq() {
		return callback_seq;
	}

	public void setCallback_seq(int callback_seq) {
		this.callback_seq = callback_seq;
	}

	public String getCallback_uri() {
		return callback_uri;
	}

	public void setCallback_uri(String callback_uri) {
		this.callback_uri = callback_uri;
	}

	public String getCallback_time() {
		return callback_time;
	}

	public void setCallback_time(String callback_time) {
		this.callback_time = callback_time;
	}

	public String getCallback_msg() {
		return callback_msg;
	}

	public void setCallback_msg(String callback_msg) {
		this.callback_msg = callback_msg;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}

	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getUuser() {
		return uuser;
	}

	public void setUuser(String uuser) {
		this.uuser = uuser;
	}

	public String getUdate() {
		return udate;
	}

	public void setUdate(String udate) {
		this.udate = udate;
	}

	@Override
	public String toString() {
		return "CallbackDTO [callback_seq=" + callback_seq + ", callback_uri=" + callback_uri + ", callback_time="
				+ callback_time + ", callback_msg=" + callback_msg + ", use_yn=" + use_yn + ", cuser=" + cuser
				+ ", cdate=" + cdate + ", uuser=" + uuser + ", udate=" + udate + "]";
	}

}