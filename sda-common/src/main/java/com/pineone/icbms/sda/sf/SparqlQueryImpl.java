package com.pineone.icbms.sda.sf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;

import com.pineone.icbms.sda.comm.util.Utils;
public class SparqlQueryImpl extends QueryCommon implements QueryItf {
	
	private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		String madeQl = "";

		log.info("runQuery of sparql start ======================>");
		
		// 가용 메모리를 확인해서  상태를 체크해서 fuseki서버를 재기동해줌
		//Utils.checkMem();

		log.debug("try (first) .................................. ");
		madeQl = makeFinal(query, idxVals);
	
		query = Utils.getSparQlHeader() + madeQl.toString();

		log.debug("final query==========>\n" + query);

		QueryExecution queryExec;
		ResultSet rs;
		try {
			queryExec = QueryExecutionFactory.sparqlService(serviceURI, query);
			rs = queryExec.execSelect();
		} catch (Exception e) {
			int waitTime = 15*1000;
			log.debug("Exception message in runSparql() =====> "+e.getMessage());  
			
			try {
				// 일정시간 대기 했다가 다시 수행함
				log.debug("sleeping (first)................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				queryExec = QueryExecutionFactory.sparqlService(serviceURI, query);
				rs = queryExec.execSelect();
			} catch (Exception ee) {
				log.debug("Exception 1====>"+ee.getMessage());
				waitTime = 30*1000;
				if(ee.getMessage().contains("Service Unavailable")|| ee.getMessage().contains("java.net.ConnectException")
						// || ee.getMessage().contains("500 - Server Error") || ee.getMessage().contains("HTTP 500 error")
						) {					
					try {
						// restart fuseki
						Utils.restartFuseki();
					
						// 일정시간을 대기 한다.
						log.debug("sleeping (final)................................. in "+waitTime);
						Thread.sleep(waitTime);
						
						// 마지막으로 다시한번 처리해줌
						log.debug("try (final).................................. ");
						queryExec = QueryExecutionFactory.sparqlService(serviceURI, query);
						rs = queryExec.execSelect();
					} catch (Exception eee) {
						log.debug("Exception 2====>"+eee.getMessage());
						throw eee;
					}
				}
				throw ee;
			}
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String vName = "";
		int m = 0;
		for (; rs.hasNext();) {
			QuerySolution qs = rs.nextSolution();
			Iterator<String> itr = qs.varNames();
			Map<String, String> map = new LinkedHashMap<String, String>();
			int n = 0;
			while (itr.hasNext()) {
				vName = (String) itr.next();
				log.debug("vName[" + (m) + "][" + (n++) + "]==================>" + vName);

				if (qs.get(vName).isLiteral()) {
					log.debug("this is Literal type ............................");
					Literal literal = qs.getLiteral(vName);
					String vValue = String.valueOf(literal.getValue());
					// vValue = replaceUriWithPrefix(vValue);
					map.put(vName, vValue);
				} else if (qs.get(vName).isResource()) {
					log.debug("this is Resource type ............................");
					String vValue = qs.getResource(vName).toString();
					// vValue = replaceUriWithPrefix(vValue);
					map.put(vName, vValue);

				} else {
					log.debug("this is unKnown QuerySolution type............................");
				}
			}
			list.add(map);
			m++;
		}

		log.info("runQuery of sparql end ======================>");
		return list;
	}
}
