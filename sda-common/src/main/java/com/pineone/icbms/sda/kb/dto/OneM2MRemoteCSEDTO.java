package com.pineone.icbms.sda.kb.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.model.TripleMap;

public class OneM2MRemoteCSEDTO implements OneM2MDTO {


	private Object _id ="";
	private	String ty=""; 	// resourceType;
	private String ri="";	// resourceID;
	private String rn=""; 	// resourceName;
	private String ct="";	//creationTime;
	private String lt="";		//lastModifiedTime;
	private String et=""; 	//expireationTime;
	
	private String acpi=""; 	//accessControlPolicyIDs;
	private String[] lbl={""}; 	//lables;
	private String announceTo="";
	private String announcedAttribute="";
	private String cst="";	//cseType;
	private String poa="";	//pointOfAcess;
	private String CSEBase=""; 
	private String CSE_ID="";
	private String M2MExtID="";
	private String TriggerRecipientID="";
	private String requestReachability="";
	private String nl="";	//nodeLink;
	private String _uri=""; // uri
	
	

	public Object get_id() {
		return _id;
	}

	public void set_id(Object _id) {
		this._id = _id;
	}

	public String getTy() {
		return ty;
	}

	public void setTy(String ty) {
		this.ty = ty;
	}

	public String getRi() {
		return ri;
	}

	public void setRi(String ri) {
		this.ri = ri;
	}

	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getLt() {
		return lt;
	}

	public void setLt(String lt) {
		this.lt = lt;
	}

	public String getEt() {
		return et;
	}

	public void setEt(String et) {
		this.et = et;
	}

	public String getAcpi() {
		return acpi;
	}

	public void setAcpi(String acpi) {
		this.acpi = acpi;
	}

	public String[] getLbl() {
		return lbl;
	}

	public void setLbl(String[] lbl) {
		this.lbl = lbl;
	}

	public String getAnnounceTo() {
		return announceTo;
	}

	public void setAnnounceTo(String announceTo) {
		this.announceTo = announceTo;
	}

	public String getAnnouncedAttribute() {
		return announcedAttribute;
	}

	public void setAnnouncedAttribute(String announcedAttribute) {
		this.announcedAttribute = announcedAttribute;
	}

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}

	public String getPoa() {
		return poa;
	}

	public void setPoa(String poa) {
		this.poa = poa;
	}

	public String getCSEBase() {
		return CSEBase;
	}

	public void setCSEBase(String cSEBase) {
		CSEBase = cSEBase;
	}

	public String getCSE_ID() {
		return CSE_ID;
	}

	public void setCSE_ID(String cSE_ID) {
		CSE_ID = cSE_ID;
	}

	public String getM2MExtID() {
		return M2MExtID;
	}

	public void setM2MExtID(String m2mExtID) {
		M2MExtID = m2mExtID;
	}

	public String getTriggerRecipientID() {
		return TriggerRecipientID;
	}

	public void setTriggerRecipientID(String triggerRecipientID) {
		TriggerRecipientID = triggerRecipientID;
	}

	public String getRequestReachability() {
		return requestReachability;
	}

	public void setRequestReachability(String requestReachability) {
		this.requestReachability = requestReachability;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String get_uri() {
		return _uri;
	}

	public void set_uri(String _uri) {
		this._uri = _uri;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public TripleMap<Statement> getTriples() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringId() {
		Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(_id.toString());
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

}
