package com.pineone.icbms.sda.sf;

import java.util.List;
import java.util.Map;

import com.pineone.icbms.sda.comm.dto.ResponseMessage;

public interface QueryItf {
	 // select
	 List<Map<String, String>> runQuery(String query) throws Exception;
	 List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception;
	 List<Map<String, String>> runQuery(List<String> queryList) throws Exception;
	 List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception;
}
