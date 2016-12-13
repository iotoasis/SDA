package com.pineone.icbms.sda.sf;

import java.util.List;
import java.util.Map;

public interface QueryItf {
	 List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception;
}
