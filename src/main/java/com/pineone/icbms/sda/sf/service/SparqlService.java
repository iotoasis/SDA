package com.pineone.icbms.sda.sf.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

public class SparqlService {

	private final Log log = LogFactory.getLog(this.getClass());

	// sparql 쿼리 실행(args없음)
	public List<Map<String, String>> runSparql(String sparql) throws Exception {
		return runSparql(sparql, new String[] { "" });
	}

	// sparql 쿼리 실행(args있음)
	public List<Map<String, String>> runSparql(String sparql, String[] idxVals) throws Exception {
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		String madeQl = "";

		log.info("runSparql start ======================>");

		madeQl = makeSparql(sparql, idxVals);

		sparql = Utils.getSparQlHeader() + madeQl.toString();

		log.debug("final sparql==========>\n" + sparql);

		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, sparql);

		ResultSet rs = queryExec.execSelect();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String vName = "";
		int m = 0;
		for (; rs.hasNext();) {
			QuerySolution qs = rs.nextSolution();
			Iterator<String> itr = qs.varNames();
			Map<String, String> map = new HashMap<String, String>();
			int n = 0;
			while (itr.hasNext()) {
				vName = (String) itr.next();
				log.debug("vName[" + (m) + "][" + (n++) + "]==================>" + vName);

				if (qs.get(vName).isLiteral()) {
					log.debug("this is Literal............................");
					Literal literal = qs.getLiteral(vName);
					map.put(vName, String.valueOf(literal.getValue()));
				} else if (qs.get(vName).isResource()) {
					log.debug("this is Resource............................");

					map.put(vName, qs.getResource(vName).toString());

				} else {
					log.debug("this is unKnown QuerySolution type............................");
				}
			}
			list.add(map);
			m++;
		}

		log.info("runSparql end ======================>");
		return list;
	}

	// 쿼리에 있는 변수를 적절한 값으로 치환하여 리턴함
	public String makeSparql(String sparql, String[] idxVals) throws Exception {
		int cnt = 0;

		StringBuffer parseQl = new StringBuffer();
		Date now = new Date();
		String addStr = "";
		String lastStr = "";
		String argStr = "";
		String idx = "";
		int skipCnt = 0;

		if (idxVals == null) {
			log.debug("idxVals is null");
		} else {
			log.debug("count of idxVals : " + idxVals.length);
			log.debug("values of idxVals : " + Arrays.toString(idxVals));
		}

		if (sparql == null || sparql.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST, "sparql is null or none !");
		}

		log.debug("sparql to make ===========>\n" + sparql);

		while (!sparql.equals("")) {
			try {
				addStr = sparql.substring(0, sparql.indexOf("@{"));
			} catch (StringIndexOutOfBoundsException e) { // 더이상 "@{"이 없다면 나머지
															// 문자열은 그대로 적용
				parseQl.append(sparql);
				break;
			}

			lastStr = sparql.substring(sparql.indexOf("@{"));
			skipCnt += 2;
			argStr = lastStr.substring(skipCnt, lastStr.indexOf("}"));

			// log.debug("addStr[" + cnt + "]==========>" + addStr);
			// log.debug("lastStr[" + cnt + "]==========>" + lastStr);
			// log.debug("argStr[" + cnt + "]==========>" + argStr);

			parseQl.append(addStr);
			if (argStr.equals("systime")) {
				parseQl.append(Utils.systimeFormat.format(now));
				skipCnt += 7;
			} else if (argStr.equals("sysdate")) {
				parseQl.append(Utils.sysdateFormat.format(now));
				skipCnt += 7;
			} else if (argStr.equals("todayzero")) {
				parseQl.append(Utils.sysdateFormat2.format(now) + "T00:00:00");
				skipCnt += 9;
			} else if (argStr.equals("sysdatetime")) {
				parseQl.append(Utils.sysdatetimeFormat.format(now));
				skipCnt += 11;
			} else if (argStr.equals("sysweekday")) {
				String d = Utils.sysweekdayFormat.format(now);
				String dStr = "";
				if (d.equals("1")) { // 월요일
					dStr = "monday";
				} else if (d.equals("2")) {
					dStr = "tuesday";
				} else if (d.equals("3")) {
					dStr = "wednesday";
				} else if (d.equals("4")) {
					dStr = "thursday";
				} else if (d.equals("5")) {
					dStr = "friday";
				} else if (d.equals("6")) {
					dStr = "saturday";
				} else if (d.equals("7")) { // 일요일
					dStr = "sunday";
				}
				parseQl.append(dStr);
				skipCnt += 10;
			} else if (argStr.startsWith("arg")) {
				skipCnt += 3;
				idx = argStr.substring(3); // "arg"이후의 숫자값을 취함
				for (int i = 0; i < 100; i++) {
					if (Integer.parseInt(idx) == i) {
						// log.debug("idx.length() of argXX===========>" +
						// idx.length());
						parseQl.append(idxVals[i]);
						skipCnt += idx.length();
						break;
					}
				}
			} else {
				skipCnt += argStr.length();
				parseQl.append("@{" + argStr + "}");
			}

			// skipCnt만큼 지난 이후의 나머지 문자열을 설정
			// log.debug("skipCnt[" + cnt + "]==========>" + skipCnt);
			sparql = lastStr.substring(skipCnt + 1); // '}'에 대한 1 증가
			skipCnt = 0;
			lastStr = "";
			argStr = "";
			// cnt++;
		} // end of while

		log.debug("sparql made ===========>\n" + parseQl.toString());

		return parseQl.toString();
	}

	// sparql 쿼리결과 만들기(argument가 없음)
	public List<Map<String, String>> runSparqlUniqueResult(List<String> sparQlList) throws Exception {
		String[] args = null;
		return runSparqlUniqueResult(sparQlList, args);
	}

	// sparql 쿼리결과 만들기(argument가 있음)
	public List<Map<String, String>> runSparqlUniqueResult(List<String> sparQlList, String[] idxVals) throws Exception {

		// sparQlList의 쿼리를 수행한 결과를 모두 담고 있는 List
		List<List<Map<String, String>>> query_result_list = new ArrayList<List<Map<String, String>>>();

		// sparQl한개의 수행결과를 담고 있는 List
		List<Map<String, String>> query_result;

		boolean haveNullResult = false;

		// return할 최종결과 List
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

		log.debug("idxVals in getSparqlResult()================>" + Arrays.toString(idxVals));

		// sparql 실행해서 구분자로 분리하여 list에 담는다.
		// 1. 모든 줄에 있는 값을 찾아야 하므로 값이 없는 row가 있으면 바로 return
		for (int i = 0; i < sparQlList.size(); i++) {
			query_result = runSparql(sparQlList.get(i), idxVals);
			log.debug("query_result[" + i + "]  =========> \n" + query_result.toString());
			if (query_result.size() == 0) {
				haveNullResult = true;
				break;
			} else {
				query_result_list.add(query_result);
				log.debug("query_result.size();" + query_result.size());
			}
		}

		log.debug("query_result_list.size() ==>" + query_result_list.size());
		log.debug("haveNullResult ==>" + haveNullResult);

		// 제일 작은 개수를 찾기위해서 개수및 idx 만으로 이루어진 임시 List를 만듬
		List<Cnt> cntList = new ArrayList<Cnt>();

		if (haveNullResult == false) {
			for (int i = 0; i < query_result_list.size(); i++) {
				Cnt cnt = new Cnt();
				cnt.setCnt(query_result_list.get(i).size());
				cnt.setIdx(i);
				cntList.add(cnt);
			}
		}

		// 2. 건수가 제일 작은 것을 기준으로 찾아야함.
		if (haveNullResult == false && query_result_list.size() > 1) {
			Collections.sort(cntList, new CntCompare());
			int idx = cntList.get(0).getIdx(); // 첫번째 값이 제일 작은값(개수가 제일작은..)
			List<Map<String, String>> stdList = query_result_list.get(idx); // 기준이 되는 List를 추출
			query_result_list.remove(idx); // idx에 속하는 List는 제거하여 중복체크되지 않도록 함

			log.debug("stdList =========> " + stdList.toString());
			log.debug("idx ==>" + idx);

			// 제일 작은 개수 List를 기준으로 체크한다.
			int matchCnt = 0;
			for (int i = 0; i < stdList.size(); i++) {
				for (int k = 0; k < query_result_list.size(); k++) {
					if (query_result_list.get(k).contains(stdList.get(i))) {
						matchCnt++;
						log.debug("query_result_list.get(" + k + ").contains(stdList.get(" + i + ")) == true");
						break;
					}
					if (matchCnt == 0)
						break; // 일치하는 것이 하나라도 확인되면 더이상 체크할 필요 없음
				} // List 순환 end
					// matchCnt와 query_result_list의 개수가 같으면 stdList의 값과 일치하는 값이
					// query_result_list각 로우에
					// 있다는 의미임
				if (matchCnt == query_result_list.size()) {
					returnList.add(stdList.get(i));
				}
			}
			log.debug("matchCnt========>" + matchCnt);
			// 결과값이 one row이면 내부 값을 모두 리턴해줌
		} else if (haveNullResult == false && query_result_list.size() == 1) {
			returnList = query_result_list.get(0);
		} else {
			// pass
		}
		return returnList;
	}

	public void updateSparql(String deleteql, String insertql, String[] idxVals) throws Exception {
		// delete
		deleteSparql(deleteql, idxVals);
		// insert
		insertSparql(deleteql, idxVals);
	}
	
	public void deleteSparql(String deleteql, String[] idxVals) throws Exception {
		String updateService = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint") + "/update";

		// delete
		String madeDeleteQl = makeSparql(deleteql, idxVals);
		UpdateRequest ur = UpdateFactory.create(madeDeleteQl);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(ur, updateService);
		up.execute();
	}

	
	public void insertSparql(String insertql, String[] idxVals) throws Exception {
		String updateService = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint") + "/update";

		// insert
		String madeInsertQl = makeSparql(insertql, idxVals);
		UpdateRequest ur = UpdateFactory.create(madeInsertQl);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(ur, updateService);
		up.execute();
	}


	// cnt를 담고 있는 임시 Cnt클래스
	private class Cnt {
		int idx;
		int cnt;

		int getCnt() {
			return cnt;
		}

		void setCnt(int cnt) {
			this.cnt = cnt;
		}

		int getIdx() {
			return idx;
		}

		void setIdx(int idx) {
			this.idx = idx;
		}
	}

	// 숫자 비교용 클래스(내림차순, DESC)
	private class CntCompare implements Comparator<Cnt> {
		@Override
		public int compare(Cnt arg0, Cnt arg1) {
			return arg0.getCnt() > arg1.getCnt() ? -1 : arg0.getCnt() < arg1.getCnt() ? 1 : 0;
		}
	}
}
