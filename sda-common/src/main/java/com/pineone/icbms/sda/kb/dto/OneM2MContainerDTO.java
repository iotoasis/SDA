package com.pineone.icbms.sda.kb.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.model.TripleMap;

/**
 *   Container정보를 담고 있는 DTO
 */
public class OneM2MContainerDTO implements OneM2MDTO {
	private id_object _id; // not standard
	private String ty=""; // resourceType;
	private String ri=""; // resourceID **;
	private String rn=""; // resourceName **;
	private String pi=""; // parentID **;
	private String[] lbl={""}; // labels **;
	private String ct=""; // creationTime **;
	private String lt=""; // lastModifiedTime;
	private String cr=""; // creator;
	private String cni=""; // currentNrOfInstances;
	private String cbs=""; // currentByteSize;
	private String _uri=""; // _uri : not standard

	private String mni=""; // maxNrOfInstances;
	private String mbs=""; // maxByteSize;
	private String mia=""; // maxInstanceAge;
	private String et=""; // expirationTime;
	private String acpi=""; // accessControlPolicyIDs;
	private String st=""; // stateTag;
	private String at=""; // announceTo;
	private String aa=""; // announcedAttribute;
	private String or="";// Ontology Reference
	
	public OneM2MContainerDTO(String ty, String ri, String rn, String pi, String[] lbl, String ct, String lt,
			String cr, String cni, String cbs) {
		this.ty = ty;
		this.ri = ri;
		this.rn = rn;
		this.pi = pi;
		this.lbl = lbl;
		this.ct = ct;
		this.lt = lt;
		this.cr = cr;
		this.cni = cni;
		this.cbs = cbs;
	}

	public id_object get_id() {
		return _id;
	}

	public void set_id(id_object _id) {
		this._id = _id;
	}

	public String get_uri() {
		return _uri;
	}

	public void set_uri(String _uri) {
		this._uri = _uri;
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

	public String[] getLbl() {
		return lbl;
	}

	public void setLbl(String[] lbl) {
		this.lbl = lbl;
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

	public String getCr() {
		return cr;
	}

	public void setCr(String cr) {
		this.cr = cr;
	}

	public String getCni() {
		return cni;
	}

	public void setCni(String cni) {
		this.cni = cni;
	}

	public String getCbs() {
		return cbs;
	}

	public void setCbs(String cbs) {
		this.cbs = cbs;
	}

	public String getMni() {
		return mni;
	}

	public void setMni(String mni) {
		this.mni = mni;
	}

	public String getMbs() {
		return mbs;
	}

	public void setMbs(String mbs) {
		this.mbs = mbs;
	}

	public String getMia() {
		return mia;
	}

	public void setMia(String mia) {
		this.mia = mia;
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

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public String getAa() {
		return aa;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}


	public String getOr() {
		return or;
	}

	public void setOr(String or) {
		this.or = or;
	}

	public String toString() {
		String result = "********************* Container **********************" + "\n _id 	: " + this.get_id()
				+ "\n ty 	: " + this.getTy() + "\n ri 	: " + this.getRi() + "\n rn 	: " + this.getRn()
				+ "\n pi 	: " + this.getPi() + "\n lbl 	: " + this.getLbl() + "\n ct 	: " + this.getCt()
				+ "\n lt 	: " + this.getLt() + "\n cr 	: " + this.getCr() + "\n cni 	: " + this.getCni()
				+ "\n cbs 	: " + this.getCbs() + "\n uri 	: " + this.get_uri() + "\n mni 	: " + this.getMni()
				+ "\n mbs 	: " + this.getMbs() + "\n mia 	: " + this.getMia() + "\n et 	: " + this.getEt()
				+ "\n acpi 	: " + this.getAcpi() + "\n st 	: " + this.getSt() + "\n at 	: " + this.getAt()
				+ "\n aa 	" + ": " + this.getAa() +
				 "\n or 	" + ": " + this.getOr() +"\n ********************* Container **********************";
		return result;
	}

	@Override
	public void print() {
		System.out.println(this.toString());
	}

	@Override
	public TripleMap<Statement> getTriples() {
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