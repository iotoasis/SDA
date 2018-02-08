package com.pineone.icbms.sda.itf.cm.dto;

/**
 * CM 요청 정보를 담는 DTO
 */
public class CmReqDTO {
	private String cmid;
	private String ciid;
	private String cmname;
	private String execution_type;
	private String cm_remarks;
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
	public String getCm_remarks() {
		return cm_remarks;
	}
	public void setCm_remarks(String cm_remarks) {
		this.cm_remarks = cm_remarks;
	}
	
	@Override
	public String toString() {
		return "CmReqDTO [cmid=" + cmid + ", ciid=" + ciid + ", cmname=" + cmname + ", execution_type=" + execution_type
				+ ", cm_remarks=" + cm_remarks + "]";
	}

}
