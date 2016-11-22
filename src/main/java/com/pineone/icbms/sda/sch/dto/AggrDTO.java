package com.pineone.icbms.sda.sch.dto;

public class AggrDTO {

	private String aggr_id;
	private String aggr_name;
	private String aggrql;
	private String argsql;
	private String updateql;
	private String deleteql;
	private String insertql;;
	private String remarks;
	private String use_yn;	
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
	
    public AggrDTO() {
		super();
	}

	public String getAggr_id() {
		return aggr_id;
	}

	public void setAggr_id(String aggr_id) {
		this.aggr_id = aggr_id;
	}

	public String getAggr_name() {
		return aggr_name;
	}

	public void setAggr_name(String aggr_name) {
		this.aggr_name = aggr_name;
	}

	public String getAggrql() {
		return aggrql;
	}

	public void setAggrql(String aggrql) {
		this.aggrql = aggrql;
	}

	public String getArgsql() {
		return argsql;
	}

	public void setArgsql(String argsql) {
		this.argsql = argsql;
	}

	public String getUpdateql() {
		return updateql;
	}

	public void setUpdateql(String updateql) {
		this.updateql = updateql;
	}

	public String getDeleteql() {
		return deleteql;
	}

	public void setDeleteql(String deleteql) {
		this.deleteql = deleteql;
	}

	public String getInsertql() {
		return insertql;
	}

	public void setInsertql(String insertql) {
		this.insertql = insertql;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
		return "AggrDTO [aggr_id=" + aggr_id + ", aggr_name=" + aggr_name + ", aggrql=" + aggrql + ", argsql=" + argsql
				+ ", updateql=" + updateql + ", deleteql=" + deleteql + ", insertql=" + insertql + ", remarks="
				+ remarks + ", use_yn=" + use_yn + ", cuser=" + cuser + ", cdate=" + cdate + ", uuser=" + uuser
				+ ", udate=" + udate + "]";
	}

}
