package com.pineone.icbms.sda.subscribe.dto;

public class SubscribeDTO {

	private String cmid;
	private String ciid;
	private String uri;
	private String notification_uri;
	private String subscribe_time;
	private String use_yn;	
	private String cuser;
	private String cdate;	
	private String uuser;
	private String udate;
	
    public SubscribeDTO() {
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

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNotification_uri() {
		return notification_uri;
	}

	public void setNotification_uri(String notification_uri) {
		this.notification_uri = notification_uri;
	}

	public String getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
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
		return "SubscribeDTO [cmid=" + cmid + ", ciid=" + ciid + ", uri=" + uri + ", notification_uri="
				+ notification_uri + ", subscribe_time=" + subscribe_time + ", use_yn=" + use_yn + ", cuser=" + cuser
				+ ", cdate=" + cdate + ", uuser=" + uuser + ", udate=" + udate + "]";
	}
}