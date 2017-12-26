package com.pineone.icbms.sda.kb.dto;

//import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContentInstanceMapper;
import com.pineone.icbms.sda.kb.model.TripleMap;

public class OneM2MContentInstanceDTO implements OneM2MDTO {
//	private Object _id=""; // not standard  v
	private id_object _id; // not standard  v
	private String ty=""; // resourceType;  v
	private String ri=""; // resourceID; v
	private String rn=""; // resourceName; v
	private String pi=""; // parentID; v
	private String[] lbl={""}; // labels; v
	private String cr=""; // creator;
	private String cnf=""; // contentInfo
	private String cs=""; // contentSize;  vvvvvvvvv(integer로 들어옴)
	private String con=""; // content;    v
	private String _uri=""; // not standard
	private String ct=""; // creationTime;    v
	private String lt=""; // lastModifiedTime;   v

	private String or=""; // ontologyRef;
	private String et=""; // expirationTime; v
	private String st=""; // stateTag;    vvvvvvvvv(integer로 들어옴)
	private String[] at={""}; // announceTo;
	private String[] aa={""}; // announcedAttribute;


	public String getStringId() {
		Matcher matcher = Pattern.compile("\"([^\"]*)\"").matcher(_id.toString());
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	public id_object get_id() {
		return _id;
	}

	/*
	public Object get_id() {
		return _id;
	}


	public void set_id(Object _id) {
		this._id = _id;
	}
	*/


	public void set_id(id_object _id) {
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

/*	public String getCon() {
		int isEncoded = 0;
		if (this.getCnf().contains("text/plain:0")) {
			isEncoded = 0;
		} else if (this.getCnf().contains("application/json:1")) {
			isEncoded = 1;
		}
		if(Utils.isBase64Encoded(this.con)) {
			isEncoded = 1;
		} else {
			isEncoded = 0;
		}
		return this.getCon(isEncoded);
	}
	*/
	public String getCon() {
		return this.con;
	}

	
	public String getCon(int encoded){
		String ret = "error : encode option must be 1 or 0";
		switch (encoded) {
		case 1 :
			//byte[] encodedContent = this.con.getBytes();
			byte[] encodedContent = StringEscapeUtils.unescapeJava(this.con).getBytes();
			System.out.println("====="+this.get_uri()+"====1==before str====> "+new String(encodedContent));	
			//ret = new String(Base64.getDecoder().decode(encodedContent));
			String tmp_ret = new String(Base64.decodeBase64(encodedContent));
			
			System.out.println("====="+this.get_uri()+"====1==after str====> "+tmp_ret);
			
			//json형태의 데이타를 분석해서 필요한 값만 리턴함
			if(	this.get_uri().contains("EXDA_SmartLocker01") ||
				this.get_uri().contains("EXDA_SmartLocker02") ||
				this.get_uri().contains("EXDA_SmartLocker03") 					) {
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					SmartLockerDTO sl = gson.fromJson(tmp_ret, SmartLockerDTO.class);
					ret = sl.getEnd_date().replace(" ", "T").replace(":","").replace("-", "");
			} else if(this.get_uri().contains("EXSA_Smartband01") ||
					 	this.get_uri().contains("EXSA_Smartband02")			) {
							Gson gson = new GsonBuilder().disableHtmlEscaping().create();
							SmartBandDTO sb = gson.fromJson(tmp_ret, SmartBandDTO.class);
							ret = sb.getHeartrate();
			} else if(this.get_uri().contains("Classapp_vote")    				) {
						Gson gson = new GsonBuilder().disableHtmlEscaping().create();
						VoteDTO vote = gson.fromJson(tmp_ret, VoteDTO.class);
						if (vote.getGood() >= vote.getCold() && vote.getGood() >= vote.getHot()) { 
							ret = "good";
						} else {
							if(vote.getCold() == vote.getHot()) ret =  "good";
							else if (vote.getCold() > vote.getHot()) ret = "cold";
							else if (vote.getCold() < vote.getHot()) ret = "hot";
						}
						
			} else if(this.get_uri().contains("Dormapp_temperature") 		) {
						Gson gson = new GsonBuilder().disableHtmlEscaping().create();
						DomappTempDTO dt = gson.fromJson(tmp_ret, DomappTempDTO.class);
						int avg = 0; 
						int sum = 0;
						int cnt  = 0;
						for(int m = 0; m < dt.student.length; m++) {
							if(dt.student[m].getInhouse().equals("Y")) {
								sum += dt.student[m].getTemperature();
								cnt++;
							}
						}
						if(cnt > 0) {
							avg = sum / cnt;
							ret =  String.valueOf(avg);
						} else {
							for(int m = 0; m < dt.student.length; m++) {
								sum += dt.student[m].getTemperature();
								cnt++;
							}
							avg = sum / cnt;
							ret =  String.valueOf(avg);
						}
			/*} else if(this.get_uri().contains("Dormapp_inhouse") 		) {
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				DomappTemp dt = gson.fromJson(tmp_ret, DomappTemp.class);
				int inCnt  = 0;
				int outCnt = 0;
				for(int m = 0; m < dt.student.length; m++) {
					if(dt.student[m].getInhouse().equals("Y")) {
						inCnt++;
					} else if(dt.student[m].getInhouse().equals("N")) {
						outCnt++;
					}
				}
				if(dt.student != null && dt.student.length != 0) {
					if(inCnt > 0)
						ret = "Y";
					else 
						ret = "N";
				} else {
					ret =  "N";
				}
				*/
						
						
			} else {
				ret = this.con;
			}
			break;
		case 0 :
			System.out.println("====="+this.get_uri()+"====0==before str====> "+this.con);
			ret =  this.con;
			break;
		}
		System.out.println("===="+this.get_uri()+"======after str(final)====> "+ret);
		
		return ret;
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
		String result = "********************* ContentInstance **********************" + "\n _id 	: " 
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
//		String sample = "{     \"_id\" : ObjectId(\"560c9d741ee8203c53a63569\"),     \"rn\" : \"CONTENT_INST_5\",     \"ty\" : 4,     \"ri\" : \"CONTENT_INST_5\",     \"pi\" : \"CONTAINER_37\",     \"lbl\" : [          \"cnt-switch\"     ],     \"cr\" : \"C_AE-D-GASLOCK1004\",     \"cnf\" : \"text/plain:0\",     \"cs\" : 3,     \"con\" : \"Off\",     \"_uri\" : \"/herit-in/herit-cse/ae-gaslock1004/cnt-switch/CONTENT_INST_5\",     \"ct\" : \"20151001T114156\",     \"lt\" : \"20151001T114156\" , \"or\":\"http://www.iotoasis.org/ontology/StateCondition\" }";
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
//				"	    \"cnf\" : \"text/plain:0\","+
"	    \"cnf\" : \"application/json:1\","+
				"	    \"cs\" : 2,"+
//				"	    \"con\" : \"13\","+
"	    \"con\" : \"eyJib3hfbm8iOiIxMTEifQ==\","+
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
		
		// gooper
		if(! model.isClosed()) {
			model.close();
		}
		if(model != null) {
			model = null;
		}

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

	class id_object {
		String _time;
		String _machine;
		String _inc;
		String _new;
		public String get_time() {
			return _time;
		}
		public void set_time(String _time) {
			this._time = _time;
		}
		public String get_machine() {
			return _machine;
		}
		public void set_machine(String _machine) {
			this._machine = _machine;
		}
		public String get_inc() {
			return _inc;
		}
		public void set_inc(String _inc) {
			this._inc = _inc;
		}
		public String get_new() {
			return _new;
		}
		public void set_new(String _new) {
			this._new = _new;
		}
		@Override
		public String toString() {
			return "id_object [_time=" + _time + ", _machine=" + _machine
					+ ", _inc=" + _inc + ", _new=" + _new + "]";
		}
	}