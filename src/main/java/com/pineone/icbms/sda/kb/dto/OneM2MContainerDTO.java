package com.pineone.icbms.sda.kb.dto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.google.gson.Gson;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContainerMapper;
import com.pineone.icbms.sda.kb.model.TripleMap;

public class OneM2MContainerDTO implements OneM2MDTO {
	private Object _id; // not standard
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

	public String get_uri() {
		return _uri;
	}

	public void set_uri(String _uri) {
		this._uri = _uri;
	}

	public Object get_id() {
		return _id;
	}

	public void set_id(String _id) {
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

	public static void main(String[] args) {
		String sample = "{     \"_id\" : ObjectId(\"561f27831ee8202c5e307d37\"),     \"rn\" : \"CONTAINER_268\",     \"ty\" : 3,     \"ri\" : \"CONTAINER_268\",     \"pi\" : \"SAE_0\",     \"lbl\" : [          \"switch\",          \"key1\",          \"key2\"     ],     \"et\" : \"20151203T122321\",     \"cr\" : \"//onem2m.herit.net/herit-cse/SAE_5\",     \"mni\" : 100,     \"mbs\" : 1.024e+006,     \"mia\" : 36000,     \"cni\" : 1,     \"cbs\" : 2,     \"_uri\" : \"/herit-in/herit-cse/SAE_0/CONTAINER_268\",     \"ct\" : \"20151015T131147\",     \"lt\" : \"20151015T131147\", \"or\":\"http://www.pineone.com/m2m/SwitchStatusSensor\" }";
		Gson gson = new Gson();
		OneM2MContainerDTO cont = gson.fromJson(sample, OneM2MContainerDTO.class);


		System.out.println(cont);
		
		OneM2MContainerMapper mapper = new OneM2MContainerMapper(cont);
		Model model = ModelFactory.createDefaultModel();
		model.add(mapper.from());
		
		//스트링 변환부분
		RDFDataMgr.write(System.out, model, RDFFormat.NTRIPLES);

		//스트링 변환부분
//		String serviceURI = "http://219.248.137.7:13030/icbms";
//
//		DatasetAccessor	accessor = DatasetAccessorFactory.createHTTP(serviceURI);
//		accessor.deleteDefault();
//		accessor.add(model);
//		
//		
//		QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI	,"select * {?s ?p ?o}"	);
//		ResultSet rs = q.execSelect();
//		ResultSetFormatter.out(rs);;
		
//		model = DatasetAccessorFactory.createHTTP(serviceURI).getModel();
//		System.out.println(model.size());

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
	public String getStringId() {
		Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(_id.toString());
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

}
