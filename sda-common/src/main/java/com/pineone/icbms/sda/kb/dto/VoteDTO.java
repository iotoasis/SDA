package com.pineone.icbms.sda.kb.dto;

public class VoteDTO {
	String class_id;
	int hot;
	int cold;
	int good;
	int total;
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public int getHot() {
		return hot;
	}
	public void setHot(int hot) {
		this.hot = hot;
	}
	public int getCold() {
		return cold;
	}
	public void setCold(int cold) {
		this.cold = cold;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
