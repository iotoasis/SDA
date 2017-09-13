package com.pineone.icbms.sda.sf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pineone.icbms.sda.comm.util.Utils;

public class QueryServiceFactory {
	static final Log log = LogFactory.getLog(QueryServiceFactory.class);
	
	//static QueryItf queryItf; 
	
	private QueryServiceFactory() {
	}

	private QueryServiceFactory(QueryItf queryItf) {
	}
	
	static public QueryService create(Utils.QUERY_GUBUN queryGubun) throws Exception {
		QueryService queryService;
			if(queryGubun == Utils.QUERY_GUBUN.MARIADBOFGRIB){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.MARIADBOFGRIB.toString());
				queryService = new QueryService(queryGubun, new MariaDbOfGribQueryImpl());
			} else if(queryGubun == Utils.QUERY_GUBUN.MARIADBOFSDA){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.MARIADBOFSDA.toString());
				queryService = new QueryService(queryGubun, new MariaDbOfSdaQueryImpl());
			} else if(queryGubun == Utils.QUERY_GUBUN.FUSEKISPARQL){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.FUSEKISPARQL.toString());
				queryService = new QueryService(queryGubun, new SparqlFusekiQueryImpl());
			} else if(queryGubun == Utils.QUERY_GUBUN.HALYARDSPARQL){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.HALYARDSPARQL.toString());
				queryService = new QueryService(queryGubun, new SparqlHalyardQueryImpl());
			} else if(queryGubun == Utils.QUERY_GUBUN.MONGODB){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.MONGODB.toString());
				queryService = new QueryService(queryGubun, new MongoDbQueryImpl());
			} else if(queryGubun == Utils.QUERY_GUBUN.SHELL){
				log.debug("query gubun : "+Utils.QUERY_GUBUN.SHELL.toString());
				queryService = new QueryService(queryGubun, new ShellQueryImpl());
			} else {
				return null;
			}
			return queryService;
	}

}
