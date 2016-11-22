package com.pineone.icbms.sda.subscribe.dto;

public class SparqlListDTO {

	private String cmid;
	private String ciid;
	private int callback_seq;
	private String uri;
	private String notifcation_uri;
	private String sparql;
    
    public SparqlListDTO() {
		super();
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

	public int getCallback_seq() {
		return callback_seq;
	}

	public void setCallback_seq(int callback_seq) {
		this.callback_seq = callback_seq;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNotifcation_uri() {
		return notifcation_uri;
	}

	public void setNotifcation_uri(String notifcation_uri) {
		this.notifcation_uri = notifcation_uri;
	}                                                                                                                                                                                                                                                                                                                                          

	public String getSparql() {
		return sparql;
	}

	public void setSparql(String sparql) {
		this.sparql = sparql;
	}

	@Override
	public String toString() {
		return "SparqlListDTO [cmid=" + cmid + ", ciid=" + ciid + ", callback_seq=" + callback_seq + ", uri=" + uri
				+ ", notifcation_uri=" + notifcation_uri + ", sparql=" + sparql + "]";
	}



}
