package com.pineone.icbms.sda.sf.dto;

public class AwareHistDTO {

	private String aware_group_id;
	private String cmid;
	private String ciid;
	private String start_time;
	private String finish_time;
	private String work_result;
	
	private String use_yn;
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
    
    public AwareHistDTO() {
		super();
	}

    
	public String getAware_group_id() {
		return aware_group_id;
	}

	public void setAware_group_id(String aware_group_id) {
		this.aware_group_id = aware_group_id;
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

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
	}

	public String getWork_result() {
		return work_result;
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
		return "AwareHistDTO [aware_group_id=" + aware_group_id + ", cmid="
				+ cmid + ", ciid=" + ciid + ", start_time=" + start_time
				+ ", finish_time=" + finish_time + ", work_result="
				+ work_result + ", use_yn=" + use_yn + ", cuser=" + cuser
				+ ", cdate=" + cdate + ", uuser=" + uuser + ", udate=" + udate
				+ "]";
	}



}