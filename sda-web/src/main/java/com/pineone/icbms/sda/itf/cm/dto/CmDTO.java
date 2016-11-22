package com.pineone.icbms.sda.itf.cm.dto;

public class CmDTO {

	private String cmid;
	private String cmname;
	private String execution_type ;
	private String schedule;
	private String cm_remarks;
	private int arg_cnt;
	private String use_yn;
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;

    public CmDTO() {
		super();
	}

	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	public String getCmname() {
		return cmname;
	}

	public void setCmname(String cmname) {
		this.cmname = cmname;
	}

	public String getExecution_type() {
		return execution_type;
	}

	public void setExecution_type(String execution_type) {
		this.execution_type = execution_type;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	
	public String getCm_remarks() {
		return cm_remarks;
	}

	public void setCm_remarks(String cm_remarks) {
		this.cm_remarks = cm_remarks;
	}

	public int getArg_cnt() {
		return arg_cnt;
	}

	public void setArg_cnt(int arg_cnt) {
		this.arg_cnt = arg_cnt;
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

	public String getUuser() {
		return uuser;
	}

	public void setUuser(String uuser) {
		this.uuser = uuser;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getUdate() {
		return udate;
	}

	public void setUdate(String udate) {
		this.udate = udate;
	}

	@Override
	public String toString() {
		return "CmDTO [cmid=" + cmid + ", cmname=" + cmname + ", execution_type=" + execution_type + ", schedule="
				+ schedule + ", cm_remarks=" + cm_remarks + ", arg_cnt=" + arg_cnt + ", use_yn=" + use_yn + ", cuser="
				+ cuser + ", cdate=" + cdate + ", uuser=" + uuser + ", udate=" + udate + "]";
	}

}
