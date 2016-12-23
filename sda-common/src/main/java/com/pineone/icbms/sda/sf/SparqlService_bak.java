package com.pineone.icbms.sda.sf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

public  class SparqlService_bak extends QueryCommon {

	private final Log log = LogFactory.getLog(this.getClass());

	// 1개의 sparql 쿼리 실행(args없음)
	public List<Map<String, String>> runSparql(String sparql) throws Exception {
		return runSparql(sparql, new String[] { "" });
	}

	// 1개의 sparql 쿼리 실행(args있음)
	public List<Map<String, String>> runSparql(String sparql, String[] idxVals) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		String madeQl = "";

		log.info("runSparql start ======================>");

		log.debug("try (first) .................................. ");
		madeQl = makeFinal(sparql, idxVals);
		sparql = Utils.getSparQlHeader() + madeQl.toString();

		log.debug("final sparql==========>\n" + sparql);

		QueryExecution queryExec;
		ResultSet rs;
		try {
			queryExec = QueryExecutionFactory.sparqlService(serviceURI, sparql);
			rs = queryExec.execSelect();
		} catch (Exception e) {
			int waitTime = 5*1000;
			log.debug("Exception message in runSparql() =====> "+e.getMessage());  
			
			try {
				// 일정시간 대기 했다가 다시 수행함
				log.debug("sleeping (first)................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				queryExec = QueryExecutionFactory.sparqlService(serviceURI, sparql);
				rs = queryExec.execSelect();
			} catch (Exception ee) {
				log.debug("Exception 1====>"+ee.getMessage());
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
						queryExec = QueryExecutionFactory.sparqlService(serviceURI, sparql);
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
			Map<String, String> map = new HashMap<String, String>();
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

		log.info("runSparql end ======================>");
		return list;
	}

	// 여러개의 sparql 쿼리를 이용하여 결과 만들기(argument가 없음)
	public List<Map<String, String>> runSparqlUniqueResult(List<String> sparQlList) throws Exception {
		String[] args = null;
		return runSparqlUniqueResult(sparQlList, args);
	}

	// 여러개의 sparql 쿼리를 이용하여 결과 만들기(argument가 있음), 컬럼의 명칭이 다르고 비교대상의 컬럼 개수가 동일한 경우의 결과 값 추출하는 로직 
	public List<Map<String, String>> runSparqlUniqueResultByVariableColumn(List<String> sparQlList, String[] idxVals) throws Exception {

		// sparQlList의 쿼리를 수행한 결과를 모두 담고 있는 List
		List<List<Map<String, String>>> query_result_list = new ArrayList<List<Map<String, String>>>();

		// sparQl한개의 수행결과를 담고 있는 List
		List<Map<String, String>> query_result;

		boolean haveNullResult = false;

		// return할 최종결과 List
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		log.debug("idxVals in getSparqlResult()================>" + Arrays.toString(idxVals));

		// sparql 실행해서 구분자로 분리하여 list에 담는다.
		// 1. 모든 줄에 있는 값을 찾아야 하므로 쿼리결과가 값이 없는 row가 있으면 다음 쿼리는 수행하지 않음
		for (int i = 0; i < sparQlList.size(); i++) {
			query_result = runSparql(sparQlList.get(i), idxVals);
			
			log.debug("query result[" + i + "]  =========> \n" + query_result.toString());
			if (query_result.size() == 0) {
				haveNullResult = true;
				log.debug("this query result has null, so i will break out loop without running rest sparql .............");
				break;
			} else {
				query_result_list.add(query_result);
				log.debug("query result added of query_result.size() : " + query_result.size());
			}
		}

		log.debug("query_result_list.size() ==>" + query_result_list.size());
		log.debug("haveNullResult ==>" + haveNullResult);

		// 제일 작은 개수를 찾기위해서 개수및 idx 만으로 이루어진 임시 List를 만듬
		List<IdxCnt> cntList = new ArrayList<IdxCnt>();

		if (haveNullResult == false) {
			for (int i = 0; i < query_result_list.size(); i++) {
				IdxCnt cnt = new IdxCnt();
				cnt.setCnt(query_result_list.get(i).size());
				cnt.setIdx(i);
				cntList.add(cnt);
			}
		}

		// 2. 건수가 제일 작은 것을 기준으로 찾아야함.
		if (haveNullResult == false && query_result_list.size() > 1) {
			Collections.sort(cntList, new CntCompare());
			int idx = cntList.get(0).getIdx(); 			// 개수가 제일 작은것을 찾는다.
			List<Map<String, String>> stdList = query_result_list.get(idx); // 비교시 기준이 되는 List를 추출한다.
			query_result_list.remove(idx); 				// idx에 속하는 List는 제거하여 중복체크되지 않도록 함

			log.debug("stdList =========> " + stdList.toString());
			log.debug("idx ==>" + idx);

			// 제일 작은 개수 List를 기준으로 체크한다.
			int matchedColumnCnt = 0;
			for (int i = 0; i < stdList.size(); i++) {
				for (int k = 0; k < query_result_list.size(); k++) {
					if (query_result_list.get(k).contains(stdList.get(i))) {
						matchedColumnCnt++;
						log.debug("query_result_list.get(" + k + ").contains(stdList.get(" + i + ")) == true");
						break;
					}
					if (matchedColumnCnt == 0)	break; 		// 일치하는 것이 하나라도 확인되면 더이상 체크할 필요 없음
				} // List 순환 end
					// matchedColumnCnt와 query_result_list의 개수가 같으면 stdList의 값과 일치하는 값이
					// 각 로우 중에 있다는 의미임(matchedColumnCnt는 매칭된 컬럼 개수임)
				if (matchedColumnCnt == query_result_list.size()) {
					returnList.add(stdList.get(i));
				}
			}
			log.debug("matchedColumnCnt========>" + matchedColumnCnt);
			// 결과값이 one row이면 내부 값을 모두 리턴해줌
		} else if (haveNullResult == false && query_result_list.size() == 1) {
			returnList = query_result_list.get(0);
		} else {
			// pass
		}
		return returnList;
	}

	
	// 여러개의 sparql 쿼리를 이용하여 결과 만들기(argument가 있음) -- 크기가 다른 로우를 가지고 있는 결과값이 동일한 명칭의 컬럼 1개만을  가지고 있는 경우 추출 로직
	public List<Map<String, String>> runSparqlUniqueResult(List<String> sparQlList, String[] idxVals) throws Exception {

		// sparQlList의 쿼리를 수행한 결과를 모두 담고 있는 List
		List<List<Map<String, String>>> query_result_list = new ArrayList<List<Map<String, String>>>();

		// sparQl한개의 수행결과를 담고 있는 List
		List<Map<String, String>> query_result;

		boolean haveNullResult = false;

		// return할 최종결과 List
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		log.debug("idxVals in getSparqlResult()================>" + Arrays.toString(idxVals));

		// sparql 실행해서 구분자로 분리하여 list에 담는다.
		// 1. 모든 줄에 있는 값을 찾아야 하므로 쿼리결과가 값이 없는 row가 있으면 다음 쿼리는 수행하지 않음
		for (int i = 0; i < sparQlList.size(); i++) {
			query_result = runSparql(sparQlList.get(i), idxVals);
			log.debug("query result[" + i + "]  =========> \n" + query_result.toString());
			if (query_result.size() == 0) {
				haveNullResult = true;
				log.debug("this query result has null, so i will break out loop without running rest sparql .............");
				break;
			} else {
				query_result_list.add(query_result);
				log.debug("query result added of query_result.size() : " + query_result.size());
			}
		}

		log.debug("query_result_list.size() ==>" + query_result_list.size());
		log.debug("haveNullResult ==>" + haveNullResult);

		// 제일 작은 개수를 찾기위해서 개수및 idx 만으로 이루어진 임시 List를 만듬
		List<IdxCnt> cntList = new ArrayList<IdxCnt>();

		if (haveNullResult == false) {
			for (int i = 0; i < query_result_list.size(); i++) {
				IdxCnt cnt = new IdxCnt();
				cnt.setCnt(query_result_list.get(i).size());
				cnt.setIdx(i);
				cntList.add(cnt);
			}
		}

		// 2. 건수가 제일 작은 것을 기준으로 찾아야함.
		if (haveNullResult == false && query_result_list.size() > 1) {
			Collections.sort(cntList, new CntCompare());
			int idx = cntList.get(0).getIdx(); 											 // 개수가 제일 작은것을 찾는다.
			List<Map<String, String>> stdList = query_result_list.get(idx); // 비교시 기준이 되는 List를 추출(선택)한다.
			query_result_list.remove(idx); 												 // 선택된 List의 idx는 제거하여 중복체크되지 않도록 함

			log.debug("stdList =========> " + stdList.toString());
			log.debug("idx ==>" + idx);

			// 제일 작은 개수 List를 기준으로 체크한다.
			// 여기는 1개의 컬럼이고 동일한 컬러명을 가지고 있으므로 1차원 배열형태로만 처리된다.
			int matchedRowCnt = 0;
			for (int stdRowIdx = 0; stdRowIdx < stdList.size(); stdRowIdx++) {
				for (int k = 0; k < query_result_list.size(); k++) {
					log.debug("stdList.get("+stdRowIdx+") ========>" + stdList.get(stdRowIdx));
					if (query_result_list.get(k).contains(stdList.get(stdRowIdx))) {
						matchedRowCnt++;
						log.debug("query_result_list.get(" + k + ").contains(stdList.get(" + stdRowIdx + ")) == true");
					}
				} // List 순환 end
					// matchedRowCnt와 query_result_list의 개수가 같으면 stdList의 값과 일치하는 로우가 모두 있다는 의미임
				if (matchedRowCnt == query_result_list.size() ) {
					returnList.add(stdList.get(stdRowIdx));
				}
			}
			log.debug("matchedRowCnt========>" + matchedRowCnt);
			// 결과값이 one row이면 stdList의 값을 모두 리턴해줌
		} else if (haveNullResult == false && query_result_list.size() == 1) {
			returnList = query_result_list.get(0);
		} else {
			// pass
		}
		return returnList;
	}
	
	
	// update
	public void updateSparql(String updateql, String[] idxVals) throws Exception {
		log.debug("update sparql start............................");
		runModifySparql(updateql, idxVals);
		log.debug("update sparql end............................");
	}

	// update(delete->insert)
	public synchronized void updateSparql(String deleteql, String insertql, String[] idxVals) throws Exception {
		log.debug("delete->insert sparql start............................");
		// delete
		runModifySparql(deleteql, idxVals);
		// insert
		runModifySparql(insertql, idxVals);
		log.debug("delete->insert sparql end............................");		
	}

	// delete
	public void deleteSparql(String deleteql, String[] idxVals) throws Exception {
		runModifySparql(deleteql, idxVals);
	}

	// insert
	public void insertSparql(String insertql, String[] idxVals) throws Exception {
		runModifySparql(insertql, idxVals);
	}

	private void runModifySparql(String sparql, String[] idxVals) throws Exception {
		String updateService = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint") + "/update";

		String madeQl = makeFinal(sparql, idxVals);
		UpdateRequest ur = UpdateFactory.create(madeQl);
		UpdateProcessor up;

		try {
			log.debug("try (first).................................. ");
			up = UpdateExecutionFactory.createRemote(ur, updateService);
			up.execute();
		} catch (Exception e) {
			int waitTime = 5*1000;
			log.debug("Exception message in runModifySparql() =====> "+e.getMessage());  // HTTP 500 error making the query: java.lang.StackOverflowError or...
			
			try {
				// 일정시간 대기 했다가 다시 수행함
				log.debug("sleeping.(first).................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				up = UpdateExecutionFactory.createRemote(ur, updateService);
				up.execute();
			} catch (Exception ee) {
				log.debug("Exception 1====>"+ee.getMessage());

				if(ee.getMessage().contains("Service Unavailable") || ee.getMessage().contains("java.net.ConnectException")			
						 // || ee.getMessage().contains("500 - Server Error") || ee.getMessage().contains("HTTP 500 error") 
						 ) {
					try {

						// restart fuseki
						Utils.restartFuseki();
						
						// 일정시간을 대기 한다.
						log.debug("sleeping (final)................................. in "+waitTime);
						Thread.sleep(waitTime);
						
						// 마지막으로 다시한번 시도한다.
						log.debug("try (final).................................. ");
						up = UpdateExecutionFactory.createRemote(ur, updateService);
						up.execute();
					} catch (Exception eee) {
						log.debug("Exception 2====>"+eee.getMessage());
						throw eee;
					}
				}
				throw ee;
			}
		}
	}

	public static void main(String[] args) {
		//SparqlService sparqlService = new SparqlService();
		QueryService sparqlService= new QueryService(new SparqlQueryImpl());
		try {
			System.out.println("now1 ==>" + new Date());
			System.out.println("result ===>" + sparqlService.makeFinal("aaa @{now+10, second, mmss}", new String[] { "" }));
			System.out.println("result ===>" + sparqlService.makeFinal("aaa @{now+50, minute, HHmm}", new String[] { "" }));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
