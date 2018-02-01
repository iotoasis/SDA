package com.pineone.icbms.sda.kb.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.jena.rdf.model.Statement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContentInstanceMapper;
import com.pineone.icbms.sda.kb.model.TripleMap;

/**
 *   ContentInstance정보를 담고 있는  DTO
 */
public class OneM2MContentInstanceDTO implements OneM2MDTO {
	private id_object _id; // not standard  
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

	public String getCon() {
		return this.con;
	}

	
	public String getCon(int encoded){
		String ret = "error : encode option must be 1 or 0";
		switch (encoded) {
		case 1 :
			byte[] encodedContent = StringEscapeUtils.unescapeJava(this.con).getBytes();
			String tmp_ret = new String(Base64.decodeBase64(encodedContent));
			
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
						
			} else if(this.get_uri().contains("lwm2m_ipe/Distance")  ) {
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				LWM2MDistanceDTO lwm2mDistance = gson.fromJson(tmp_ret, LWM2MDistanceDTO.class);
				
				ret = lwm2mDistance.getContent().getValue();
			} else {
				ret = this.con;
			}
			break;
		case 0 :
			ret =  this.con;
			break;
		}
	
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

	@Override
	public void print() {

	}

	@Override
	public TripleMap<Statement> getTriples() {
		TripleMap<Statement> map = new TripleMap<Statement>();
		OneM2MContentInstanceMapper mapper = new OneM2MContentInstanceMapper(this);
		map.add(mapper.from());
		
		//gooper2
		mapper.close();
		
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