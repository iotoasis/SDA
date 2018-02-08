package com.pineone.icbms.sda.sf.mongo;

import java.util.List;
import java.util.Map;

/**
 *   MongoDB를 사용하기 위한 인터페이스
 */
public interface MongoQueryItf {
	/**
	 * class를 이용한 query
	 * @throws Exception
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> runMongoQueryByClass () throws Exception;
}