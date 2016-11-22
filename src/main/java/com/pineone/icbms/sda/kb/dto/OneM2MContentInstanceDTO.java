package com.pineone.icbms.sda.kb.dto;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.atlas.logging.Log;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.google.gson.Gson;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContainerMapper;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContentInstanceMapper;
import com.pineone.icbms.sda.kb.model.TripleMap;

public class OneM2MContentInstanceDTO implements OneM2MDTO {
	private Object _id=""; // not standard
	private String ty=""; // resourceType;
	private String ri=""; // resourceID;
	private String rn=""; // resourceName;
	private String pi=""; // parentID;
	private String[] lbl={""}; // labels;
	private String cr=""; // creator;
	private String cnf=""; // contentInfo
	private String cs=""; // contentSize;
	private String con=""; // content;
	private String _uri=""; // not standard
	private String ct=""; // creationTime;
	private String lt=""; // lastModifiedTime;

	private String or=""; // ontologyRef;
	private String et=""; // expirationTime;
	private String st=""; // stateTag;
	private String[] at={""}; // announceTo;
	private String[] aa={""}; // announcedAttribute;

	public Object get_id() {
		return _id;
	}

	public String getStringId() {
		Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(_id.toString());
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
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

	public String getCr() {
		return cr;
	}

	public void setCr(String cr) {
		this.cr = cr;
	}

	public String getCnf() {
		return cnf;
	}

	public void setCnf(String cnf) {
		this.cnf = cnf;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getCon() {
		return con;
	}
	
	public String getCon(int encoded){
		switch (encoded) {
		case 1 :
			byte[] encodedContent = this.getCon().getBytes();
			return Base64.getDecoder().decode(encodedContent).toString();
		case 0 :
			return this.getCon();
		}
		return "error : encode option must 1 or 0";
	}

	public void setCon(String con) {
		this.con = con;
	}

	public String get_uri() {
		return _uri;
	}

	public void set_uri(String _uri) {
		this._uri = _uri;
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

	public String getOr() {
		return or;
	}

	public void setOr(String or) {
		this.or = or;
	}

	public String getEt() {
		return et;
	}

	public void setEt(String et) {
		this.et = et;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
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

	public String toString() {
		String result = "********************* ContentInstance **********************" + "\n _id 	: " + this.get_id()
				+ "\n ty 	: " + this.getTy() + "\n ri 	: " + this.getRi() + "\n rn 	: " + this.getRn()
				+ "\n pi 	: " + this.getPi() + "\n lbl 	: " + this.getLbl() + "\n ct 	: " + this.getCt()
				+ "\n lt 	: " + this.getLt() + "\n cr 	: " + this.getCr() + "\n cnf 	: " + this.getCnf()
				+ "\n cs 	: " + this.getCs() + "\n con 	: " + this.getCon() + "\n uri 	: " + this.get_uri()
				+ "\n ct 	: " + this.getCt() + "\n lt 	: " + this.getLt() + "\n or 	: " + this.getOr()
				+ "\n et 	: " + this.getEt() + "\n st 	: " + this.getSt() + "\n at 	: " + this.getAt()
				+ "\n aa 	: " + this.getAa() + "\n stringId 	: " + this.getStringId()
				+ "\n ********************* Content **********************";
		return result;
	}

	public static void main(String[] args) {
//		String sample = "{     \"_id\" : ObjectId(\"560c9d741ee8203c53a63569\"),     \"rn\" : \"CONTENT_INST_5\",     \"ty\" : 4,     \"ri\" : \"CONTENT_INST_5\",     \"pi\" : \"CONTAINER_37\",     \"lbl\" : [          \"cnt-switch\"     ],     \"cr\" : \"C_AE-D-GASLOCK1004\",     \"cnf\" : \"text/plain:0\",     \"cs\" : 3,     \"con\" : \"Off\",     \"_uri\" : \"/herit-in/herit-cse/ae-gaslock1004/cnt-switch/CONTENT_INST_5\",     \"ct\" : \"20151001T114156\",     \"lt\" : \"20151001T114156\" , \"or\":\"http://www.pineone.com/campus/StateCondition\" }";
		String sample = 
				"{ "+
				"	    \"_id\" : ObjectId(\"560c9b1e1ee8203c53a63554\"),"+
				"	    \"rn\" : \"CONTENT_INST_0\","+
				"	    \"ty\" : 4,"+
				"	    \"ri\" : \"CONTENT_INST_0\","+
				"	    \"pi\" : \"CONTAINER_15\","+
				"	    \"lbl\" : [ "+
				"	        \"cnt-temperature\""+
				"	    ],"+
				"	    \"cr\" : \"C_AE-D-GASLOCK1001\","+
				"	    \"cnf\" : \"text/plain:0\","+
				"	    \"cs\" : 2,"+
				"	    \"con\" : \"13\","+
				"	    \"_uri\" : \"/herit-in/herit-cse/ae-gaslock1001/cnt-temperature/CONTENT_INST_0\","+
				"	    \"ct\" : \"20151001T113158\","+
				"	    \"lt\" : \"20151001T113158\" "+
				"	} " ;
		Gson gson = new Gson();
		OneM2MContentInstanceDTO cont = gson.fromJson(sample, OneM2MContentInstanceDTO.class);
		System.out.println(cont);


		OneM2MContentInstanceMapper mapper = new OneM2MContentInstanceMapper(cont);
		mapper = new OneM2MContentInstanceMapper(cont);
		Model model = ModelFactory.createDefaultModel();
		model.add(mapper.from());

		// 스트링 변환부분
		RDFDataMgr.write(System.out, model, RDFFormat.NTRIPLES);
	}

	@Override
	public void print() {

	}

	@Override
	public TripleMap<Statement> getTriples() {
		TripleMap<Statement> map = new TripleMap<Statement>();
		OneM2MContentInstanceMapper mapper = new OneM2MContentInstanceMapper(this);
		map.add(mapper.from());
		return map;

	}
}
