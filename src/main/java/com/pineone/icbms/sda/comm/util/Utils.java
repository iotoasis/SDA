package com.pineone.icbms.sda.comm.util;

import java.io.File;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.dto.ResponseMessageErr;
import com.pineone.icbms.sda.comm.exception.RemoteSIException;
import com.pineone.icbms.sda.comm.exception.RemoteSOException;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;

public class Utils {
	private static final Log log = LogFactory.getLog(Utils.class);
	
	// 날짜 형식
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
	public static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	

	public static final SimpleDateFormat MMddFormat = new SimpleDateFormat("MMdd");
	
	public static final SimpleDateFormat systimeFormat = new SimpleDateFormat("HHmm");
	public static final SimpleDateFormat sysdateFormat = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat sysdateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sysdatetimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	public static final SimpleDateFormat sysweekdayFormat = new SimpleDateFormat("u");
	
	public static final SimpleDateFormat nYearFormat = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat nMonthFormat = new SimpleDateFormat("MM");
	public static final SimpleDateFormat nDayFormat = new SimpleDateFormat("dd");
	public static final SimpleDateFormat nHourFormat = new SimpleDateFormat("HH");
	public static final SimpleDateFormat nMinuteFormat = new SimpleDateFormat("mm");
	public static final SimpleDateFormat nSecondFormat = new SimpleDateFormat("ss");
	
	// 
	public static final String NoTripleFile = "No Triple File";
	public static final String NotCheck = "Not Check";
	public static final String NotSendToSo = "Not Sent To SO";
	public static final String NoArg = "No argument or result ! "; 
	public static final String None = "None";
	public static final String Valid = "Valid";
	public static final String NotSupportingType = "Not Supporting Type";
	
	// SO 결과 리턴 구분(emergency)
	public static final String CALLBACK_EMERGENCY = "occ-emergency";
	// SO 결과 리턴 구분(schedule)
	public static final String CALLBACK_SCHEDULE = "occ-schedule";
	
	//test용
	public static final String CALLBACK_TEST = "occ-test";

	// response
	public static final String RESPONSE_CODE = "code";
	public static final String RESPONSE_MESSAGE = "message";
	public static final String RESPONSE_CONTENT = "content";

	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_ENCODING_IDNTITY = "identity";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String ENDODING_UTF8 = "UTF-8";

	public static final String NEW_LINE = "\n";
	// 정상코드
	public static final int OK_CODE = 200;
	public static final String OK_MSG = "OK";

	// 핸들링되지 않은 Exception
	public static final int UNKNOWNEXCEPTION_CODE = 400;
	public static final String UNKNOWNEXCEPTION_MSG = "UnknownException !";

	public static final  ResponseMessage makeResponseBody(Exception e) {
		ResponseMessage resultMsg = new ResponseMessage();

		// 원격지 오류는 원격지에서 보내준 메세지를 그대로 보여준다.
		if (e instanceof RemoteSOException) {
			resultMsg.setCode(((RemoteSOException) e).getCode());
			resultMsg.setMessage(((RemoteSOException) e).getMsg());
			resultMsg.setContent("");
		} else if (e instanceof RemoteSIException) {
			resultMsg.setCode(((RemoteSIException) e).getCode());
			resultMsg.setMessage(((RemoteSIException) e).getMsg());
			resultMsg.setContent("");
		} else if (e instanceof UserDefinedException) {
			resultMsg.setCode(((UserDefinedException) e).getCode());
			resultMsg.setMessage(((UserDefinedException) e).getMsg());
			resultMsg.setContent("");
		} else {
			String[] msg = e.toString().split(":");
			resultMsg.setCode(UNKNOWNEXCEPTION_CODE);
			if(msg.length >= 2) {
				resultMsg.setMessage(msg[0]);
				resultMsg.setContent(msg[1]);
			} else if(msg.length == 1){
				resultMsg.setMessage(msg[0]);
				resultMsg.setContent("");
			} else if(msg.length == 0) {
				resultMsg.setMessage("");
				resultMsg.setContent("");
			}
		}

		return resultMsg;
	}

	public static final String getSparQlHeader() {
		String sparQlHeader = ""
					+"prefix swrlb: <http://www.w3.org/2003/11/swrlb#>\n"   
					+"prefix protege: <http://protege.stanford.edu/plugins/owl/protege#>\n"    
					+"prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#>    \n"
					+"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					+"prefix dct: <http://purl.org/dc/terms/>    \n"
					+"prefix icbms: <http://www.pineone.com/campus/>\n"    
					+"prefix dc: <http://purl.org/dc/elements/1.1/>    \n"
					+"prefix j.0: <http://data.qudt.org/qudt/owl/1.0.0/text/>\n"    
					+"prefix owl: <http://www.w3.org/2002/07/owl#>    \n"
					+"prefix xsp: <http://www.owl-ontologies.com/2005/08/07/xsp.owl#>\n"
					+"prefix swrl: <http://www.w3.org/2003/11/swrl#>    \n"
					+"prefix skos: <http://www.w3.org/2004/02/skos/core#>\n"
					+"prefix DUL: <http://www.loa-cnr.it/ontologies/DUL.owl#>  \n"  
					+"prefix m2m: <http://www.pineone.com/m2m/>    \n"
					+"prefix cc: <http://creativecommons.org/ns#>    \n"
					+"prefix p1: <http://purl.org/dc/elements/1.1/#>    \n"
					+"prefix foaf: <http://xmlns.com/foaf/0.1/>    \n"
					+"prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"      
					+"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"   
					+"prefix qudt: <http://data.nasa.gov/qudt/owl/qudt#>\n"
					+"prefix quantity: <http://data.nasa.gov/qudt/owl/quantity#>\n"
					+"prefix unit: <http://data.nasa.gov/qudt/owl/unit#>\n"
					+"prefix dim: <http://data.nasa.gov/qudt/owl/dimension#>\n"
					+"prefix oecc: <http://www.oegov.org/models/common/cc#>\n";

		return sparQlHeader;
	}
	
	public static final String getHeaderForTripleFile() {
		String headerForTripleFile = ""
				+"@prefix swrlb: <http://www.w3.org/2003/11/swrlb#> . \n"
				+"@prefix protege: <http://protege.stanford.edu/plugins/owl/protege#> . \n"
				+"@prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#> . \n"
				+"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"
				+"@prefix dct: <http://purl.org/dc/terms/> . \n"
				+"@prefix icbms: <http://www.pineone.com/campus/> . \n"
				+"@prefix dc: <http://purl.org/dc/elements/1.1/> . \n"
				+"@prefix j.0: <http://data.qudt.org/qudt/owl/1.0.0/text/> . \n"
				+"@prefix owl: <http://www.w3.org/2002/07/owl#> . \n"
				+"@prefix xsp: <http://www.owl-ontologies.com/2005/08/07/xsp.owl#> . \n"
				+"@prefix swrl: <http://www.w3.org/2003/11/swrl#> . \n"
				+"@prefix skos: <http://www.w3.org/2004/02/skos/core#> . \n"
				+"@prefix DUL: <http://www.loa-cnr.it/ontologies/DUL.owl#> . \n"
				+"@prefix m2m: <http://www.pineone.com/m2m/> . \n"
				+"@prefix cc: <http://creativecommons.org/ns#> . \n"
				+"@prefix p1: <http://purl.org/dc/elements/1.1/#> . \n"
				+"@prefix foaf: <http://xmlns.com/foaf/0.1/> . \n"
				+"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> . \n"
				+"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . \n"
				+"@prefix qudt: <http://data.nasa.gov/qudt/owl/qudt#> . \n"
				+"@prefix quantity: <http://data.nasa.gov/qudt/owl/quantity> . \n"
				+"@prefix unit: <http://data.nasa.gov/qudt/owl/unit#> . \n"
				+"@prefix dim: <http://data.nasa.gov/qudt/owl/dimension#> . \n";
		
		return headerForTripleFile;
	}

	public static final String getSdaProperty(String envName) {
		return SdaConstant.sdaVariables.get(envName);
	}
	
	/*
	public static String getSdaProperty(String envName){
		String sda_home = System.getenv("SDA_HOME");
		String getValue = "";

		// 강제로 설정함
		if (sda_home == null) {
			sda_home = "/svc/apps/sda/webapps/sda";
		}

		// Configuration config = ConfigurationFactory
		//		.createConfiguration(sda_home + "/WEB-INF/classes/conf/system.properties");
		//	return config.getStringProperty(envName);

		try {
			if(properties == null) {
				properties.load(new FileInputStream(sda_home + "/WEB-INF/classes/conf/system.properties"));
			}
			
			getValue = properties.getProperty(envName); 
		} catch (Exception e) {
			e.printStackTrace();
			getValue = null;
		}
		return getValue;
	}
	*/

	// POST로 요청
	public static final ResponseMessage requestData(String uri, String data) throws Exception {
		HttpResponse response = null;
		ResponseMessage responseMessage = new ResponseMessage();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(uri);
			post.setHeader(ACCEPT_ENCODING, ACCEPT_ENCODING_IDNTITY);
			post.setHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);

			HttpEntity entity = new ByteArrayEntity(data.getBytes(ENDODING_UTF8));
			post.setEntity(entity);
			response = client.execute(post);
			log.debug("Response from Server(POST) ====response====> [" + response + "]");
			log.debug("Response from Server(POST) ====response.getStatusLine().getStatusCode() ====> ["
					+ response.getStatusLine().getStatusCode() + "]");
			log.debug("Response from Server(POST) ====response.getStatusLine().getReasonPhrase() ====> ["
					+ response.getStatusLine().getReasonPhrase() + "]");

			responseMessage.setCode(response.getStatusLine().getStatusCode());
			responseMessage.setMessage(response.getStatusLine().getReasonPhrase());
			responseMessage.setContent(EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return responseMessage;
	}

	// GET으로 요청
	public static final ResponseMessage requestData(String uri) throws Exception {
		HttpResponse response = null;
		ResponseMessage responseMessage = new ResponseMessage();

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(uri);
			get.setHeader(ACCEPT_ENCODING, ACCEPT_ENCODING_IDNTITY);
			get.setHeader(CONTENT_TYPE, CONTENT_TYPE_JSON);

			response = client.execute(get);
			log.debug("Response from Server(GET) ====response====> [" + response + "]");
			log.debug("Response from Server(GET) ====response.getStatusLine().getStatusCode() ====> ["
					+ response.getStatusLine().getStatusCode() + "]");
			log.debug("Response from Server(GET) ====response.getStatusLine().getReasonPhrase() ====> ["
					+ response.getStatusLine().getReasonPhrase() + "]");

			responseMessage.setCode(response.getStatusLine().getStatusCode());
			responseMessage.setMessage(response.getStatusLine().getReasonPhrase());
			responseMessage.setContent(EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return responseMessage;
	}

	// uri로부터 부모 객체를 식별
	public static final String getParentURI(String inputUri) {
		if (inputUri.contains("/")) {
			return inputUri.substring(0,inputUri.lastIndexOf("/"));
		} else {
			return "";
		}
	}

	// 데이터 초기화
	public static final void deleteTripleAll() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		accessor.deleteDefault();
	}

	public static final void getTripleCount() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");

		String queryString = "select  (count(?s) as ?count) where {?s ?p ?o }";
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, queryString);

		ResultSet rs = queryExec.execSelect();

		// 값을 console에 출력함
		ResultSetFormatter.out(rs);
	}

	public static final void getTripleAll() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");

		String queryString = "select ?s ?p ?o {?s ?p ?o}";
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, queryString);

		ResultSet rs = queryExec.execSelect();
		ResultSetFormatter.out(rs);
	}

	public static final String[] runShell(StringBuilder args) throws Exception {
		Process process = null;
		boolean notTimeOver = true;
		String[] result = new String[] { "", "" };
		ProcessOutputThread stdMsgT=null;
		ProcessOutputThread errMsgT=null;

		// OS 종류 확인
		String osName = System.getProperty("os.name");

		try {
			String[] cmd = null;
			if (osName.toLowerCase().startsWith("window")) {
				// cmd = new String[] { "cmd.exe", "/y", "/c", sb.toString() };
			} else {
				cmd = new String[] { "/bin/sh", "-c", args.toString() };
			}
			// 콘솔 명령 실행
			process = Runtime.getRuntime().exec(cmd);

			// 실행 결과 확인 (에러)
			StringBuffer stdMsg = new StringBuffer();
			// 스레드로 inputStream 버퍼 비우기
			stdMsgT = new ProcessOutputThread(process.getInputStream(), stdMsg);
			stdMsgT.start();

			StringBuffer errMsg = new StringBuffer();
			// 스레드로 errorStream 버퍼 비우기
			errMsgT = new ProcessOutputThread(process.getErrorStream(), errMsg);
			errMsgT.start();

			// 수행종료시까지 대기
			while(true) {
				if(! stdMsgT.isAlive() && ! errMsgT.isAlive()) {
					notTimeOver = process.waitFor(30L, TimeUnit.MINUTES);
					log.debug("Thread stdMsgT Status : "+stdMsgT.getState());
			        log.debug("Thread errMsgT Status : "+errMsgT.getState());

					break;
				}
			}
			log.debug("notTimeOver ==========================>" + notTimeOver);

			// 실행결과
			result[0] = stdMsg.toString().trim();
			result[1] = errMsg.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (process != null) {
				IOUtils.closeQuietly(process.getOutputStream());
				IOUtils.closeQuietly(process.getInputStream());
				IOUtils.closeQuietly(process.getErrorStream());
				
				process.destroy();
				log.debug("process destoryed ==========================>");
			}
		}

		args.delete(0, args.length());
		args.setLength(0);
		args = null;

		return result;
	}
	
	// 폴더를 만듬(폴더끝에 년월일을 붙임)
	public static final String makeSavePath(String save_path) {
		save_path = save_path + "/" + Utils.sysdateFormat.format(new Date());
		File desti = new File(save_path);
		if (!desti.exists()) {
			desti.mkdirs();
		} else {
			// pass
		}
		return save_path;
	}
	
	// 날짜계산(오늘+- iDay)
	public static final String getDate ( int iDay ) 
	{
	    Calendar temp=Calendar.getInstance ( );    
	    temp.add ( Calendar.DAY_OF_MONTH, iDay );
	     
	    int nYear = temp.get ( Calendar.YEAR );
	    int nMonth = temp.get ( Calendar.MONTH ) + 1;
	    int nDay = temp.get ( Calendar.DAY_OF_MONTH );
	     
	    StringBuffer sbDate=new StringBuffer ( );
	    sbDate.append ( nYear );
	     
	    if ( nMonth < 10 ) 
	        sbDate.append ( "0" );
	        sbDate.append ( nMonth );
	 
	    if ( nDay < 10 ) 
	        sbDate.append ( "0" );
	        sbDate.append ( nDay );
	 
	    return sbDate.toString ( );
	}


	// test수행
	public static void main(String[] args) throws Exception {

		// Gson gson = new Gson();
		// // List<Map<String, String>> list = new ArrayList<Map<String,
		// String>>();
		//
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("_uri", "csebase/SAE_0021");
		// map.put("_notificationUri", "http://si.herit.net:9090/noti2");
		//
		// //list.add(map);
		//
		// String jsonMsg = gson.toJson(map);
		// log.debug("Request message => "+jsonMsg);
		//
		// //String response = Utils.requestData(SUBSCRIPTION_URL, jsonMsg); //
		// POST
		// String response = Utils.requestData(SUBSCRIPTION_URL); // GET
		// ResponseMessage responseMessage = getMessageFromResponse(response);
		//
		// log.debug("result of responseMessage :
		// "+responseMessage.toString());

		String test = "/herit-in/herit-cse/ae-gaslock010";
		log.debug(test);
		log.debug(getParentURI(test));
	}

}
