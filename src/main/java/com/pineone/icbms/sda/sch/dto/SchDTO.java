package com.pineone.icbms.sda.sch.dto;

public class SchDTO {

	private String task_group_id;
	private String task_id;
	private String task_class;
	private String task_expression;
	private String last_work_time;
	private String use_yn;	
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
	
    public SchDTO() {
		super();
	}

	public String getTask_group_id() {
		return task_group_id;
	}

	public void setTask_group_id(String task_group_id) {
		this.task_group_id = task_group_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_class() {
		return task_class;
	}

	public void setTask_class(String task_class) {
		this.task_class = task_class;
	}

	public String getTask_expression() {
		return task_expression;
	}

	public void setTask_expression(String task_expression) {
		this.task_expression = task_expression;
	}

	public String getLast_work_time() {
		return last_work_time;
	}

	public void setLast_work_time(String last_work_time) {
		this.last_work_time = last_work_time;
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
		return "SchDTO [task_group_id=" + task_group_id + ", task_id=" + task_id + ", task_class=" + task_class
				+ ", task_expression=" + task_expression + ", last_work_time=" + last_work_time + ", use_yn=" + use_yn
				+ ", cuser=" + cuser + ", cdate=" + cdate + ", uuser=" + uuser + ", udate=" + udate + "]";
	}
}
