package com.pineone.icbms.sda.subscribe.dto;

public class CallbackNoticeDTO {

	private int callback_seq;
	private String cmid;
	private String uri;
	private String so_notice_time;
	private String so_notice_msg;
	private String work_result;
	private String triple_file_name;
	private String triple_check_result;
	private String use_yn;
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
    
    public CallbackNoticeDTO() {
		super();
	}

	public int getCallback_seq() {
		return callback_seq;
	}

	public void setCallback_seq(int callback_seq) {
		this.callback_seq = callback_seq;
	}

	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSo_notice_time() {
		return so_notice_time;
	}

	public void setSo_notice_time(String so_notice_time) {
		this.so_notice_time = so_notice_time;
	}

	public String getSo_notice_msg() {
		return so_notice_msg;
	}

	public void setSo_notice_msg(String so_notice_msg) {
		this.so_notice_msg = so_notice_msg;
	}

	public String getWork_result() {
		return work_result;
	}

	public String getTriple_file_name() {
		return triple_file_name;
	}

	public void setTriple_file_name(String triple_file_name) {
		this.triple_file_name = triple_file_name;
	}

	public String getTriple_check_result() {
		return triple_check_result;
	}

	public void setTriple_check_result(String triple_check_result) {
		this.triple_check_result = triple_check_result;
	}

	public void setWork_result(String work_result) {
		this.work_result = work_result;
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
		return "CallbackNoticeDTO [callback_seq=" + callback_seq + ", cmid=" + cmid + ", uri=" + uri
				+ ", so_notice_time=" + so_notice_time + ", so_notice_msg=" + so_notice_msg + ", work_result="
				+ work_result + ", triple_file_name=" + triple_file_name + ", triple_check_result="
				+ triple_check_result + ", use_yn=" + use_yn + ", cuser=" + cuser + ", cdate=" + cdate + ", uuser="
				+ uuser + ", udate=" + udate + "]";
	}
}