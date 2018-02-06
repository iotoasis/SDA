package com.pineone.icbms.sda.sf;

import java.util.List;
import java.util.Map;

/**
 * 쿼리 서비스 수행
 */
public  class QueryService extends QueryCommon {
	
	private QueryItf queryItf;
	private Enum<?> QueryGubun;

	public Enum<?> getQueryGubun() {
		return QueryGubun;
	}

	/**
	 * 쿼리 구분 설정
	 * @param queryGubun
	 * @return void
	 */
	public void setQueryGubun(Enum<?> queryGubun) {
		QueryGubun = queryGubun;
	}

	public QueryService(Enum<?> queryGubun, QueryItf queryItf) {
		this.QueryGubun = queryGubun;
		this.queryItf = queryItf;
	}
	
	/**
	 * 구현체 리턴
	 * @throws Exception
	 * @return QueryItf
	 */
	public QueryItf getImplementClass() throws Exception{
			return this.queryItf;
	}

	/**
	 * 	쿼리 실행(args없음) 
	 * @param query
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> runQuery(String query) throws Exception {
		return queryItf.runQuery(removeQueryGubun(query), new String[] { "" });
	}

	/**
	 * 쿼리 실행(args있음)
	 * @param query
	 * @param idxVals
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		return queryItf.runQuery(removeQueryGubun(query), idxVals);
	}
	
	/**
	 * 다수의 쿼리 실행(args없음)
	 * @param queryList
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		return queryItf.runQuery(removeQueryGubun(queryList), new String[] { "" });
	}
	
	/**
	 * 다수의 쿼리 실행(args있음)
	 * @param queryList
	 * @param idxVals
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		return queryItf.runQuery(removeQueryGubun(queryList), idxVals);
	}
}