package com.pineone.icbms.sda.comm.util;

import java.io.File;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.RemoteSIException;
import com.pineone.icbms.sda.comm.exception.RemoteSOException;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;

/**
 *   Util성 클래스 파일
 */
public class Utils {
	private static final Log log = LogFactory.getLog(Utils.class);

	private Utils() {
	}
	
	private volatile static PropertiesConfiguration conf ;
	
	// topic 
	public static enum KafkaTopics {
		COL_ONEM2M,
		COL_LWM2M,
		COL_RDBMS,
	}
	
	// 쿼리구분
	public static enum QUERY_GUBUN {
		MONGODB
		,MARIADBOFGRIB
		,MARIADBOFSDA
		,FUSEKISPARQL
		,HALYARDSPARQL
		,SHELL
	}
	
	// 쿼리 수행 목적지
	public static enum QUERY_DEST {
		DM,
		DW,
		ALL
	}
	
	// kafka broker
	public  static final String BROKER_LIST = "sda6:9092,sda7:9092,sda8:9092";
	public  static final String ZOOKEEPER_LIST = "sda6:2181,sda7:2181,sda8:2181";
	public  static final String HBASE_ZOOKEEPER_HOST = "sda6,sda7,sda8";
	public  static final String HBASE_ZOOKEEPER_PORT = "2181";
	
	// 날짜형식
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
	
	public static final String NoTripleFile = "No Triple File";
	public static final String NotCheck = "Not Check";
	public static final String NotSendToSo = "Not Sent To SO";
	public static final String NoArg = "No argument or result ! "; 
	public static final String None = "None";
	public static final String Valid = "Valid";
	public static final String DataSaved = "Data saved on Kafka Broker.";
	public static final String NotSupportingType = "Not Supporting Type";
	public static final String SchNotInit= "Scheduler is not initiated....";
	
	// 수집구분
	public static final String COL_ONEM2M_STATUS_DATA = "COL-ONEM2M-STATUS-DATA";
	public static final String COL_ONEM2M_ZONE_DATA = "COL-ONEM2M-ZONE-DATA";
	
	public static final String COL_ONEM2M_DATA = "COL-ONEM2M-DATA";
	
	public static final String COL_LWM2M_DATA = "COL-LWM2M-DATA";
	
	public static final String COL_SS_USER_DATA= "COL-SS-USER-DATA";
	public static final String COL_SS_ALARM_DATA= "COL-SS-ALARM-DATA";
	public static final String COL_SS_SURVEY_DATA= "COL-SS-SURVEY-DATA";
	
	public static final String COL_SS_DEVICEINFO_DATA= "COL-SS-DEVICEINFO-DATA";
	public static final String COL_SS_TIMETABLE_WATT_DATA= "COL-SS-TIMETABLE-WATT-DATA";
	
	// SO 결과 리턴 구분(emergency)
	public static final String CALLBACK_EMERGENCY = "occ-emergency";

	public static final String CMD = "query";
	
	// Semantic Descriptor 쿼리 경로 
	public static final String QUERY_LECTURE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/query/query_lecture.sql";
	public static final String QUERY_DEVICE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/query/query_device.sql";
	public static final String ALL_SAVE_FILE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/icbms_update_triple.ttl";
	public static final String DEVICE_SAVE_FILE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/icbms_update_device_triple.ttl";
	public static final String LECTURE_SAVE_FILE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/icbms_update_lecture_triple.ttl";
	
	public static final String UPDATE_DEVICE_SAVE_FILE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/temp_one_device/temp_";
	public static final String UPDATE_QUERY_DEVICE_PATH = "/home/pineone/svc/apps/sda/update-jena-data/query/temp_one_device/update_query_device.sql";
	public static final String UPDATE_QUERY_DEVICE_TEMP_FILE_PATH ="/home/pineone/svc/apps/sda/update-jena-data/query/temp_one_device/temp_";

	// response
	public static final String RESPONSE_CODE = "code";
	public static final String RESPONSE_MESSAGE = "message";
	public static final String RESPONSE_CONTENTS = "contents";

	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_ENCODING_IDNTITY = "identity";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String ENDODING_UTF8 = "UTF-8";

	public static final String NEW_LINE = "\n";
	public static final String SPLIT_STR = "~#";
	
	// 정상코드
	public static final int OK_CODE = 200;
	public static final String OK_MSG = "OK";
	public static final String PREF= "http://www.iotoasis.org/herit-in/herit-cse/"; 

	// 내부오류
	public static final int INTERNAL_SERVER_ERROR_CODE = 500;
	public static final String INTERNAL_SERVER_ERROR_MSG = "Internal Server Error";
	
	// 핸들링되지 않은 Exception
	public static final int UNKNOWNEXCEPTION_CODE = 400;
	public static final String UNKNOWNEXCEPTION_MSG = "UnknownException !";

	/**
	 *   응답결과 Body값 만들기
	 * @param e
	 * @return
	 */
	public static final  ResponseMessage makeResponseBody(Exception e) {
		ResponseMessage resultMsg = new ResponseMessage();
		
		log.debug("e.getCause() : "+e.getCause());

		// 원격지 오류는 원격지에서 보내준 메세지를 그대로 보여준다.
		if (e instanceof RemoteSOException) {
			resultMsg.setCode(((RemoteSOException) e).getCode());
			resultMsg.setMessage(((RemoteSOException) e).getMsg());
			resultMsg.setContents("");
		} else if (e instanceof RemoteSIException) {
			resultMsg.setCode(((RemoteSIException) e).getCode());
			resultMsg.setMessage(((RemoteSIException) e).getMsg());
			resultMsg.setContents("");
		} else if (e instanceof UserDefinedException) {
			resultMsg.setCode(((UserDefinedException) e).getCode());
			resultMsg.setMessage(((UserDefinedException) e).getMsg());
			resultMsg.setContents("");
		} else {
			String[] msg;
			if(e.getMessage() ==null || e.getMessage().equals("")) {
				resultMsg.setCode(INTERNAL_SERVER_ERROR_CODE);
				resultMsg.setMessage(INTERNAL_SERVER_ERROR_MSG);
			} else {
				msg  = e.getMessage().split(":");
				resultMsg.setCode(INTERNAL_SERVER_ERROR_CODE);
				if(msg.length >= 2) {
					resultMsg.setMessage(msg[0]);
					resultMsg.setContents(msg[1]);
				} else if(msg.length == 1){
					resultMsg.setMessage(msg[0]);
					resultMsg.setContents("");
				} else if(msg.length == 0) {
					resultMsg.setMessage("");
					resultMsg.setContents("");
				}
			}
		}

		return resultMsg;
	}

	/**
	 * sparql문장의 header
	 * @return String
	 */
	public static final String getSparQlHeader() {
		String sparQlHeader = ""
		        +"prefix swrlb: <http://www.w3.org/2003/11/swrlb#>  \n"
		        +"prefix protege: <http://protege.stanford.edu/plugins/owl/protege#> \n"
		        +"prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#> \n"
		        +"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
		        +"prefix dct: <http://purl.org/dc/terms/>  \n"
		        +"prefix dc: <http://purl.org/dc/elements/1.1/>  \n"
		        +"prefix j.0: <http://data.qudt.org/qudt/owl/1.0.0/text/>  \n"
		        +"prefix owl: <http://www.w3.org/2002/07/owl#>  \n"
		        +"prefix xsp: <http://www.owl-ontologies.com/2005/08/07/xsp.owl#>  \n"
		        +"prefix swrl: <http://www.w3.org/2003/11/swrl#>  \n"
		        +"prefix skos: <http://www.w3.org/2004/02/skos/core#>  \n"
		        +"prefix dul: <http://www.loa-cnr.it/ontologies/DUL.owl#>  \n"
		        +"prefix cc: <http://creativecommons.org/ns#>  \n"
		        +"prefix p1: <http://purl.org/dc/elements/1.1/#>  \n"
		        +"prefix foaf: <http://xmlns.com/foaf/0.1/>  \n"
		        +"prefix xsd: <http://www.w3.org/2001/XMLSchema#>  \n"
		        +"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  \n"
		        +"prefix qudt: <http://data.nasa.gov/qudt/owl/qudt#>  \n"
		        +"prefix quantity: <http://data.nasa.gov/qudt/owl/quantity>  \n"
		        +"prefix unit: <http://data.nasa.gov/qudt/owl/unit#>  \n"
		        +"prefix dim: <http://data.nasa.gov/qudt/owl/dimension#>  \n"
		        +"prefix b: <http://www.onem2m.org/ontology/Base_Ontology#>  \n"
		        +"prefix herit: <http://herit-in/herit-cse/> \n"
		        +"prefix o: <http://www.iotoasis.org/ontology/> \n";

		return sparQlHeader;
	}
	
	/**
	 * triple file 처리를 위한 header
	 * @return String
	 */
	public static final String getHeaderForTripleFile() {
	    String headerForTripleFile = ""
	        +"@prefix swrlb: <http://www.w3.org/2003/11/swrlb#> . \n"
	        +"@prefix protege: <http://protege.stanford.edu/plugins/owl/protege#> . \n"
	        +"@prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#> . \n"
	        +"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"
	        +"@prefix dct: <http://purl.org/dc/terms/> . \n"
	        +"@prefix dc: <http://purl.org/dc/elements/1.1/> . \n"
	        +"@prefix j.0: <http://data.qudt.org/qudt/owl/1.0.0/text/> . \n"
	        +"@prefix owl: <http://www.w3.org/2002/07/owl#> . \n"
	        +"@prefix xsp: <http://www.owl-ontologies.com/2005/08/07/xsp.owl#> . \n"
	        +"@prefix swrl: <http://www.w3.org/2003/11/swrl#> . \n"
	        +"@prefix skos: <http://www.w3.org/2004/02/skos/core#> . \n"
	        +"@prefix dul: <http://www.loa-cnr.it/ontologies/DUL.owl#> . \n"
	        +"@prefix cc: <http://creativecommons.org/ns#> . \n"
	        +"@prefix p1: <http://purl.org/dc/elements/1.1/#> . \n"
	        +"@prefix foaf: <http://xmlns.com/foaf/0.1/> . \n"
	        +"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> . \n"
	        +"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . \n"
	        +"@prefix qudt: <http://data.nasa.gov/qudt/owl/qudt#> . \n"
	        +"@prefix quantity: <http://data.nasa.gov/qudt/owl/quantity> . \n"
	        +"@prefix unit: <http://data.nasa.gov/qudt/owl/unit#> . \n"
	        +"@prefix dim: <http://data.nasa.gov/qudt/owl/dimension#> . \n"
	        +"@prefix b: <http://www.onem2m.org/ontology/Base_Ontology#> . \n"
	        +"@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n" 
	        +"@prefix herit: <http://herit-in/herit-cse/> . \n"
	        +"@prefix o: <http://www.iotoasis.org/ontology/> .\n";
	    return headerForTripleFile;
	  }

	/**
	 *   설정정보 읽기
	 * @param envName
	 * @return String
	 */
	public static synchronized String getSdaProperty(String envName) {
		String getValue = "";

		if(conf == null) {
			log.info("configuration file reading start ...........");
			
			try {
			    // load the configuration
				synchronized(Utils.class) {
					if(conf == null) {
					    conf = new PropertiesConfiguration("system.properties");
					    conf.load();
					}
				}
			} catch (Exception e) {
				log.debug("configuration file reading exception .......:"+e.getMessage());
				getValue = null;
			}
			
			log.info("configuration file reading end ...........");
		}
			
		getValue = conf.getString(envName);
		
		return getValue;
	}

	/**
	 *   PUT요청
	 * @param uri
	 * @param data
	 * @param headers
	 * @return ResponseMessage
	 * @throws Exception
	 */
	public static final ResponseMessage requestPut(String uri, String data, Map<String, String> headers) throws Exception {
		HttpResponse response = null;
		ResponseMessage responseMessage = new ResponseMessage();
		Iterator<String> itr = headers.keySet().iterator();

		String key = "";
		String val = "";
		
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(uri);
		try {
			while(itr.hasNext()) {
				key = (String)itr.next();
				val = (String)headers.get(key);
				
				put.setHeader(key, val);
			}

			HttpEntity entity = new ByteArrayEntity(data.getBytes(ENDODING_UTF8));
			put.setEntity(entity);
			response = client.execute(put);
			log.debug("Response from Server(PUT) ====response====> [" + response + "]");
			log.debug("Response from Server(PUT) ====response.getStatusLine().getStatusCode() ====> ["
					+ response.getStatusLine().getStatusCode() + "]");
			log.debug("Response from Server(PUT) ====response.getStatusLine().getReasonPhrase() ====> ["
					+ response.getStatusLine().getReasonPhrase() + "]");

			responseMessage.setCode(response.getStatusLine().getStatusCode());
			responseMessage.setMessage(response.getStatusLine().getReasonPhrase());
			if(response.getEntity() == null) {
				responseMessage.setContents("");
			} else {
				responseMessage.setContents(EntityUtils.toString(response.getEntity()));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();
			}
		}

		return responseMessage;		
	}
	
	/**
	 *   DELETE요청
	 * @param uri
	 * @param headers
	 * @return ResponseMessage
	 * @throws Exception
	 */
	public static final ResponseMessage requestDelete(String uri, Map<String, String> headers) throws Exception {
		HttpResponse response = null;
		ResponseMessage responseMessage = new ResponseMessage();
		Iterator<String> itr = headers.keySet().iterator();

		String key = "";
		String val = "";

		HttpClient client = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete(uri);

		try {
			while(itr.hasNext()) {
				key = (String)itr.next();
				val = (String)headers.get(key);
				
				delete.setHeader(key, val);
			}

			response = client.execute(delete);
			log.debug("Response from Server(DELETE) ====response====> [" + response + "]");
			log.debug("Response from Server(DELETE) ====response.getStatusLine().getStatusCode() ====> ["
					+ response.getStatusLine().getStatusCode() + "]");
			log.debug("Response from Server(DELETE) ====response.getStatusLine().getReasonPhrase() ====> ["
					+ response.getStatusLine().getReasonPhrase() + "]");

			responseMessage.setCode(response.getStatusLine().getStatusCode());
			responseMessage.setMessage(response.getStatusLine().getReasonPhrase());
			if(response.getEntity() == null) {
				responseMessage.setContents("");
			} else {
				responseMessage.setContents(EntityUtils.toString(response.getEntity()));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();
			}
		}

		return responseMessage;		
	}
	
	
	/**
	 *   Post요청
	 * @param uri
	 * @param data
	 * @param headers
	 * @return ResponseMessage
	 * @throws Exception
	 */
	public static final ResponseMessage requestPost(String uri, String data, Map<String, String> headers) throws Exception {
		HttpResponse response = null;
		ResponseMessage responseMessage = new ResponseMessage();
		Iterator<String> itr = headers.keySet().iterator();

		String key = "";
		String val = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri);
		
		try {
			
			while(itr.hasNext()) {
				key = (String)itr.next();
				val = (String)headers.get(key);
				
				post.setHeader(key, val);
			}

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
			if(response.getEntity() == null) {
				responseMessage.setContents("");
			} else {
				responseMessage.setContents(EntityUtils.toString(response.getEntity()));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();
			}
		}

		return responseMessage;		
	}
	
	/**
	 *   POST요청
	 * @param uri
	 * @param data
	 * @return ResponseMessage
	 * @throws Exception
	 */
	public static final ResponseMessage requestPost(String uri, String data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Utils.ACCEPT_ENCODING, Utils.ACCEPT_ENCODING_IDNTITY);
		map.put(Utils.CONTENT_TYPE, Utils.CONTENT_TYPE_JSON);

		return requestPost(uri, data, map);
	}

	/**
	 *   GET요청
	 * @param uri
	 * @return ResponseMessage
	 * @throws Exception
	 */
	public static final ResponseMessage requestGet(String uri) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Utils.ACCEPT_ENCODING, Utils.ACCEPT_ENCODING_IDNTITY);
		map.put(Utils.CONTENT_TYPE, Utils.CONTENT_TYPE_JSON);
		
		return requestGet(uri, map);
	}
	
	/**
	 *   GET요청
	 * @param uri
	 * @param headers
	 * @return ResponseMessage
	 * @throws Exception
	 */
	public static final ResponseMessage requestGet(String uri, Map<String, String> headers) throws Exception {
		HttpResponse response = null;
		ResponseMessage responseMessage = new ResponseMessage();
		Iterator<String> itr = headers.keySet().iterator();

		String key = "";
		String val = "";

		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(uri);
		try {
			while(itr.hasNext()) {
				key = (String)itr.next();
				val = (String)headers.get(key);
				
				get.setHeader(key, val);
			}

			response = client.execute(get);
			log.debug("Response from Server(GET) ====response====> [" + response + "]");
			log.debug("Response from Server(GET) ====response.getStatusLine().getStatusCode() ====> ["
					+ response.getStatusLine().getStatusCode() + "]");
			log.debug("Response from Server(GET) ====response.getStatusLine().getReasonPhrase() ====> ["
					+ response.getStatusLine().getReasonPhrase() + "]");

			responseMessage.setCode(response.getStatusLine().getStatusCode());
			responseMessage.setMessage(response.getStatusLine().getReasonPhrase());
			if(response.getEntity() == null) {
				responseMessage.setContents("");
			} else {
				responseMessage.setContents(EntityUtils.toString(response.getEntity()));
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(client != null) {
				client.getConnectionManager().shutdown();
			}
		}

		return responseMessage;
	}

	/**
	 *   uri로 부터 부모 객채를 식별 
	 * @param inputUri
	 * @return ResponseMessage
	 */
	public static final String getParentURI(String inputUri) {
		if (inputUri.contains("/")) {
			return inputUri.substring(0,inputUri.lastIndexOf("/"));
		} else {
			return "";
		}
	}
	
	/**
	 * fuseki 프로세스 지우기
	 * @throws Exception
	 * @return String[]
	 */
	private static String[] killFuseki() throws Exception {
		String[] result = new String[]{"",""};
		String[] args = { "ps","aux", "|", "grep", "-i", "fuseki-server", "|", "awk", "{'print $2'}", "|", "head","-1", "|","xargs", "kill"};

		log.debug("killFuseki start==========================>");
		
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}

		try {
			result = Utils.runShell(sb);
		} catch (Exception ee) {
			log.debug("runShell exception : "+ee.getMessage());
		}

		log.debug("resultStr in TripleService.killFuseki() == > "+ Arrays.toString(result));
		
		log.info("killFuseki end==========================>");

		return result;
	}
	
	/**
	 *   fuseki 재기동
	 * @return String[]
	 * @throws Exception
	 */
	private static String[] startFuseki() throws Exception {
		String[] result = new String[]{"",""};

		String[] args = { "/svc/apps/sda/bin/fuseki/run_fuseki.sh"};

		log.info("startFuseki start==========================>");
		
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}
	
		try {
			result = Utils.runShell(sb);
		} catch (Exception ee) {
			log.debug("runShell exception : "+ee.getMessage());
		}
		log.debug("resultStr in TripleService.startFuseki() == > "+ Arrays.toString(result));
		
		log.info("startFuseki end==========================>");

		return result;
	}

	/**
	 *   DW데이타 지우기
	 * @return void	    
	 * @throws Exception
	 */
	public static final void deleteDWTripleAll() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		accessor.deleteDefault();
	}
	
	
	/**
	 * DW데이타 지우기
	 * @throws Exception
	 * @return void
	 */
	public static final void deleteDWTripleAll2() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");

		String queryString = "delete data where {?s ?p ?o }";
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, queryString);

		ResultSet rs = queryExec.execSelect();
		ResultSetFormatter.out(rs);
	}

	/**
	 *   DM데이타 초기화
	 * @throws Exception
	 * @return void
	 */
	public static final void deleteDMTripleAll() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dm.sparql.endpoint");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		accessor.deleteDefault();
	}
	
	/**
	 *   fuseki 재기동
	 * @throws Exception
	 * @return void 
	 */
	public static final void restartFuseki() throws Exception {
		log.debug("fuseki-server restart start ...................");
		String[] rst = Utils.killFuseki();
		if(rst[0].trim().equals("")) {
			Utils.startFuseki();
		}
		log.debug("fuseki-server restart end ...................");
	}
	
	/**
	 *   Shell수행
	 * @param args
	 * @return String[]
	 * @throws Exception
	 */
	public static final String[] runShell(StringBuilder args) throws Exception {
		Process process = null;
		boolean notTimeOver = true;
		String[] result = new String[] { "", "" };
		ProcessOutputThread stdMsgT=null;
		ProcessOutputThread errMsgT=null;

		// OS종류 확인
		String osName = System.getProperty("os.name");

		try {
			String[] cmd = null;
			if (osName.toLowerCase().startsWith("window")) {
				// pass
			} else {
				cmd = new String[] { "/bin/sh", "-c", args.toString() };
			}
			// 명령 실행
			process = Runtime.getRuntime().exec(cmd);

			StringBuffer stdMsg = new StringBuffer();
			stdMsgT = new ProcessOutputThread(process.getInputStream(), stdMsg);
			stdMsgT.start();

			StringBuffer errMsg = new StringBuffer();
			errMsgT = new ProcessOutputThread(process.getErrorStream(), errMsg);
			errMsgT.start();

			while(true) {
				if(! stdMsgT.isAlive() && ! errMsgT.isAlive()) {
					notTimeOver = process.waitFor(30L, TimeUnit.MINUTES);
					log.debug("Thread stdMsgT Status : "+stdMsgT.getState());
			        log.debug("Thread errMsgT Status : "+errMsgT.getState());

					break;
				}
			}
			log.debug("notTimeOver ==========================>" + notTimeOver);

			result[0] = stdMsg.toString().trim();
			result[1] = errMsg.toString().trim();
		} catch (Exception e) {
			log.debug("Exception in runShell() : "+ e.getMessage());
			throw e;
		} finally {
			if (process != null) {
				IOUtils.closeQuietly(process.getOutputStream());
				IOUtils.closeQuietly(process.getInputStream());
				IOUtils.closeQuietly(process.getErrorStream());
				
				process.destroy();
			}
		}

		args.delete(0, args.length());
		args.setLength(0);
		args = null;

		return result;
	}
	
	/**
	 *   폴더 생성
	 * @param save_path
	 * @return String
	 */
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
	
	/**
	 *   날짜 계산
	 * @param iDay
	 * @return String
	 */
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

	/**
	 *   Device정보 리턴
	 * @param deviceUri
	 * @return String
	 * @throws Exception
	 */
	public static String getDeviceInfo(String deviceUri) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint")+"/sparql";
		StringWriter out = new StringWriter();
		String query = getSparQlHeader() + "\n"+ "describe "+ deviceUri;
	
		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURI, query);
		Model model =   qe.execDescribe();
		qe.close();
		model.write(out,"RDF/XML");
		return out.toString();
	}
	
	/**
	 *   Device Semantic Description :: AE Describe Info 
	 * @param deviceUri
	 * @return Device Describe Info (Data Format : RDF/XML)
	 * @throws Query Execution Exception
	 */
	public static String getDeviceSemanticDescInfo(String deviceUri) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint")+"/sparql";
		StringWriter out = new StringWriter();
		
		String query = getSparQlHeader() 
				+ "\n"+ "describe * \n"
				+ " where \n "
				+ "{ "
				+ " bind("+deviceUri+" as ?device). \n "
				+ " { \n "
				+ " select distinct * \n "
				+ "  where \n "
				+ "  { \n "
				+ "   ?device o:hasDeviceType ?devicetype. \n "
				+ "   ?device b:hasFunctionality ?functionality. \n "
				+ "   ?device b:hasService ?service. \n "
				+ "   ?device o:hasResource ?resource. \n "
				+ "   ?functionality b:hasCommand ?command. \n "
				+ "   ?functionality b:hasAspect ?aspect_i. \n "
				+ "   ?functionality b:refersTo ?aspect. \n "
				+ "   ?service b:hasOperation ?operation. \n "
		        + " } \n "
		        + "} \n "
				+"} \n ";
		
	
		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURI, query);
		Model model =   qe.execDescribe();
		qe.close();
		model.write(out,"RDF/XML");
		return out.toString();
	}

	/**
	 * pass여부 확인
	 * @param args
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean checkPass(String args) throws Exception {
		String pass = Utils.sysdateFormat.format(new Date());
		
		log.debug("args==>"+args);
		log.debug("pass=>"+pass);
		
		if(args.equals(pass)) {
			return true;
		} else {
			return false;   
		}
	}
	
	/**
	 * 호스트명 
	 * @return
	 * @throws Exception
	 * @return String
	 */
	public static String getHostName() throws Exception {
		return InetAddress.getLocalHost().getHostName().trim().toLowerCase();
	}
	
	/**
	 *   Base64 엔코딩 여부
	 * @param str
	 * @return boolean
	 */
	public static boolean isBase64Encoded(String str) 	{
	    try {
	    	Base64.getDecoder().decode(str);
	        return (str.replace(" ","").length() % 4 == 0);
	    }   catch (Exception e)    {
	       return false;
	    }
	}
}