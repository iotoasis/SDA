package com.pineone.icbms.sda.sf;

import java.util.List;
import java.util.Map;

/**
 * 쿼리수행 인터페이스
 */
public interface QueryItf {
	 /**
	 * 변수없이 쿼리수행
	 * @param query
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	List<Map<String, String>> runQuery(String query) throws Exception;
	
	 /**
	 * 변수지정 쿼리수행
	 * @param query
	 * @param idxVals
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception;
	
	 /**
	 * 여러개의 쿼리 수행
	 * @param queryList
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	List<Map<String, String>> runQuery(List<String> queryList) throws Exception;
	
	 /**
	 * 변수지정하여 여러개의 쿼리 수행
	 * @param queryList
	 * @param idxVals
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception;
}
