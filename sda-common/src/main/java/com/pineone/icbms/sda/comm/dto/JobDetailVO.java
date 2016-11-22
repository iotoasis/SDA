package com.pineone.icbms.sda.comm.dto;

public class JobDetailVO {
	private String batchName;
	private String expression;
	
	public JobDetailVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JobDetailVO(String batchName, String expression) {
		super();
		this.batchName = batchName;
		this.expression = expression;
	}
	
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getExpression() {
		return expression;
	} 
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return "JobDetailVO [batchName=" + batchName + ", expression=" + expression + "]";
	}
	
}
