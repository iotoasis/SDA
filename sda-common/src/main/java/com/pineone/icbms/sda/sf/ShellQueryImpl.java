package com.pineone.icbms.sda.sf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pineone.icbms.sda.comm.util.Utils;

import scala.NotImplementedError;
/**
 * MongoDB에 접속하여 쿼리수행
 */
public class ShellQueryImpl extends QueryCommon implements QueryItf {

	private final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		log.info("runQuery of shell start ======================>");

		log.debug("try (first) .................................. ");
		try {
			list = getResult(query, idxVals);
		} catch (Exception e) {
			int waitTime = 15*1000;
			log.debug("Exception message in runQuery() =====> "+e.getMessage());  
			
			try {
				// 일정시간 대기 했다가 다시 수행함
				log.debug("sleeping (first)................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				list = getResult(query, idxVals);
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
						list = getResult(query, idxVals);
					} catch (Exception eee) {
						log.debug("Exception 2====>"+eee.getMessage());
						throw eee;
					}
				}
				throw ee;
			}
		}

		log.info("runQuery of shell end ======================>");
		return list;
	}
	
	/**
	 * 쿼리 수행 결과
	 * @param query
	 * @param idxVals
	 * @return
	 * @throws Exception
	 */
	private final List<Map<String, String>> getResult (String query, String[] idxVals) throws Exception {
		query = makeFinal(query, idxVals);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] result = new String[]{""};

		// query문자열 에서 필요한 실행에 필요한 부분 추출(예, python3 /svc/apps/sda/ml/predict_.py @{arg0} @{arg1})
		String[] args = query.split("\\s+");
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}
	
		// 실행
		result = Utils.runShell(sb);
		System.out.println("resultStr in runPredict() == > "+ Arrays.toString(result));
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", result[0].toString());
	    list.add(map);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.lang.String)
	 */
	@Override
	public List<Map<String, String>> runQuery(String query) throws Exception {
		return runQuery(query, new String[]{""});
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.util.List)
	 */
	@Override
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		return runQuery(queryList, new String[]{""});
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.util.List, java.lang.String[])
	 */
	@Override
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		if(queryList.size() == 1) {
			return runQuery(queryList.get(0), idxVals);
		} else {
			throw new NotImplementedError("runQuery() for many querys is not implemented");
		}
	}}
