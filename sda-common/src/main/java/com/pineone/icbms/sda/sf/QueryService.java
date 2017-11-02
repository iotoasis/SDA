package com.pineone.icbms.sda.sf;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

public  class QueryService extends QueryCommon {
	private final Log log = LogFactory.getLog(this.getClass());
	
	private QueryItf queryItf;
	private Enum<?> QueryGubun;
	
	private QueryService() {
		super();
	}

	public Enum<?> getQueryGubun() {
		return QueryGubun;
	}

	public void setQueryGubun(Enum<?> queryGubun) {
		QueryGubun = queryGubun;
	}

	public QueryService(Enum<?> queryGubun, QueryItf queryItf) {
		this.QueryGubun = queryGubun;
		this.queryItf = queryItf;
	}
	
	public QueryItf getImplementClass() throws Exception{
			return this.queryItf;
	}

	// 쿼리 실행(args없음)
	public List<Map<String, String>> runQuery(String query) throws Exception {
		return queryItf.runQuery(removeQueryGubun(query), new String[] { "" });
	}

	// 쿼리 실행(args있음)
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		return queryItf.runQuery(removeQueryGubun(query), idxVals);
	}
	
	// 다수의 쿼리 실행(args없음)
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		return queryItf.runQuery(removeQueryGubun(queryList), new String[] { "" });
	}
	
	// 다수의 쿼리 실행(args있음)
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		return queryItf.runQuery(removeQueryGubun(queryList), idxVals);
	}
	
	public static void main(String[] args) {
		QueryService sparqlService = new QueryService();
		
		
		//  hasContentValue
		String delete_val_ql =  ""
				+" prefix o: <http://www.iotoasis.org/ontology/> "
				+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "  
				+" delete  { <@{arg0}> o:hasContentValue ?o . } "
				+" WHERE   { <@{arg0}> o:hasContentValue  ?o  .} "   ;
		String insert_val_ql =   ""
				+" prefix o: <http://www.iotoasis.org/ontology/> "
				+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "
				+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+" insert data { <@{arg0}> o:hasContentValue @{arg1} . }" ;

		// DM에 hasContentValue 생성(DM)
		String val = "[HUB_VER: 161205_ONE_M2M_v2.0], [HUB_ID : T1ENG_605_HUB04], [HUB_IP : 192.168.0.33]";
		//sparqlService.updateSparql(delete_val_ql, insert_val_ql, new String[]{"http://www.aaa.com/aaa", "\""+val+"\"^^xsd:string"}, Utils.QUERY_DEST.DM.toString());
		
		
		String madeQl = "";
		try { 
		   madeQl = sparqlService.makeFinal(insert_val_ql, new String[]{"http://www.aaa.com/aaa", "\""+val+"\"^^xsd:string"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(madeQl);
		
		/*
		try {
			System.out.println("now1 ==>" + new Date());
			System.out.println("result ===>" + sparqlService.makeFinal("aaa @{now+10, second, mmss}", new String[] { "" }));
			System.out.println("result ===>" + sparqlService.makeFinal("aaa @{now+50, minute, HHmm}", new String[] { "" }));

		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	// AwareHist에 finish time, work_result, triple_file_name, triple_check_result를 update
	/*
	public int updateFinishTime(String cmid, String ciid, String start_time, String finish_time, String work_result) throws Exception {
		log.debug("updateFinishTime of AwareHist start.....");
		
		int updateCnt = 0;
		AwareHistDTO awareHistDTO = new AwareHistDTO();
		
		awareHistDTO.setCmid(cmid);
		awareHistDTO.setCiid(ciid);

		awareHistDTO.setStart_time(start_time);
		awareHistDTO.setFinish_time(finish_time);
		awareHistDTO.setWork_result(work_result);
		
		awareHistDTO.setUuser(user_id);

		List<AwareHistDTO> list = new ArrayList<AwareHistDTO>();
		Map<String, Object> updateMap = new HashMap<String, Object>();
		list.add(awareHistDTO);
		updateMap.put("list", list);
		try {
			AwareService awareService = new AwareServiceImpl();			
			updateCnt = awareService.updateFinishTime(updateMap);
		} catch (Exception e) {
			throw e;
		}
		
		log.debug("updateFinishTime of AwareHist end.....");
		return updateCnt;
	}
	*/
	
	/*
	// awareHist테이블에 데이타 insert
	public int insertAwareHist(String cmid, String ciid, String start_time) throws Exception {
		log.debug("insertAwareHist start....");
		int updateCnt = 0;
		
		AwareHistDTO awareHistDTO = new AwareHistDTO();
		
		awareHistDTO.setCmid(cmid);
		awareHistDTO.setCiid(ciid);
		
		awareHistDTO.setStart_time(start_time);

		awareHistDTO.setCuser(user_id);
		awareHistDTO.setUuser(user_id);

		List<AwareHistDTO> list = new ArrayList<AwareHistDTO>(); 
		Map<String, List<AwareHistDTO>> insertAwareHistMap = new HashMap<String, List<AwareHistDTO>>();
		list.add(awareHistDTO);
		insertAwareHistMap.put("list", list);
		try {
			AwareService awareService = new AwareServiceImpl();
			updateCnt = awareService.insertAwareHist(insertAwareHistMap);
		} catch (Exception e) {
			throw e;
		}
		
		log.debug("insertAwareHist  end.....");		
		return updateCnt;
	}
	*/
}
