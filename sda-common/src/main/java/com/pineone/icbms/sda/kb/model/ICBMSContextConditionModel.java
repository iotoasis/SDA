package com.pineone.icbms.sda.kb.model;

/**
 *   Context Condtion의 Model클래스
 */
public class ICBMSContextConditionModel {

	private String subject;
	private String predicate;
	private String object;

	public ICBMSContextConditionModel(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
}