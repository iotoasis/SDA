package com.pineone.icbms.sda.kb.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.model.TripleMap;

/**
 *   OneM2M의  AE 정보를 담는 DTO
 */
public class OneM2MAEDTO implements OneM2MDTO{

	private Object _id="";	// not standard;
	private String ty=""; 	// resourceType;
	private String ri="";	// resourceID;
	private String rn=""; 	// resourceName;
	private String pi="";	//parentID;
	private String et=""; //expirationTime
	private String acpi="";	// accessControlPolicyIDs;
	private String ct="";	//creationTime;
	private String lt="";	//lastModifiedTime;
	private String[] lbl={""};	//labels;
	private String[] at={""};	//announceTo;
	private String[] aa={""};	//announceAttribute;
	private String apn="";	//appName;
	private String api="";	//appID;
	private String aei="";	//aeID;
	private String[] poa={""};	//pointOfAddress;
	private String or="";	//ontologyRef;
	private String _uri="";	//not standard
	private String nl="";		//nodeLink;
	
	

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
	public String getPi() {
		return pi;
	}
	public void setPi(String pi) {
		this.pi = pi;
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
	public String[] getLbl() {
		return lbl;
	}
	public void setLbl(String[] lbl) {
		this.lbl = lbl;
	}
	public String[] getAt() {
		return at;
	}
	public void setAt(String[] at) {
		this.at = at;
	}
	public String[] getAa() {
		return aa;
	}
	public void setAa(String[] aa) {
		this.aa = aa;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getAei() {
		return aei;
	}
	public void setAei(String aei) {
		this.aei = aei;
	}
	public String[] getPoa() {
		return poa;
	}
	public void setPoa(String[] poa) {
		this.poa = poa;
	}
	public String getOr() {
		return or;
	}
	public void setOr(String or) {
		this.or = or;
	}
	public String get_uri() {
		return _uri;
	}
	public void set_uri(String _uri) {
		this._uri = _uri;
	}
	public String getNl() {
		return nl;
	}
	public void setNl(String nl) {
		this.nl = nl;
	}
	@Override
	public void print() {
		System.out.println(this.toString());
	}
	@Override
	public TripleMap<Statement> getTriples() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public String getStringId(){
        Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(_id.toString());
        while (matcher.find()) {
        	return matcher.group(1);
        } 
        return "";
	}
}
