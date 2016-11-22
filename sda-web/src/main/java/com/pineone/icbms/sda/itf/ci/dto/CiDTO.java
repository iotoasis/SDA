package com.pineone.icbms.sda.itf.ci.dto;

public class CiDTO {

	private String ciid;
	private String ciname;
	private String conditions;
	private String domain;
	private String sparql;
	private String ci_remarks;
	private int arg_cnt;
	private String use_yn;
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
    
    public CiDTO() {
		super();
	}

	public String getCiid() {
		return ciid;
	}

	public void setCiid(String ciid) {
		this.ciid = ciid;
	}

	public String getCiname() {
		return ciname;
	}

	public void setCiname(String ciname) {
		this.ciname = ciname;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSparql() {
		return sparql;
	}

	public void setSparql(String sparql) {
		this.sparql = sparql;
	}

	public String getCi_remarks() {
		return ci_remarks;
	}

	public void setCi_remarks(String ci_remarks) {
		this.ci_remarks = ci_remarks;
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
		return "CiDTO [ciid=" + ciid + ", ciname=" + ciname + ", conditions=" + conditions + ", domain=" + domain
				+ ", sparql=" + sparql + ", ci_remarks=" + ci_remarks + ", arg_cnt=" + arg_cnt + ", use_yn=" + use_yn
				+ ", cuser=" + cuser + ", cdate=" + cdate + ", uuser=" + uuser + ", udate=" + udate + "]";
	}
	
	

}