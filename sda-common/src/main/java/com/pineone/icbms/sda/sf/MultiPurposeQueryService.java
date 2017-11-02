package com.pineone.icbms.sda.sf;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

public  class MultiPurposeQueryService extends QueryCommon {
	private final Log log = LogFactory.getLog(this.getClass());

	private QueryItf queryItf;
	
	public MultiPurposeQueryService() {
		super();
	}
	
	public QueryItf getImplementClass() {
		return this.queryItf;
	}
	
	private void setImplementClass(List<String> list) throws Exception {
		if(list == null || list.size() == 0) throw new NullPointerException("list is null or space");
		setImplementClass(list.get(0).toString());
	}
	
	private void setImplementClass(String query) throws Exception {
		String queryGubun;

		// 구분에 따른 쿼리를 수행한다.
		if(query.contains(Utils.SPLIT_STR)) {
			String[] splitStr = query.split(Utils.SPLIT_STR);
			
			queryGubun = splitStr[1];
			
			if(queryGubun.equals(Utils.QUERY_GUBUN.MARIADBOFGRIB.toString())){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.MARIADBOFGRIB.toString());
				queryItf = new MariaDbOfGribQueryImpl();
			} else if(queryGubun.equals(Utils.QUERY_GUBUN.MARIADBOFSDA.toString())){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.MARIADBOFSDA.toString());
				queryItf = new MariaDbOfSdaQueryImpl();
			} else if(queryGubun.equals(Utils.QUERY_GUBUN.FUSEKISPARQL.toString())){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.FUSEKISPARQL.toString());
				queryItf = new SparqlFusekiQueryImpl();
			} else if(queryGubun.equals(Utils.QUERY_GUBUN.HALYARDSPARQL.toString())){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.HALYARDSPARQL.toString());
				queryItf = new SparqlHalyardQueryImpl();
			} else if(queryGubun.equals(Utils.QUERY_GUBUN.MONGODB.toString())){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.MONGODB.toString());
				queryItf = new MongoDbQueryImpl();
			} else if(queryGubun.equals(Utils.QUERY_GUBUN.SHELL.toString())){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.SHELL.toString());
				queryItf = new ShellQueryImpl();
			} else {
				throw new UserDefinedException(HttpStatus.BAD_REQUEST, "2Unknown query gubun of "+queryGubun);
			}

		} else {
			// pass
		}
	}

	// 쿼리 실행(args없음)
	public List<Map<String, String>> runQuery(String query) throws Exception {
		setImplementClass(query);
		return queryItf.runQuery(removeQueryGubun(query), new String[] { "" });
	}

	// 쿼리 실행(args있음)
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		setImplementClass(query);
		return queryItf.runQuery(removeQueryGubun(query), idxVals);
	}
	
	// 다수의 쿼리 실행(args없음)
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		setImplementClass(queryList);
		return queryItf.runQuery(removeQueryGubun(queryList), new String[] { "" });
	}
	
	// 다수의 쿼리 실행(args있음)
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		setImplementClass(queryList);
		return queryItf.runQuery(removeQueryGubun(queryList), idxVals);
	}
}
