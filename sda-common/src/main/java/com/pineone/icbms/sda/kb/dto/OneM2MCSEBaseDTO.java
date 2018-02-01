package com.pineone.icbms.sda.kb.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MCSEBaseMapper;
import com.pineone.icbms.sda.kb.model.TripleMap;

/**
 *   CSEBase정보를 담는 DTO
 */
public class OneM2MCSEBaseDTO implements OneM2MDTO {

	private Object _id="";
	private	String ty=""; 	// resourceType;
	private String ri="";	// resourceID;
	private String rn=""; 	// resourceName;
	private String ct="";	//creationTime;
	private String lt="";		//lastModifiedTime;
	private String acpi=""; 	//accessControlPolicyIDs;
	private String[] lbl={""}; 	//lables;
	private String cst="";	//cseType;
	private String srt=""; //supportedResourceType;
	private String poa="";	//pointOfAcess;
	private String nl="";	//nodeLink;
	private String ncp=""; //notificationCongestionPolicy;
	private String _uri=""; // uri
	
	public String get_uri() {
		return _uri;
	}

	public void set_uri(String _uri) {
		this._uri = _uri;
	}

	public Object get_id() {
		Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(_id.toString());
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
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

	public String getCst() {
		return cst;
	}

	public void setCst(String cst) {
		this.cst = cst;
	}

	public String getSrt() {
		return srt;
	}

	public void setSrt(String srt) {
		this.srt = srt;
	}

	public String getPoa() {
		return poa;
	}

	public void setPoa(String poa) {
		this.poa = poa;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getNcp() {
		return ncp;
	}

	public void setNcp(String ncp) {
		this.ncp = ncp;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public String toString() {
		String result = "********************* ContentInstance **********************" + "\n _id 	: " + this.get_id()
				+ "\n ty 	: " + this.getTy() + "\n ri 	: " + this.getRi() + "\n rn 	: " + this.getRn()
				+ "\n ct 	: " + this.getCt() + "\n lbl 	: " + this.getLbl() 
				+ "\n lt 	: " + this.getLt() + "\n acpi 	: " + this.getAcpi() + "\n cst 	: " + this.getCst()
				+ "\n srt 	: " + this.getSrt() + "\n poa 	: " + this.getPoa() + "\n uri 	: " + this.get_uri()
				+ "\n nl 	: " + this.getNl() + "\n ncp 	: " + this.getNcp() + "\n or 	: " 
				+ "\n ********************* Content **********************";
		return result;
	}

	@Override
	public TripleMap<Statement> getTriples() {
		TripleMap<Statement> map = new TripleMap<Statement>();
		OneM2MCSEBaseMapper mapper = new OneM2MCSEBaseMapper(this);
		map.add(mapper.from());
		return map;

	}

	@Override
	public String getStringId() {
		// TODO Auto-generated method stub
		return null;
	}

}
