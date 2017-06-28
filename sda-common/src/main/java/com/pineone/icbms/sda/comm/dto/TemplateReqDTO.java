package com.pineone.icbms.sda.comm.dto;

import java.util.Arrays;

import com.google.gson.Gson;
import com.sun.mail.imap.Utility.Condition;

import net.sf.json.JSONArray;

public class TemplateReqDTO {
	
	private String ciid;
	private String ciname;
	private JSONArray conditions;
	private String domain;
	private String select_list;
	private int select_cnt;
	private String tmid;
	private String ci_remarks;
	
	public TemplateReqDTO() {
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

	public JSONArray getConditions() {
		return conditions;
	}

	public void setConditions(JSONArray conditions) {
		this.conditions = conditions;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSelect_list() {
		return select_list;
	}

	public void setSelect_list(String select_list) {
		this.select_list = select_list;
	}

	public int getSelect_cnt() {
		return select_cnt;
	}

	public void setSelect_cnt(int select_cnt) {
		this.select_cnt = select_cnt;
	}

	public String getTmid() {
		return tmid;
	}

	public void setTmid(String tmid) {
		this.tmid = tmid;
	}

	public String getCi_remarks() {
		return ci_remarks;
	}

	public void setCi_remarks(String ci_remarks) {
		this.ci_remarks = ci_remarks;
	}
	
	@Override
	public String toString() {
		return "TemplateReqDTO [ciid=" + ciid + ", ciname=" + ciname + ", conditions=" 
				+ conditions.toString() + ", domain=" + domain + ", select_list" 
				+ select_list + ", select_cnt" + select_cnt + ", ci_remakrs" + ci_remarks + "]";
	}
}
