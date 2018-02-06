package com.pineone.icbms.sda.sf;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pineone.icbms.sda.comm.util.Utils;

import scala.NotImplementedError;

/**
 *   SI DB에 쿼리수행
 */
public class MongoDbQueryImpl extends QueryCommon implements QueryItf {

	private final Log log = LogFactory.getLog(this.getClass());
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		log.info("runQuery of mongo start ======================>");

		log.debug("try (first) .................................. ");
		try {
			list = getResult(query, idxVals);
		} catch (Exception e) {
			int waitTime = 15*1000;
			log.debug("Exception message in runQuery() =====> "+e.getMessage());  
			
			try {
				log.debug("sleeping (first)................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				list = getResult(query, idxVals);
			} catch (Exception ee) {
				log.debug("Exception(1)====>"+ee.getMessage());
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
						log.debug("Exception(2)====>"+eee.getMessage());
						throw eee;
					}
				}
				throw ee;
			}
		}

		log.info("runQuery of mongo end ======================>");
		return list;
	}
	
	/**
	 *   쿼리결과를 가져온다.
	 * @param query
	 * @param idxVals
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private final List<Map<String, String>> getResult (String query, String[] idxVals) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			Class<?> workClass = Class.forName(query);
			Object newObj = workClass.newInstance();
			Method m = workClass.getDeclaredMethod("runMongoQueryByClass");
			list = (List<Map<String, String>>) m.invoke(newObj);
		} catch (Exception e) {
			log.debug(e.getMessage());
		}		
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
	}
}
