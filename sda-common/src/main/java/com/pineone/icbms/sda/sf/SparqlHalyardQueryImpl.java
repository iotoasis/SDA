package com.pineone.icbms.sda.sf;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

import scala.NotImplementedError;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.util.Utils;
public class SparqlHalyardQueryImpl extends QueryCommon implements QueryItf {
	
	private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.halyard.sparql.endpoint");
		String madeQl = "";
		String lines[] = null; 
		String header[] = null;

		log.info("runQuery of halyard sparql start ======================>");
		
		madeQl = makeFinal(query, idxVals);
		query = Utils.getSparQlHeader() + madeQl.toString();
		log.debug("final query of halyard sparql ==========>\n" + query);
		
		query = serviceURI + URLEncoder.encode(query, "UTF-8");
		
		log.info("-------------------------------------------------");;
		log.info(query);
		log.info("-------------------------------------------------");				

		ResponseMessage rm = null;
		try {
			rm = Utils.requestGet(query);
		} catch (Exception e) {
			throw e;
		}
		log.debug("query result of Halyard : "+rm.getContents());
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if(rm.getContents().length() >= 2) {
			lines = rm.getContents().split("\n");
			header = lines[0].split(",");							// header분리
		} 
		
		// 첫번째는 header이므로 1부터 시작함
		for(int m = 1; m < lines.length; m++) {
			String row[] = lines[m].split(",");					// row를 컬럼으로 분리
			Map<String, String> map = new LinkedHashMap<String, String>();
			for(int n = 0; n < header.length; n++) {
				map.put(header[n].trim(), "<"+row[n].trim()+">");
			}
			list.add(map);
		}
	
		log.info("runQuery of halyard sparql end ======================>");
		return list;
	}
	
	// delete->insert(인수가 sparql인 경우)
	public ResponseMessage updateByQuery(String query) throws Exception {
		log.debug("------------------------updateByQuery-----start-------------------");
		if(query == null || query.equals("")) { throw new NullPointerException("query is null or query is space"); }
		
		// select
		List<Map<String, String>> list;
		//QueryService sparqlService= new QueryService(new SparqlHalyardQueryImpl());
		QueryService sparqlService= QueryServiceFactory.create(Utils.QUERY_GUBUN.HALYARDSPARQL);
		list = sparqlService.runQuery(query, new String[]{""});
		
		log.debug("------------------------updateByQuery-----end-------------------");
		return updateByList(list);
	}
	
	// delete->insert(인수가 String형태의 triple data인경우)
	public ResponseMessage updateByData(String data) throws Exception {
		log.debug("------------------------updateByData-----start-----------------------");		
		if(data == null || data.equals("")) { throw new NullPointerException("data is null or data is space"); }
		
		List<Map<String, String>> dataList = makeListData(data);
		
		ResponseMessage deleteResponse=null;
		ResponseMessage insertResponse=null;
		ResponseMessage returnResponse=null;

		for(int m = 0; m < dataList.size(); m++) {
			Map<String, String> map = (Map<String, String>)dataList.get(m);
			log.debug("dataList["+m+"]"+" : s==>"+(String)map.get("s")+ ", p==>"+(String)map.get("p"));
			deleteResponse = delete((String)map.get("s"), (String)map.get("p"), (String)map.get("o"));
			
			if(deleteResponse.getCode() != 200 || deleteResponse.getCode() != 204) {
				returnResponse = deleteResponse;
			}
		}
		
		insertResponse = insertByPost(data);		
		if(insertResponse.getCode() != 200 || insertResponse.getCode() != 204) {
			returnResponse = insertResponse;
		}

		log.debug("------------------------updateByData-----end-----------------------");
		return returnResponse;
	}
	
	// delete->insert(인수가 List인경우)
	public ResponseMessage updateByList(List<Map<String, String>> dataList) throws Exception {
		log.debug("------------------------updateByList-----start-----------------------");		
		if(dataList == null || dataList.size() == 0) { throw new NullPointerException("dataList is null or dataList.size() is 0"); }
		
		ResponseMessage deleteResponse=null;
		ResponseMessage insertResponse=null;
		ResponseMessage returnResponse=null;
		for(int m = 0; m < dataList.size(); m++) {
			Map<String, String> map = (Map<String, String>)dataList.get(m);
			log.debug("dataList["+m+"]"+" : s==>"+(String)map.get("s")+ ", p==>"+(String)map.get("p")+ ", o==>"+(String)map.get("o"));
			
			deleteResponse = delete((String)map.get("s"), (String)map.get("p"), (String)map.get("o"));
			insertResponse = insertByPost((String)map.get("s")+" "+(String)map.get("p")+" "+(String)map.get("o").replace(">", "gooper2>")+ " . ");
			
			if(deleteResponse.getCode() != 200 || deleteResponse.getCode() != 204) {
				returnResponse = deleteResponse;
			}
			if(insertResponse.getCode() != 200 || insertResponse.getCode() != 204) {
				returnResponse = insertResponse;
			}
		}

		log.debug("------------------------updateByList-----end-----------------------");
		return returnResponse;
	}

	
	// DELETE(s, p)
	public ResponseMessage delete(String s, String p) throws Exception {
		log.debug("------------------------delete(s, o)-----start-----------------------");

		if(s == null || s.equals("")) { throw new NullPointerException("s is null or s is space"); }
		if(p == null || p.equals("")) { throw new NullPointerException("p is null or s is space"); }
		
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.halyard.statement.endpoint");
		String url = serviceURI+
					"?subj="+
					URLEncoder.encode(s, "UTF-8")+
					"&pred="+
					URLEncoder.encode(p, "UTF-8");

		log.debug("------------------------delete(s, o)-----end-----------------------");
		return Utils.requestDelete(url, new HashMap<String, String>());
	}

	// DELETE(s, p, o)
	public ResponseMessage delete(String s, String p, String o) throws Exception {
		log.debug("------------------------delete(s, p, o)-----start-----------------------");
		if(s == null || s.equals("")) { throw new NullPointerException("s is null or s is space"); }
		if(p == null || p.equals("")) { throw new NullPointerException("p is null or p is space"); }
		if(o == null || o.equals("")) { throw new NullPointerException("o is null or o is space"); }
		
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.halyard.statement.endpoint");
		String url = serviceURI+
					"?subj="+
					URLEncoder.encode(s, "UTF-8")+
					"&pred="+
					URLEncoder.encode(p, "UTF-8")+					
					"&obj="+
					URLEncoder.encode(o, "UTF-8");

		log.debug("------------------------delete(s, p, o)-----end-----------------------");
		return Utils.requestDelete(url, new HashMap<String, String>());			
	}

	// POST로 insert(?s ?p ?o가 모두 일치하는 triple은 skip하고 나머지의 경우는 모두 insert함)
	public ResponseMessage insertByPost(String data) throws Exception {
		log.debug("------------------------insertByPost-----start-----------------------");
		if(data == null || data.equals("")) { throw new NullPointerException("data is null or data is space");	}
		
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.halyard.statement.endpoint");
		String contentType = "text/turtle;charset=UTF-8";
							
		Map<String, String> map = new HashMap<String, String>();
		map.put(Utils.CONTENT_TYPE, contentType);
		
		log.debug("------------------------insertByPost-----end-----------------------");
		return Utils.requestPost(serviceURI, data, map);	
	}
	
	// 문자열의 triple data를 List<Map<String, String>>형태로 변경함
	private List<Map<String, String>> makeListData(String data) {
		log.debug("------------------------makeListData-----start-----------------------");
		if(data == null || data.equals("")) { throw new NullPointerException("data is null or data is space");	}
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		StringTokenizer lines = new StringTokenizer(data, "\n");
		
		while(lines.hasMoreTokens()) {
			String[] split = lines.nextToken().split("\\s");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("s", split[0]);
			map.put("p", split[1]);
			map.put("o", split[2]);
			
			list.add(map);
		}
		log.debug("------------------------makeListData-----end-----------------------");		
		return list;
	}

	@Override
	public List<Map<String, String>> runQuery(String query) throws Exception {
		return runQuery(query, new String[]{""});
	}

	@Override
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		return runQuery(queryList, new String[]{""});
	}

	@Override
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		if(queryList.size() == 1) {
			return runQuery(queryList.get(0), idxVals);
		} else {
			throw new NotImplementedError("runQuery() for many querys is not implemented");
		}
	}}
