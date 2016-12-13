package com.pineone.icbms.sda.hbase.onem2m;

public class SiData {
	String task_group_id;
	String task_id;
	String start_time;
	String col_from;
	String contents;
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
	public String getCol_from() {
		return col_from;
	}
	public void setCol_from(String col_from) {
		this.col_from = col_from;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	@Override
	public String toString() {
		return "SiData [task_group_id=" + task_group_id + ", task_id="
				+ task_id + ", start_time=" + start_time + ", col_from="
				+ col_from + ", contents=" + contents + ", getTask_group_id()="
				+ getTask_group_id() + ", getTask_id()=" + getTask_id()
				+ ", getStart_time()=" + getStart_time() + ", getCol_from()="
				+ getCol_from() + ", getContents()=" + getContents()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public SiData(String task_group_id, String task_id, String start_time,
			String col_from, String contents) {
		super();
		this.task_group_id = task_group_id;
		this.task_id = task_id;
		this.start_time = start_time;
		this.col_from = col_from;
		this.contents = contents;
	}
	
	

}
