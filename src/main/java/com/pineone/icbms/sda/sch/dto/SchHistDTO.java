package com.pineone.icbms.sda.sch.dto;

public class SchHistDTO {

	private String task_group_id;
	private String task_id;
	private String start_time;
	private String finish_time;
	private String task_class;
	private String task_expression;
	private int work_cnt;
	private String work_time;
	private String work_result;
	
	private String triple_file_name;
	private String triple_check_result;
	
	private String use_yn;
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
    
    public SchHistDTO() {
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

	public int getWork_cnt() {
		return work_cnt;
	}

	public void setWork_cnt(int work_cnt) {
		this.work_cnt = work_cnt;
	}

	public String getWork_time() {
		return work_time;
	}

	public void setWork_time(String work_time) {
		this.work_time = work_time;
	}

	public String getWork_result() {
		return work_result;
	}

	public void setWork_result(String work_result) {
		this.work_result = work_result;
	}

	public String getTriple_file_name() {
		return triple_file_name;
	}

	public void setTriple_file_name(String triple_file_name) {
		this.triple_file_name = triple_file_name;
	}

	public String getTriple_check_result() {
		return triple_check_result;
	}

	public void setTriple_check_result(String triple_check_result) {
		this.triple_check_result = triple_check_result;
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
		return "SchHistDTO [task_group_id=" + task_group_id + ", task_id=" + task_id + ", start_time=" + start_time
				+ ", finish_time=" + finish_time + ", task_class=" + task_class + ", task_expression=" + task_expression
				+ ", work_cnt=" + work_cnt + ", work_time=" + work_time + ", work_result=" + work_result
				+ ", triple_file_name=" + triple_file_name + ", triple_check_result=" + triple_check_result
				+ ", use_yn=" + use_yn + ", cuser=" + cuser + ", cdate=" + cdate + ", uuser=" + uuser + ", udate="
				+ udate + "]";
	}

}
