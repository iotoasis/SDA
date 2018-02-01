package com.pineone.icbms.sda.sf.mongo;

import java.util.List;
import java.util.Map;

/**
 *   MongoDB를 사용하기 위한 인터페이스
 */
public interface MongoQueryItf {
	public List<Map<String, String>> runMongoQueryByClass () throws Exception;
}