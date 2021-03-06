package com.pineone.icbms.sda.itf.cmi.dto;

/**
 * CMI정보를 담는 DTO
 */
public class CmiDTO {

	private String tnsda_context_model_cmid;
	private String tnsda_context_info_ciid;
	private int ci_seq;
    
    public int getCi_seq() {
		return ci_seq;
	}

	public void setCi_seq(int ci_seq) {
		this.ci_seq = ci_seq;
	}

	public CmiDTO() {
		super();
	}

	public String getTnsda_context_model_cmid() {
		return tnsda_context_model_cmid;
	}

	public void setTnsda_context_model_cmid(String tnsda_context_model_cmid) {
		this.tnsda_context_model_cmid = tnsda_context_model_cmid;
	}

	public String getTnsda_context_info_ciid() {
		return tnsda_context_info_ciid;
	}

	public void setTnsda_context_info_ciid(String tnsda_context_info_ciid) {
		this.tnsda_context_info_ciid = tnsda_context_info_ciid;
	}
}