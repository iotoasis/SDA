package com.pineone.icbms.sda.itf.template.dto;

/**
 *  템플릿용 DTO
 */
public class TemplateDTO {
	
	private String tmid;
	private String tmname;
	private String tm_remarks;
	private String tm_query;
	private String use_yn;
	private String cuser;
	private String cdate;
	private String uuser;
	private String udate;
	private int arg_cnt;
	
	public TemplateDTO() {
		super();
	}

	public String getTmid() {
		return tmid;
	}

	public void setTmid(String tmid) {
		this.tmid = tmid;
	}

	public String getTmname() {
		return tmname;
	}

	public void setTmname(String tmname) {
		this.tmname = tmname;
	}

	public String getTm_remarks() {
		return tm_remarks;
	}

	public void setTm_remarks(String tm_remarks) {
		this.tm_remarks = tm_remarks;
	}

	public String getTm_query() {
		return tm_query;
	}

	public void setTm_query(String tm_query) {
		this.tm_query = tm_query;
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

	public int getArg_cnt() {
		return arg_cnt;
	}

	public void setArg_cnt(int arg_cnt) {
		this.arg_cnt = arg_cnt;
	}

}