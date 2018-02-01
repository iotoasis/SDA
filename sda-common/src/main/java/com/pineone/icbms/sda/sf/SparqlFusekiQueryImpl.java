package com.pineone.icbms.sda.sf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import com.pineone.icbms.sda.comm.util.Utils;

/**
 * Fuseki Endpoint를 이용한 Sparql처리
 */
public class SparqlFusekiQueryImpl extends QueryCommon implements QueryItf {
	
	private final Log log = LogFactory.getLog(this.getClass());

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.lang.String)
	 */
	@Override
	public List<Map<String, String>> runQuery(String query) throws Exception {
		return  runQuery(query, new String[]{""});
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");
		String madeQl = "";

		log.info("runQuery of sparql start ======================>");
		
		// gooper
		if(Utils.getHostName().equals(Utils.getSdaProperty("com.pineone.icbms.sda.fuseki.dm.hostname"))) {
			serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dm.sparql.endpoint");
		}

		log.debug("try (first) .................................. ");
		madeQl = makeFinal(query, idxVals);
	
		query = Utils.getSparQlHeader() + madeQl.toString();

		log.debug("final query==========>\n" + query);

		QueryExecution queryExec;
		ResultSet rs = null;
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
					map.put(vName, vValue);
				} else if (qs.get(vName).isResource()) {
					log.debug("this is Resource type ............................");
					String vValue = qs.getResource(vName).toString();
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
	
	/**
	 * update수행
	 * @param deleteql
	 * @param insertql
	 * @param idxVals
	 * @param dest
	 * @throws Exception
	 */
	public synchronized void updateSparql(String deleteql, String insertql, String[] idxVals, String dest) throws Exception {
		log.debug("delete->insert sparql start............................");
		// delete
		runModifySparql(deleteql, idxVals, dest);
		// insert
		runModifySparql(insertql, idxVals, dest);
		log.debug("delete->insert sparql end............................");		
	}

	/**
	 * update쿼리수행(sparql만 해당됨)
	 * @param sparql
	 * @param idxVals
	 * @param dest
	 * @throws Exception
	 */
	public void  runModifySparql(String sparql, String[] idxVals, String dest) throws Exception {
		String madeQl = makeFinal(sparql, idxVals);
		
		if(dest.equals("ALL") || dest.equals("DW")) {
			String updateService = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint") + "/update";
			//String madeQl = makeFinal(sparql, idxVals);
			UpdateRequest ur = UpdateFactory.create(madeQl);
			UpdateProcessor up;
			
			log.debug("runModifySparql() on DatWarehouse server start.................................. ");
		
			try {
				log.debug("try (first).................................. ");
				up = UpdateExecutionFactory.createRemote(ur, updateService);
				up.execute();
			} catch (Exception e) {
				int waitTime = 15*1000;
				log.debug("Exception message in runModifySparql() =====> "+e.getMessage());  
				
				try {
					// 일정시간 대기 했다가 다시 수행함
					log.debug("sleeping.(first).................................. in "+waitTime);
					Thread.sleep(waitTime);
					
					log.debug("try (second).................................. ");
					up = UpdateExecutionFactory.createRemote(ur, updateService);
					up.execute();
				} catch (Exception ee) {
					log.debug("Exception 1====>"+ee.getMessage());
					waitTime = 30*1000;
					if(ee.getMessage().contains("Service Unavailable") || ee.getMessage().contains("java.net.ConnectException")			
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
				} // 두번째 try
			} // 첫번째 try
			
			log.debug("runModifySparql() on DataWarehouse server end.................................. ");
		}
		
		if(dest.equals("ALL") || dest.equals("DM")) {
			//동일한(delete or insert) sparql를 DM서버에도 수행함(최근값 혹은 추론결과, subscription값등을 등록한다.)
			log.debug("runModifySparql() on DataMart server start.................................. ");
			//String madeQl = makeFinal(sparql, idxVals);
			String updateService2 = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dm.sparql.endpoint") + "/update";
			UpdateRequest ur2 = UpdateFactory.create(madeQl);
			
			//log.debug("sparql of "+dest+" in runModifySparql ===> "+madeQl2);
			
			//log.debug("madeQl2  ============>"+ madeQl2);
			//log.debug("query  ============>"+ ur2.toString());
			
			UpdateProcessor up2 = UpdateExecutionFactory.createRemote(ur2, updateService2);
			up2.execute();
			log.debug("runModifySparql() on DataMart server end.................................. ");
		}
	}
	
	// 여러개의 쿼리를 이용하여 결과 만들기(argument가 없음)
	@Override
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		return getUniqueResultBySameColumn(queryList, new String[] { "" });
	}
	
	// 여러개의 쿼리를 이용하여 결과 만들기(argument가 있음)
	@Override
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		// 컬럼명칭이 다른것도 있으며 컬럼개수가 상이한 경우
		return getUniqueResultBySameColumn(queryList, idxVals);
	}
	
	// 여러개의 쿼리를 이용하여 결과 만들기(argument가 있음): 컬럼의 개수가 1개 이상이고 비교대상의 컬럼 명칭이 모두 동일한 경우 결과 값 추출하는 로직 
	private List<Map<String, String>> getUniqueResultBySameColumn(List<String> queryList, String[] idxVals) throws Exception {

		// queryList의 쿼리를 수행한 결과를 모두 담고 있는 List
		List<List<Map<String, String>>> query_result_list = new ArrayList<List<Map<String, String>>>();

		// queryList한개의 수행결과를 담고 있는 List
		List<Map<String, String>> query_result;

		boolean haveNullResult = false;
		
		//String result_str = "";
		//String aware_group_id = Utils.dateFormat.format(new Date()) + "S"+String.format("%010d", ai.getAndIncrement());

		//log.debug("idxVals in getUniqueResultBySameColumn()================>" + Arrays.toString(idxVals));

		// 쿼리를 실행해서 구분자로 분리하여 list에 담는다.
		// 1. 모든 줄에 있는 값을 찾아야 하므로 쿼리결과가 값이 없는 row가 있으면 다음 쿼리는 수행하지 않음
		for (int i = 0; i < queryList.size(); i++) {
			query_result = runQuery(queryList.get(i), idxVals);
			log.debug("query result[" + i + "]  =========> \n" + query_result.toString());
			
			// aware수행을 thsda_context_aware_hist에 insert(runQuery전에 insert하지 말것)
			//String start_time  = Utils.dateFormat.format(new Date());
			//insertAwareHist(aware_group_id, this.cmid, this.ciid, start_time);

			//result_str = (i+1)+"/"+queryList.size() + " : ";
			if (query_result.size() == 0) {
				haveNullResult = true;
				log.debug("this query result has null, so it will break out loop without running rest query .............");
				//result_str += " this query result has null, so it will break out loop without running rest query .............";
				break;
			} else {
				//result_str += query_result.toString();
				
				// 중복제거
				query_result = distinctList(query_result);
				log.debug("distinct query result[" + i + "]  =========> \n" + query_result.toString());
				
				query_result_list.add(query_result);
				log.debug("result added of query_result.size() : " + query_result.size());
			}
			
			// aware수행 결과를 thsda_context_aware_hist에 update
			//updateFinishTime(aware_group_id, this.cmid, this.ciid, start_time, Utils.dateFormat.format(new Date()), result_str);
		}
		
		log.debug("query_result_list ==>" + query_result_list);
		
		// 결과값의 전체 개수
		int total_query_result_list_count = query_result_list.size();

		log.debug("total_query_result_list.size() ==>" + total_query_result_list_count);
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
		
		log.debug("cntList =========> " + cntList);
		
		// return할 최종결과 List
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		int matchedRowCnt = 0;
		// 2. 건수가 제일 작은 것을 기준으로 찾아야함.
		if (haveNullResult == false && total_query_result_list_count > 1) {
			Collections.sort(cntList, new CntCompare());
			int idx = cntList.get(0).getIdx(); 			// 개수가 제일 작은것을 찾는다.
			List<Map<String, String>> stdList = query_result_list.get(idx); // 비교시 기준이 되는 List를 추출한다.
			query_result_list.remove(idx); 				// idx에 속하는 List는 제거하여 중복체크되지 않도록 함

			log.debug("stdList =========> " + stdList.toString());
			log.debug("removed query_result_list ==>" + query_result_list);
			
			// 제일 작은 개수 List를 기준으로 체크한다.
			for (int i = 0; i < stdList.size(); i++) {
				matchedRowCnt = 0;
				log.debug("stdList.get(" + i + ") :"+stdList.get(i));
				for (int k = 0; k < query_result_list.size(); k++) {
					log.debug("query_result_list.get(" + k + ") :" +query_result_list.get(k));
					
					if (query_result_list.get(k).contains(stdList.get(i))) {
						matchedRowCnt++;
						log.debug("query_result_list.get(" + k + ").contains(stdList.get(" + i + ")) == true");
					}
				} // 내부 순환 end
				log.debug("matchedRowCnt of "+stdList.get(i)+" ===> "+matchedRowCnt);
				if(matchedRowCnt == (total_query_result_list_count-1)) {
					returnList.add(stdList.get(i));
				}
			}
		} else if (haveNullResult == false && total_query_result_list_count  == 1) {       			// 결과값이 1개의 row만을 가지고 있으면 내부 값을 모두 리턴해줌
			log.debug("total_query_result_list_count is 1  =========> " + query_result_list.get(0));
			returnList = query_result_list.get(0);
		} else {
			// pass
		}
		
		return distinctList(returnList);
	}
	
	private List<Map<String, String>> distinctList(List<Map<String, String>> list) {
		//중복 제거
		// HashSet 데이터 형태로 생성되면서 중복 제거됨
		HashSet<Map<String,String>> hs = new HashSet<Map<String, String>>(list);

		// ArrayList 형태로 다시 생성
		ArrayList<Map<String, String>> returnList2 = new ArrayList<Map<String, String>>(hs);
		return returnList2;
	}
	
	// update(sparql만 해당됨)
	public void updateSparq(String updateql, String[] idxVals, String dest) throws Exception {
		log.debug("update sparql start............................");
		runModifySparql(updateql, idxVals, dest);
		log.debug("update sparql end............................");
	}

	// update(delete->insert)(sparql만 해당됨)
	/*
	public synchronized void updateSparql(String deleteql, String insertql, String[] idxVals, String dest) throws Exception {
		log.debug("delete->insert sparql start............................");
		// delete
		runModifySparql(deleteql, idxVals, dest);
		// insert
		runModifySparql(insertql, idxVals, dest);
		log.debug("delete->insert sparql end............................");		
	}
	*/

	// update(delete, insert를 한트랜잭션에서 처리) - 해당 uri가 존재하지 않으면 작동하지 않음
	public void updateSparql2(String deleteinsertql, String[] idxVals, String dest) throws Exception {
		log.debug("delete+insert sparql start............................");
		// delete+insert
		runModifySparql(deleteinsertql, idxVals, dest);
		log.debug("delete+insert sparql end............................");		
	}

	// delete(sparql만 해당됨)
	public void deleteSparql(String deleteql, String[] idxVals, String dest) throws Exception {
		runModifySparql(deleteql, idxVals, dest);
	}

	// insert(sparql만 해당됨)
	public void insertSparql(String insertql, String[] idxVals, String dest) throws Exception {
		runModifySparql(insertql, idxVals, dest);
	}

}
