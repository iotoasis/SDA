package com.pineone.icbms.sda.comm.dto;

import java.util.Arrays;

public class RequestDTO {

	private String cmid;
	private String ciid;
	private String name;
	private ConditionDTO[] conditions;
	private String execution_type;		//subscribe , test, schedule
	private String schedule;
	private String domain;
	private String remarks;
    
    public RequestDTO() {
		super();
	}

	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	public String getCiid() {
		return ciid;
	}

	public void setCiid(String ciid) {
		this.ciid = ciid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConditionDTO[] getConditions() {
		return conditions;
	}

	public void setConditions(ConditionDTO[] conditions) {
		this.conditions = conditions;
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "RequestDTO [cmid=" + cmid + ", ciid=" + ciid + ", name=" + name + ", conditions="
				+ Arrays.toString(conditions) + ", execution_type=" + execution_type + ", schedule=" + schedule
				+ ", domain=" + domain + ", remarks=" + remarks + "]";
	}
}