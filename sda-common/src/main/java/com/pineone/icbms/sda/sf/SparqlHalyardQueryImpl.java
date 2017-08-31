package com.pineone.icbms.sda.sf;

import java.net.URLEncoder;
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
			rm = Utils.requestData(query);
		} catch (Exception e) {
			throw e;
		}
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		if(rm.getContents().length() >= 2) {
			lines = rm.getContents().split("\n");
			header = lines[0].split(",");							// header분리
		} 
		
		// 첫번째는 header이므로 1부터 시작함
		for(int m = 1; m < lines.length; m++) {
			String row[] = lines[m].split(",");					// 각각의 row를 분리
			for(int n = 0; n < header.length; n++) {			
				Map<String, String> map = new LinkedHashMap<String, String>();
				map.put(header[n].trim(), row[n].trim());
				list.add(map);
			}
		}
		log.info("runQuery of halyard sparql end ======================>");
		return list;
	}
}
