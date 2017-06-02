package com.pineone.icbms.sda.comm.util;

import java.io.File;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.PropertiesConfiguration;
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
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.RemoteSIException;
import com.pineone.icbms.sda.comm.exception.RemoteSOException;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

public class Utils {
	private static final Log log = LogFactory.getLog(Utils.class);

	private Utils() {
	}
	
	private volatile static PropertiesConfiguration conf ;
	
	// topic 
	public static enum KafkaTopics {
		COL_AE,															// AE
		COL_CONTENT_INSTANCE,							// ContentInstance
		COL_CONTAINER,
		COL_REMOTE_CSE,
		COL_CSE_BASE,
		
		COL_SURVEY_INFO,
		COL_USER_INFO,
		COL_TIME_TABLE,
		COL_ALARM_INFO,
		
		COL_EMP,
		
		COL_ONEM2M,
		COL_RDBMS,
		
		CMD_REQUEST,				// 작업요청
		CMD_RESULT						// 요청결과
	}
	
	// 쿼리구분
	public static enum QUERY_GUBUN {
		MONGODB,
		MARIADBOFGRIB,
		MARIADBOFSDA,
		SPARQL,
		SHELL
	}
	
	// 쿼리 수행 목적지
	public static enum QUERY_DEST {
		DM,
		DW,
		ALL
	}
	
	// kafka broker
	public  static final String BROKER_LIST = "sda1:9092,sda2:9092,sda3:9092";
	public  static final String ZOOKEEPER_LIST = "sda1:2181,sda2:2181,sda3:2181";
	public  static final String HBASE_ZOOKEEPER_HOST = "sda1,sda2,sda3";
	public  static final String HBASE_ZOOKEEPER_PORT = "2181";
	
	// zookeeper
	
	// 날짜형식
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
	//public static final SimpleDateFormat milDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS");
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
	public static final String DataSaved = "Data saved on Kafka Broker.";
	public static final String NotSupportingType = "Not Supporting Type";
	public static final String SchNotInit= "Scheduler is not initiated....";
	
	// 수집구분
	public static final String COL_SI_STATUS_DATA = "COL-SI-STATUS-DATA";
	public static final String COL_SI_ZONE_DATA = "COL-SI-ZONE-DATA";
	public static final String COL_SI_DATA = "COL-SI-DATA";
	
	public static final String COL_SS_USER_DATA= "COL-SS-USER-DATA";
	public static final String COL_SS_ALARM_DATA= "COL-SS-ALARM-DATA";
	public static final String COL_SS_SURVEY_DATA= "COL-SS-SURVEY-DATA";
	
	public static final String COL_SS_DEVICEINFO_DATA= "COL-SS-DEVICEINFO-DATA";
	public static final String COL_SS_TIMETABLE_WATT_DATA= "COL-SS-TIMETABLE-WATT-DATA";
	
	
	
	// SO 결과 리턴 구분(emergency)
	public static final String CALLBACK_EMERGENCY = "occ-emergency";
	// SO 결과 리턴 구분(schedule)
	public static final String CALLBACK_SCHEDULE = "occ-schedule";
	
	public static final String CMD = "query";
	public static final String CMD_TEST = "query-test";
	
	//test용
	public static final String CALLBACK_TEST = "occ";
	
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
//		} else if (e instanceof HttpHostConnectException) {
//			resultMsg.setCode(500);
//			resultMsg.setMessage(e.getMessage());
//			resultMsg.setContents("");
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

	/*
	public static final String getSparQlHeader() {
		String sparQlHeader = ""
					+"prefix swrlb: <http://www.w3.org/2003/11/swrlb#>\n"   
					+"prefix protege: <http://protege.stanford.edu/plugins/owl/protege#>\n"    
					+"prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#>    \n"
					+"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					+"prefix dct: <http://purl.org/dc/terms/>    \n"
					+"prefix icbms: <http://www.iotoasis.org/ontology/>\n"    
					+"prefix dc: <http://purl.org/dc/elements/1.1/>    \n"
					+"prefix j.0: <http://data.qudt.org/qudt/owl/1.0.0/text/>\n"    
					+"prefix owl: <http://www.w3.org/2002/07/owl#>    \n"
					+"prefix xsp: <http://www.owl-ontologies.com/2005/08/07/xsp.owl#>\n"
					+"prefix swrl: <http://www.w3.org/2003/11/swrl#>    \n"
					+"prefix skos: <http://www.w3.org/2004/02/skos/core#>\n"
					+"prefix DUL: <http://www.loa-cnr.it/ontologies/DUL.owl#>  \n"  
					+"prefix m2m: <http://www.iotoasis.org/ontology/>    \n"
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
	*/
	
	
	public static final String getSparQlHeader() {
		String sparQlHeader = ""
				/*
					+"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
//					+"PREFIX dul: <http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#> \n"
				    +"prefix dul:   <http://www.loa-cnr.it/ontologies/DUL.owl#> \n"
					+"PREFIX prefix: <http://prefix.cc/> \n"
					+"prefix owl: <http://www.w3.org/2002/07/owl#> \n"
					+"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					+"prefix o: <http://www.iotoasis.org/ontology/> \n"
					+"prefix b: <http://www.onem2m.org/ontology/Base_Ontology#> \n"
					+"prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n";
					*/
		
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
		        +"prefix foaf: <http://xmlns.com/foaf/0.1/> \n" 
		        +"prefix herit: <http://herit-in/herit-cse/> \n"
		        +"prefix o: <http://www.iotoasis.org/ontology/> \n";

		return sparQlHeader;
	}
	
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

	/*
	public static final String getHeaderForTTLFile_bak() {
		String headerForTTLFile = ""
				+"@prefix qudt:  <http://qudt.org/1.1/schema/qudt#> . \n"
				+"@prefix b:     <http://www.onem2m.org/ontology/Base_Ontology#> . \n"
				+"@prefix quantity: <http://data.nasa.gov/qudt/owl/quantity#> . \n"
				+"@prefix owl:   <http://www.w3.org/2002/07/owl#> . \n"
				+"@prefix swrl:  <http://www.w3.org/2003/11/swrl#> . \n"
				+"@prefix protege: <http://protege.stanford.edu/plugins/owl/protege#> . \n"
				+"@prefix swrlb: <http://www.w3.org/2003/11/swrlb#> . \n"
				+"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> . \n"
				+"@prefix herit: <http://herit-in/herit-cse/> . \n"
				+"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> . \n"
				+"@prefix j.0:   <http://www.iotoasis.org/ontology/#> . \n"
				+"@prefix o:     <http://www.iotoasis.org/ontology/> . \n"
				+"@prefix ssn:   <http://purl.oclc.org/NET/ssnx/ssn#> . \n"
				+"@prefix unit:  <http://data.nasa.gov/qudt/owl/unit#> . \n"
				+"@prefix xsp:   <http://www.owl-ontologies.com/2005/08/07/xsp.owl#> . \n"
				+"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . \n"
				+"@prefix dul:   <http://www.loa-cnr.it/ontologies/DUL.owl#> . \n"
				+"@prefix foaf:  <http://xmlns.com/foaf/0.1/> . \n"
				+"	@prefix dc:    <http://purl.org/dc/elements/1.1/> . \n";
		return headerForTTLFile;
	}
*/
	

	/*
	public static final String getSdaProperty(String envName) {
		return SdaConstant.sdaVariables.get(envName);
	}
	 */
	
	/*
	 // commons-configuration2용
	public static String getSdaProperty(String envName) {
		String getValue = "";

		if(conf == null) {
			log.info("configuration file reading start ...........");
			try {
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
								.configure(params.properties().setEncoding("UTF-8").setFileName("system.properties"));
			conf = builder.getConfiguration();
			} catch (Exception e) {
				log.debug("configuration file reading exception .......:"+e.getMessage());
				getValue = null;
			}
			log.debug("configuration file reading end ...........");
		}
			
		getValue = conf.getString(envName); 
		return getValue;
	}
	 */
	
	/* */
	// commons-configuration용
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
	/* */


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
			responseMessage.setContents(EntityUtils.toString(response.getEntity()));
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
			responseMessage.setContents(EntityUtils.toString(response.getEntity()));
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

	// uri로 부터 부모 객채를 식별 
	public static final String getParentURI(String inputUri) {
		if (inputUri.contains("/")) {
			return inputUri.substring(0,inputUri.lastIndexOf("/"));
		} else {
			return "";
		}
	}
	private static String[] killFuseki() throws Exception {
		String[] result = new String[]{"",""};

		// ps aux | grep -i fuseki-server | awk {'print $2'} | head -1 | xargs kill -9
		//String[] args = { "ps","aux", "|", "grep", "-i", "fuseki-server", "|", "awk", "{'print $2'}", "|", "head","-1", "|","xargs", "kill", "-9"};
		String[] args = { "ps","aux", "|", "grep", "-i", "fuseki-server", "|", "awk", "{'print $2'}", "|", "head","-1", "|","xargs", "kill"};

		// conf값을 확인해서 재설정함
		/*
		String result_path = Utils.getSdaProperty("com.pineone.icbms.sda.riot.result.save_path");
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");;
		args[2] = triple_path_file;
		*/

		log.debug("killFuseki start==========================>");
		
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}

		// 실행
		try {
			result = Utils.runShell(sb);
		} catch (Exception ee) {
			// 로그출력하고 무시함
			log.debug("runShell exception : "+ee.getMessage());
		}

		log.debug("resultStr in TripleService.killFuseki() == > "+ Arrays.toString(result));
		
		log.info("killFuseki end==========================>");

		return result;
	}
	
	private static String[] startFuseki() throws Exception {
		String[] result = new String[]{"",""};

		String[] args = { "/svc/apps/sda/bin/fuseki/run_fuseki.sh"};

		// conf값을 확인해서 재설정함
		/*
		String result_path = Utils.getSdaProperty("com.pineone.icbms.sda.riot.result.save_path");
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");;
		args[2] = triple_path_file;
		*/

		log.info("startFuseki start==========================>");
		
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}
	
		// 실행
		try {
			result = Utils.runShell(sb);
		} catch (Exception ee) {
			// 로그출력하고 무시함
			log.debug("runShell exception : "+ee.getMessage());
		}
		log.debug("resultStr in TripleService.startFuseki() == > "+ Arrays.toString(result));
		
		log.info("startFuseki end==========================>");

		return result;
	}

	// DW데이터 초기화
	public static final void deleteDWTripleAll() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		accessor.deleteDefault();
	}
	
	
	public static final void deleteDWTripleAll2() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");

		String queryString = "delete data where {?s ?p ?o }";
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, queryString);

		ResultSet rs = queryExec.execSelect();

		// 값을 console에 출력함
		ResultSetFormatter.out(rs);
	}

	// DM데이터 초기화
	public static final void deleteDMTripleAll() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dm.sparql.endpoint");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		accessor.deleteDefault();
	}

/*	
	public static final void getTripleCount_XX() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");
		String queryString = "select  (count(?s) as ?count) where {?s ?p ?o }";
		try {
			QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
			ResultSet rs = queryExec.execSelect();
	
			// 값을 console에 출력함
			ResultSetFormatter.out(rs);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception message in getTripleCount() =====> "+e.getMessage());  // HTTP 500 error making the query: java.lang.StackOverflowError
			if(e.getMessage().contains("HTTP 500 error") || e.getMessage().contains("java.net.ConnectException")) {
				// restart fuseki
				restartFuseki();
			}
			throw e;
		}
	}
*/
	
	// restart fuseki-server
	public static final void restartFuseki() throws Exception {
		log.debug("fuseki-server restart start ...................");
		String[] rst = Utils.killFuseki();
		if(rst[0].trim().equals("")) {
			Utils.startFuseki();
		}
		log.debug("fuseki-server restart end ...................");
	}
	
	// gooper
	public static void checkMem_() {
		Runtime r = Runtime.getRuntime();
		
		// MB단위의 가용메모리
		//long freeMem = r.freeMemory()/1024/1024;
		//long maxMem = r.maxMemory()/1024/1024;
		
		// dead line : 500MB
		long deadLine = 1024*1024*500;
		
		DecimalFormat format = new DecimalFormat("###,###,###.##");
        
        //JVM이 현재 시스템에 요구 가능한 최대 메모리량, 이 값을 넘으면 OutOfMemory 오류가 발생 합니다.               
        long max = r.maxMemory();
       
        //JVM이 현재 시스템에 얻어 쓴 메모리의 총량
        long total = r.totalMemory();
       
        //JVM이 현재 시스템에 청구하여 사용중인 최대 메모리(total)중에서 사용 가능한 메모리
        long free = r.freeMemory();
        
        
		log.debug("max memory ==>"+format.format(max));
		log.debug("total memory ==>"+format.format(total));
		log.debug("free memory ==>"+format.format(free));
		log.debug("deadline memory ==>"+format.format(deadLine));
		
		if((free/2014/1024) <= deadLine) {
			// restart fuseki
			try {
				log.debug("fuseki restart because free memory is below than deadline memory !!!!!");
				Utils.restartFuseki();
			} catch (Exception e) {
				log.debug("Fuseki Restart Exception ====>"+e.getMessage());
			}
		}
	}
	
/*	
	public static final void getTripleAll_() throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");

		String queryString = "select ?s ?p ?o {?s ?p ?o}";
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, queryString);

		ResultSet rs = queryExec.execSelect();
		ResultSetFormatter.out(rs);
	}
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
				// cmd = new String[] { "cmd.exe", "/y", "/c", sb.toString() };
			} else {
				cmd = new String[] { "/bin/sh", "-c", args.toString() };
			}
			// 명령 실행
			process = Runtime.getRuntime().exec(cmd);

			// 실행결과 확인(에러)
			StringBuffer stdMsg = new StringBuffer();
			// 스레드로 inputStream버퍼 비우기
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

	// 장비 정보 리턴 
	public static String getDeviceInfo(String deviceUri) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint")+"/sparql";
		StringWriter out = new StringWriter();
		// 요기
		String query = getSparQlHeader() + "\n"+ "describe "+ deviceUri;
	
		QueryExecution qe = QueryExecutionFactory.sparqlService(serviceURI, query);
		Model model =   qe.execDescribe();
		qe.close();
		model.write(out,"RDF/XML");
		return out.toString();
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
	
	public static String getHostName() throws Exception {
		//log.debug("sda.dw value : '"+Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dw")+"'");
		//log.debug("host name : '"+InetAddress.getLocalHost().getHostName().trim().toLowerCase()+"'");
		return InetAddress.getLocalHost().getHostName().trim().toLowerCase();
	}
	
}