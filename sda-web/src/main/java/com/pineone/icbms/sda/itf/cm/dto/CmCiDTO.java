package com.pineone.icbms.sda.itf.cm.dto;

public class CmCiDTO {

	private String tnsda_context_model_cmid;
	private String tnsda_context_info_ciid;
	private String conditions;
	private String domain;
	private String sparql;
	private String cmname;
	private String ciname;
	private String ci_remarks;
	private String cm_remarks;
	private int ci_arg_cnt;
	private int cm_arg_cnt;

    public int getCi_arg_cnt() {
		return ci_arg_cnt;
	}

	public void setCi_arg_cnt(int ci_arg_cnt) {
		this.ci_arg_cnt = ci_arg_cnt;
	}

	public int getCm_arg_cnt() {
		return cm_arg_cnt;
	}

	public void setCm_arg_cnt(int cm_arg_cnt) {
		this.cm_arg_cnt = cm_arg_cnt;
	}

	public CmCiDTO() {
		super();
	}

	public String getTnsda_context_model_cmid() {
		return tnsda_context_model_cmid;
	}

	public void setTnsda_context_model_cmid(String tnsda_context_model_cmid) {
		this.tnsda_context_model_cmid = tnsda_context_model_cmid;
	}

	public String getTnsda_context_info_ciid() {
		return tnsda_context_info_ciid;
	}

	public void setTnsda_context_info_ciid(String tnsda_context_info_ciid) {
		this.tnsda_context_info_ciid = tnsda_context_info_ciid;
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

	public String getCmname() {
		return cmname;
	}

	public void setCmname(String cmname) {
		this.cmname = cmname;
	}

	public String getCiname() {
		return ciname;
	}

	public void setCiname(String ciname) {
		this.ciname = ciname;
	}

	public String getCi_remarks() {
		return ci_remarks;
	}

	public void setCi_remarks(String ci_remarks) {
		this.ci_remarks = ci_remarks;
	}

	public String getCm_remarks() {
		return cm_remarks;
	}

	public void setCm_remarks(String cm_remarks) {
		this.cm_remarks = cm_remarks;
	}

	@Override
	public String toString() {
		return "CmCiDTO [tnsda_context_model_cmid=" + tnsda_context_model_cmid + ", tnsda_context_info_ciid="
				+ tnsda_context_info_ciid + ", conditions=" + conditions + ", domain=" + domain + ", sparql=" + sparql
				+ ", cmname=" + cmname + ", ciname=" + ciname + ", ci_remarks=" + ci_remarks + ", cm_remarks="
				+ cm_remarks + ", ci_arg_cnt=" + ci_arg_cnt + ", cm_arg_cnt=" + cm_arg_cnt + "]";
	}

}