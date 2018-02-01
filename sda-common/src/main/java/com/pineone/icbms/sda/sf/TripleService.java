package com.pineone.icbms.sda.sf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MContentInstanceDTO;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContentInstanceMapper;

public class TripleService implements Serializable{
	private static final long serialVersionUID = 5936292391250544152L;
	private final Log log = LogFactory.getLog(this.getClass());
	private String parentResourceUri;
	private String instanceUri;
	private String instanceValue;
	private String createDate;
	private String lastModifiedDate;
	
	// hasLatestContentInstance
	private String deleteql =  ""
			                +" prefix o: <http://www.iotoasis.org/ontology/> "
							+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
							+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+" delete  { <@{arg0}> o:hasLatestContentInstance ?o . } "
							+" where   { <@{arg0}> o:hasLatestContentInstance  ?o  .} "   ;
	
	private String insertql =   ""
			                +" prefix o: <http://www.iotoasis.org/ontology/> "
							+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
							+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+" insert data { <@{arg0}> o:hasLatestContentInstance <@{arg1}> . }" ;
	
	// isContentInstanceOf (	 ?ci   o:isContentInstanceOf ?con2 .)
	private String delete_ici_ql =  ""
			+" prefix o: <http://www.iotoasis.org/ontology/> "
			+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "				
			+" delete  { <@{arg0}> o:isContentInstanceOf ?o . } "
			+" where   { <@{arg0}> o:isContentInstanceOf  ?o  .} "   ;
	private String insert_ici_ql =   ""
			+" prefix o: <http://www.iotoasis.org/ontology/> "
			+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "				
			+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+" insert data { <@{arg0}> o:isContentInstanceOf <@{arg1}> . }" ;

	
	//  hasContentValue
	private String delete_cv_ql =  ""
			+" prefix o: <http://www.iotoasis.org/ontology/> "
			+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "  
			+" delete  { <@{arg0}> o:hasContentValue ?o . } "
			+" where   { <@{arg0}> o:hasContentValue  ?o  .} "   
            +" ; "
			+" delete  { <@{arg0}> o:hasCreateDate ?o . } "
			+" where   { <@{arg0}> o:hasCreateDate  ?o  .} "   
			+" ; "
			+" delete  { <@{arg0}> o:hasLastModifiedDate ?o . } "
			+" where   { <@{arg0}> o:hasLastModifiedDate  ?o  .} "   

			;
	
	private String insert_cv_ql_string =   ""
			+" prefix o: <http://www.iotoasis.org/ontology/> "
			+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
			+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+" insert data { <@{arg0}> o:hasContentValue \"@{arg1}\"^^xsd:string . }" 
            +" ; "				
			+" insert data { <@{arg0}> o:hasCreateDate \"@{arg2}\"^^xsd:string . }"
            +" ; "				
			+" insert data { <@{arg0}> o:hasLastModifiedDate \"@{arg3}\"^^xsd:string . }"
			;

	private String insert_cv_ql_float =   ""
			+" prefix o: <http://www.iotoasis.org/ontology/> "
			+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
			+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+" insert data { <@{arg0}> o:hasContentValue \"@{arg1}\"^^xsd:float . }"
            +" ; "				
			+" insert data { <@{arg0}> o:hasCreateDate \"@{arg2}\"^^xsd:string . }"
            +" ; "				
			+" insert data { <@{arg0}> o:hasLastModifiedDate \"@{arg3}\"^^xsd:string . }"
			;
	
	private List<String> ci_ql_statements = new ArrayList<String>();
	private List<String> ici_ql_statements = new ArrayList<String>();
	private List<String> cv_ql_statements = new ArrayList<String>();
	

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getParentResourceUri() {
		return parentResourceUri;
	}

	public void setParentResourceUri(String parentResourceUri) {
		this.parentResourceUri = parentResourceUri;
	}

	public String getInstanceUri() {
		return instanceUri;
	}

	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}

	public String getInstanceValue() {
		return instanceValue;
	}

	public void setInstanceValue(String instanceValue) {
		this.instanceValue = instanceValue;
	}

	// DBObject나 String값을 triple로 변환
	public String getTriple(Object doc) throws Exception {
		Gson gson = new Gson();
		StringWriter sw = new StringWriter();
		String returnStr = "";
		String ri = Utils.NotSupportingType;
		OneM2MContentInstanceDTO contextInstanceDTO = new OneM2MContentInstanceDTO();

		int ty = -1;
	
		if (doc instanceof DBObject) {
			DBObject docT = (DBObject) doc;
			ty = (Integer) docT.get("ty");
		} else if (doc instanceof String) {
			// ty값을 확인하기 위해서 특정 객체에 매핑해봄(ty=4)
			contextInstanceDTO = gson.fromJson((String)doc, OneM2MContentInstanceDTO.class);
			ty = Integer.parseInt(contextInstanceDTO.getTy());
		}

		if (ty == 5) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");
			sw.flush();
		} else if (ty == 4) {
			if (doc instanceof DBObject) {
				contextInstanceDTO = gson.fromJson(gson.toJson(doc), OneM2MContentInstanceDTO.class);
				
			} else if (doc instanceof String) {
				contextInstanceDTO = gson.fromJson((String)doc, OneM2MContentInstanceDTO.class);
			}
			//log.debug("=== ri : "+contextInstanceDTO.getRi()+",  ty : "+ty+" ====>include");		
			

			OneM2MContentInstanceMapper mapper = new OneM2MContentInstanceMapper(contextInstanceDTO);
		
			Model model = ModelFactory.createDefaultModel();
			model.add(mapper.from());
			
			this.setParentResourceUri(mapper.getParentResourceUri());
			this.setInstanceUri(mapper.getInstanceUri());
			this.setInstanceValue(mapper.getContent());
			
			//gooper
			this.setCreateDate(mapper.getCreateDate());
			this.setLastModifiedDate(mapper.getLastModifiedDate());

			// 스트링 변환부분
			RDFDataMgr.write(sw, model, RDFFormat.NTRIPLES);

			returnStr = sw.toString();
			sw.flush();
			sw.close();
			
			// gooper2
			if(! model.isClosed()) {
				model.close();
			}
			
			if(model != null) {
				model = null;
			}
			if(mapper != null) {
				//gooper
				mapper.close();
				mapper = null;
			}
			if(doc != null) {
				doc = null;
			}
		} else if (ty == 3) {
			log.debug("=== ri: "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();
		} else if (ty == 2) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();
		} else if (ty == 1) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();
		} else if (ty == 0) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();
		} else {
			log.debug("Unknown ty("+ty+") value ====>exclude ");
		}
		
		return returnStr;
	}
	
	public void makeFinalSparql() throws Exception {
		QueryCommon qc = new QueryCommon();
		
		ci_ql_statements.add(qc.makeFinal(this.deleteql, new String[]{this.getParentResourceUri(), this.getInstanceUri()}));
		ci_ql_statements.add(qc.makeFinal(this.insertql, new String[]{this.getParentResourceUri(), this.getInstanceUri()}));
		
		ici_ql_statements.add(qc.makeFinal(this.delete_ici_ql, new String[]{this.getInstanceUri(), this.getParentResourceUri()}));
		ici_ql_statements.add(qc.makeFinal(this.insert_ici_ql, new String[]{this.getInstanceUri(), this.getParentResourceUri()}));
		
		cv_ql_statements.add(qc.makeFinal(this.delete_cv_ql, new String[]{this.getInstanceUri(), this.getInstanceValue()}));
		try {
			Float.parseFloat(this.getInstanceValue());
			cv_ql_statements.add(qc.makeFinal(this.insert_cv_ql_float, new String[]{this.getInstanceUri(), this.getInstanceValue(), this.getCreateDate(), this.getLastModifiedDate()}));
		} catch (Exception e) {
			cv_ql_statements.add(qc.makeFinal(this.insert_cv_ql_string, new String[]{this.getInstanceUri(), this.getInstanceValue(), this.getCreateDate(), this.getLastModifiedDate()}));
		}
	}

	public void addLatestContentInstanceMany() throws Exception {
		int MAX_SIZE = 3000;
		int bundle_count = 0;

		//test
		// log.debug("String.join(\" ; \", this.delete_ici_ql_statements) ===>" + String.join("\n ; \n", this.ici_ql_statements));

		QueryService sparqlService = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL);

		//ci_statements(ci의 delete및 insert를 모두 가지고 있음)
		bundle_count = this.ci_ql_statements.size() / MAX_SIZE;
		for(int m = 1; m <= bundle_count; m++) {
			((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).runModifySparql(String.join(" ; ", 
					this.ci_ql_statements.subList(MAX_SIZE*(m-1), MAX_SIZE*m)), new String[]{}, Utils.QUERY_DEST.ALL.toString());
		}
		((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).runModifySparql(String.join(" ; ", 
				this.ci_ql_statements.subList(MAX_SIZE*bundle_count, ci_ql_statements.size())), new String[]{}, Utils.QUERY_DEST.ALL.toString());

		//ici_ql_statements(ici의 delete및 insert를 모두 가지고 있음)
		bundle_count = this.ici_ql_statements.size() / MAX_SIZE;
		for(int m = 1; m <= bundle_count; m++) {
			((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).runModifySparql(String.join(" ; ", 
					this.ici_ql_statements.subList(MAX_SIZE*(m-1), MAX_SIZE*m)), new String[]{}, Utils.QUERY_DEST.ALL.toString());
		}
		((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).runModifySparql(String.join(" ; ", 
				this.ici_ql_statements.subList(MAX_SIZE*bundle_count, ici_ql_statements.size())), new String[]{}, Utils.QUERY_DEST.ALL.toString());


		//cv_ql_statements(cv의 delete및 insert를 모두 가지고 있음)
		bundle_count = this.cv_ql_statements.size() / MAX_SIZE;
		for(int m = 1; m <= bundle_count; m++) {
			((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).runModifySparql(String.join(" ; ", 
					this.cv_ql_statements.subList(MAX_SIZE*(m-1), MAX_SIZE*m)), new String[]{}, Utils.QUERY_DEST.DM.toString());
		}
		((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).runModifySparql(String.join(" ; ", 
				this.cv_ql_statements.subList(MAX_SIZE*bundle_count, cv_ql_statements.size())), new String[]{}, Utils.QUERY_DEST.DM.toString());
	}
	
	
	//public void addLatestContentInstance(String parentResourceUri,  String instanceUri) throws Exception {
	public void addLatestContentInstance() throws Exception {
		// this.getParentResourceUri() 	: http://www.iotoasis.org/herit-in/herit-cse/CAMPUS_HUB01/hub/status
		// this.getInstanceUri()     			: http://www.iotoasis.org/herit-in/herit-cse/CAMPUS_HUB01/hub/status/CONTENT_INST_1103475
		// his.getInstanceValue()  			: 35 or ONSB_Alcohol01_001(00:15:83:00:96:0E), ONSB_Butane01_001(00:15:83:00:43:33)

		try {
			QueryService sparqlService = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL);
			
			// hasLatestContentInstance 생성(DW, DM)
			((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(this.deleteql, this.insertql, new String[]{this.getParentResourceUri(), this.getInstanceUri()}, Utils.QUERY_DEST.ALL.toString());
			
			// DM에 isContentInstanceOf를 생성(DM)->ALL
			((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(this.delete_ici_ql, this.insert_ici_ql, new String[]{this.getInstanceUri(), this.getParentResourceUri()}, Utils.QUERY_DEST.ALL.toString());
	
			// DM에 hasContentValue 생성(DM)(DW는 ContentInstanceMapper에서 Model API를 통해서 입력하고 있으므로 여기서 입력하지 않음)
			// con값을 숫자로 변활 수 있는 값만 처리함->모두 처리함
			try {
				Float.parseFloat(this.getInstanceValue());
				((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(this.delete_cv_ql, this.insert_cv_ql_float, new String[]{this.getInstanceUri(), this.getInstanceValue(), this.getCreateDate(), this.getLastModifiedDate()}, Utils.QUERY_DEST.DM.toString());				
			} catch (Exception e) {
				((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(this.delete_cv_ql, this.insert_cv_ql_string, new String[]{this.getInstanceUri(), this.getInstanceValue(), this.getCreateDate(), this.getLastModifiedDate()}, Utils.QUERY_DEST.DM.toString());
			}
		} catch (Exception e) {
		    log.debug("exception in addLatestContentInstance() : "+e.getMessage());
		}
	}
	
	// Halyard에는 최근값을 입력하지 않음...
	/*
	public void addLatestContentInstanceIntoHalyard() throws Exception {
	}
	*/
	
	// triple파일 생성(작업시간과 생성시간을 엮어서 만듬)
	public void makeTripleFile(String triple_path_file, StringBuffer sb) throws Exception {
		FileWriter fw = null;
		BufferedWriter bw = null;
		log.info("makeTripleFile start==========================>");
		log.debug("makeTripleFile ========triple_path_file=================>" + triple_path_file); 
		try {
			fw = new FileWriter(triple_path_file);
			bw = new BufferedWriter(fw);
			bw.write(sb.toString());

			bw.flush();
			fw.flush();
			
			bw.close();
			fw.close();
			
			log.info("makeTripleFile end==========================>");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (bw != null) {
				try {
					IOUtils.closeQuietly(bw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (fw != null) {
				try {
					IOUtils.closeQuietly(fw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// triple파일을 DW로 전송
	public String[] sendTripleFileToDW(String triple_path_file) throws Exception {
		String[] result = new String[]{"",""};
		
		String[] args = { "/home/pineone/svc/apps/sda/bin/apache-jena-fuseki-2.3.0/bin/s-post",
						"http://192.168.1.1:3030/icbms", "default", "/tmp/test.nt" };

		// conf값을 확인해서 재설정함
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.triple.regist.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");
		args[2] = "default";
		args[3] = triple_path_file;

		log.info("sendTripleFile to DW start==========================>");
		log.debug("sendTripleFile ==============triple_path_file============>" + triple_path_file);
		
		// 개수확인(before)
		//log.debug("before count ====>\n");
		//Utils.getTripleCount();

		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}

		log.debug("sendTripleFile ==============args============>" + sb.toString());
	
		// 실행(1차)
		log.debug("try (first).......................");
		result = Utils.runShell(sb);
		
		log.debug("resultStr in TripleService.sendTripleFileToDW() == > "+ Arrays.toString(result));
		
		if(result[1] == null || ! result[1].trim().equals("")) {
			log.debug("result[1](error message) in TripleService.sendTripleFileToDW() == > "+ result[1]);
			int waitTime = 15*1000;
			// fuseki재기동 (에러 메세지 : 500 Server Error http://166.104.112.43:23030/icbms?default)
			if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
				
				log.debug("sleep (first)...........................");
				// 일정시간을 대기 한다.(1차)
				Thread.sleep(waitTime);

				// restart fuseki
				Utils.restartFuseki();
				
				log.debug("sleep (final)...........................");
				// 일정시간을 대기 한다.(2차)
				waitTime = 30*1000;
				Thread.sleep(waitTime);
				
				// 실행(2차)
				log.debug("try (final).......................");
				result = Utils.runShell(sb);
				if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
					throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
				}
			} else {
				// pass
			}
		}
		
		log.info("sendTripleFile to DW  end==========================>");
		return result;
	}
	
	
	// triple파일을 DM로 전송
	public String[] sendTripleFileToDM(String triple_path_file) throws Exception {
		String[] result = new String[]{"",""};
		
		String[] args = { "/home/pineone/svc/apps/sda/bin/apache-jena-fuseki-2.3.0/bin/s-post",
						"http://192.168.1.1:3030/icbms", "default", "/tmp/test.nt" };

		// conf값을 확인해서 재설정함
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.triple.regist.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dm.sparql.endpoint");
		args[2] = "default";
		args[3] = triple_path_file;

		log.info("sendTripleFile to DM start==========================>");
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}

		log.debug("sendTripleFile  to DM ==============args============>" + sb.toString());
	
		// 전송 실행
		result = Utils.runShell(sb);
		
		if(result[1] == null || ! result[1].trim().equals("")) {
			log.debug("result[1](error message) in TripleService.sendTripleFileToDM() == > "+ result[1]);
			if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
				throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
			}
		} else {
			// pass
		}
		
		log.info("sendTripleFile to DM  end==========================>");
		return result;
	}
	
	
	// triple파일을 읽어서 Post형태로 Halyard에 등록함(임시)
	public ResponseMessage sendTripleFileToHalyard_bak(File triple_path_file) throws Exception {
		return new ResponseMessage();
		
	}
	
	// triple파일을 읽어서 Post형태로 Halyard에 등록함	
	public ResponseMessage sendTripleFileToHalyard(File triple_path_file) throws Exception {
		log.info("sendTripleFile to Halyard  start==========================>");

		// 전송할 파일읽기
		String line = "";
		StringBuffer sb = new StringBuffer();
		//BufferedReader in = new BufferedReader(new FileReader(new File(triple_path_file)));
		BufferedReader in = new BufferedReader(new FileReader(triple_path_file));
		while(( line = in.readLine()) != null) {
			sb.append(line);
			sb.append(" \n ");
		}
		if(in != null) in.close();
				
		QueryService sparqlService= QueryServiceFactory.create(Utils.QUERY_GUBUN.HALYARDSPARQL);
		ResponseMessage result = ((SparqlHalyardQueryImpl)sparqlService.getImplementClass()).insertByPost(sb.toString());
		
		if(result.getContents() == null || ! result.getContents().trim().equals("")) {
			log.debug("result in TripleService.sendTripleFileToHalyard() == > "+ result.getContents());
		} else {
			// pass
		}
		
		log.info("sendTripleFile to Halyard  end==========================>");
		return result;
	}
	
	// triple파일 체크(Fuseki만)
	public String[] checkTripleFile(String triple_path_file, String result_file_name) throws Exception {
		String[] result = new String[]{"",""};

		String[] args = { "/svc/apps/sda/bin/jena/apache-jena-3.0.0/bin/riot",
						"--check", "/tmp/test.nt" };

		// conf값을 확인해서 재설정함
		String result_path = Utils.getSdaProperty("com.pineone.icbms.sda.riot.result.save_path");
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");;
		args[2] = triple_path_file;

		log.info("checkTripleFile start============================>");
		log.debug("checkTripleFile ==============triple_path_file============>" + triple_path_file);
		log.debug("result_path ==============result_path============>" + result_path);
		
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str); 
			sb.append(" ");
		}

		log.debug("checkTripleFile ==============args============>" + sb.toString());
	
		// 실행
		/*
		result = Utils.runShell(sb);
		//log.debug("resultStr in TripleService.checkTripleFile() == > "+ Arrays.toString(result));
		if(result[1] == null || ! result[1].trim().equals("")) {
			// fuseki재기동 (에러 메세지 : 500 Server Error http://166.104.112.43:23030/icbms?default)
			if(result[1].contains("500 Server Error")) {
				// restart fuseki
				Utils.restartFuseki();
			}
		// check하는 경우는 별도의 exception을 발생시키지 않음
		//throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
		}
		*/

		//실행(1차)
		log.debug("try (first)...........................");
		result = Utils.runShell(sb);
		
		if(result[1] == null || ! result[1].trim().equals("")) {
			log.debug("result[1](error message) in TripleService.checkTripleFile() == > "+ result[1]);
			int waitTime = 15*1000;
			// fuseki재기동 (에러 메세지 : 500 Server Error http://166.104.112.43:23030/icbms?default)
			if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
				log.debug("sleep (first)...........................");
				// 일정시간을 대기 한다.(1차)
				Thread.sleep(waitTime);

				// restart fuseki
				Utils.restartFuseki();
				
				log.debug("sleep (final)...........................");
				// 일정시간을 대기 한다.(2차)
				waitTime = 30*1000;
				Thread.sleep(waitTime);
				
				// 실행(2차)
				log.debug("try (final)...........................");
				result = Utils.runShell(sb);
				if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
					throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
				}
			} else {
				throw new UserDefinedException(HttpStatus.CONTINUE,  result[1].toString());
			}
		}


		
		log.info("checkTripleFile end==========================>");

		return result;
	}
	
	
	// triple파일 체크결과 파일 생성(작업시간과 생성시간을 엮어서 만듬)
	public void makeResultFile(String file_name, String[] check_result) throws Exception {
		FileWriter fw = null;
		BufferedWriter bw = null;
		String result_path = Utils.getSdaProperty("com.pineone.icbms.sda.riot.result.save_path");
		String result_path_file = "";
		log.info("makeResultFile start==========================>");
		log.debug("makeResultFile ========result_path_file=================>" + result_path_file);
		
		// 폴더가 없으면 생성
		result_path = Utils.makeSavePath(result_path);
		
		try {
			result_path_file = result_path + "/" + file_name;
			fw = new FileWriter(result_path_file);
			bw = new BufferedWriter(fw);
			bw.write(check_result[1]+check_result[0]);

			bw.flush();
			fw.flush();
			
			bw.close();
			fw.close();
			
			log.info("makeResultFile end==========================>");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (bw != null) {
				try {
					IOUtils.closeQuietly(bw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (fw != null) {
				try {
					IOUtils.closeQuietly(fw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
