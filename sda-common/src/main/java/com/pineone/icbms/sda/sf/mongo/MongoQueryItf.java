package com.pineone.icbms.sda.sf.mongo;

import java.util.List;
import java.util.Map;

public interface MongoQueryItf {
	public List<Map<String, String>> runMongoQueryByClass () throws Exception;
}