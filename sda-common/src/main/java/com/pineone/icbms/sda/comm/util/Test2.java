package com.pineone.icbms.sda.comm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.pineone.icbms.sda.sf.IdxCnt;
  
public class Test2 {
	private static final Log log = LogFactory.getLog(Test2.class);
    /**
     * @param args
     */
    public static void main(String[] args) {
    	try {
    		List<Map<String, String>> list = getUniqueResultBySameColumn();
    		System.out.println("final list==>"+list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//String lbl_tmp = "[ \"ONDB_TicketCount01_001_ReportLabel\"]";
    	String lbl_tmp = "[ \"ONDB_TicketCount01_001_ReportLabel\"]";
    	Gson gson =new Gson();
    	
    	System.out.println(lbl_tmp);
    	
    	String[] json = gson.fromJson(lbl_tmp ,String[].class);
    	
    	for(String str : json)
    	System.out.println("adsfsdfsdf=>"+str);
    }
    
    
	// 여러개의 쿼리를 이용하여 결과 만들기(argument가 있음): 컬럼의 개수가 1개 이상이고 비교대상의 컬럼 명칭이 모두 동일한 경우 결과 값 추출하는 로직 
	private static List<Map<String, String>> getUniqueResultBySameColumn() throws Exception {

		// queryList의 쿼리를 수행한 결과를 모두 담고 있는 List
		List<List<Map<String, String>>> query_result_list = new ArrayList<List<Map<String, String>>>();

		boolean haveNullResult = false;
	
		/*
		 * 	
o:engcenter_616
	
o:t1eng_605
		 * 
		 */
		List<Map<String, String>> query_result1= new ArrayList<Map<String, String>>();
		List<Map<String, String>> query_result2= new ArrayList<Map<String, String>>();
		List<Map<String, String>> query_result3= new ArrayList<Map<String, String>>();
		
		Map<String, String> map1_1 = new HashMap<String, String>(); map1_1.put("loc", "o:1"); query_result1.add(map1_1);
		//Map<String, String> map1_2 = new HashMap<String, String>(); map1_2.put("loc", "o:1"); query_result1.add(map1_2);
		//Map<String, String> map1_3 = new HashMap<String, String>(); map1_3.put("loc", "o:3"); query_result1.add(map1_3);
			
		Map<String, String> map2_1 = new HashMap<String, String>();   map2_1.put("loc", "o:11");  query_result2.add(map2_1);
		//Map<String, String> map2_2 = new HashMap<String, String>();   map2_2.put("loc", "o:2");  query_result2.add(map2_2);
   	    //Map<String, String> map2_3 = new HashMap<String, String>();   map2_3.put("loc", "o:33");  query_result2.add(map2_3);
   	    //Map<String, String> map2_4 = new HashMap<String, String>();   map2_4.put("loc", "o:1");  query_result2.add(map2_4);

	
		Map<String, String> map3_1 = new HashMap<String, String>();  map3_1.put("loc", "o:1"); query_result3.add(map3_1);
		Map<String, String> map3_2 = new HashMap<String, String>();  map3_2.put("loc", "o:2"); query_result3.add(map3_2);
		Map<String, String> map3_3 = new HashMap<String, String>();  map3_3.put("loc", "o:3"); query_result3.add(map3_3);
		Map<String, String> map3_4 = new HashMap<String, String>();  map3_4.put("loc", "o:4"); query_result3.add(map3_4);

		// 중복제거
		query_result1 = distinctList(query_result1);
		query_result2 = distinctList(query_result2);
		query_result3 = distinctList(query_result3);
		
		query_result_list.add(query_result1);
		query_result_list.add(query_result2);
		//query_result_list.add(query_result3);
		
		// 결과값의 전체 개수
		int total_query_result_list_count = query_result_list.size();
		log.debug("query_result_list =========> " + query_result_list.toString());

		log.debug("total_query_result_list.size() ==>" + total_query_result_list_count);
		log.debug("haveNullResult ==>" + haveNullResult);

		// 제일 작은 개수를 찾기위해서 개수및 idx 만으로 이루어진 임시 List를 만듬
		List<IdxCnt> cntList = new ArrayList<IdxCnt>();

		if (haveNullResult == false) {
			for (int i = 0; i < query_result_list.size(); i++) {
				IdxCnt cnt = new IdxCnt();
				cnt.setCnt(query_result_list.get(i).size());
				cnt.setIdx(i);
				cntList.add(cnt);
			}
		}
		log.debug("cntList =========> " + cntList);
		
		// return할 최종결과 List
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		int matchedRowCnt = 0;
		// 2. 건수가 제일 작은 것을 기준으로 찾아야함.
		if (haveNullResult == false && total_query_result_list_count > 1) {
			Collections.sort(cntList, new CntCompare());
			log.debug("sorted cntList =========> " + cntList);
			
			int idx = cntList.get(0).getIdx(); 			// 개수가 제일 작은것을 찾는다.
			List<Map<String, String>> stdList = query_result_list.get(idx); // 비교시 기준이 되는 List를 추출한다.
			query_result_list.remove(idx); 				// idx에 속하는 List는 제거하여 중복체크되지 않도록 함

			log.debug("stdList =========> " + stdList.toString());
			log.debug("removed query_result_list =========> " + query_result_list.toString());

			// 제일 작은 개수 List를 기준으로 체크한다.
			for (int i = 0; i < stdList.size(); i++) {
				matchedRowCnt = 0;
				log.debug("stdList.get(" + i + ") :"+stdList.get(i));
				for (int k = 0; k < query_result_list.size(); k++) {
					log.debug("query_result_list.get(" + k + ") :" +query_result_list.get(k));
					
					if (query_result_list.get(k).contains(stdList.get(i))) {
						matchedRowCnt++;
						log.debug("query_result_list.get(" + k + ").contains(stdList.get(" + i + ")) == true");
					}
				} // List 순환 end
				
				log.debug("matchedRowCnt of "+stdList.get(i)+" ===> "+matchedRowCnt);
				if(matchedRowCnt == (total_query_result_list_count-1)) {
					returnList.add(stdList.get(i));
				}
			}
		} else if (haveNullResult == false && total_query_result_list_count  == 1) {       			// 결과값이 1개의 row만을 가지고 있으면 내부 값을 모두 리턴해줌
			log.debug("total_query_result_list_count is 1  =========> " + query_result_list.get(0));
			returnList = query_result_list.get(0);
		} else {
			// pass
		}
		
		return distinctList(returnList);

	}
	
	private static  List<Map<String, String>> distinctList(List<Map<String, String>> list) {
		//중복 제거
		// HashSet 데이터 형태로 생성되면서 중복 제거됨
		HashSet<Map<String,String>> hs = new HashSet<Map<String, String>>(list);

		// ArrayList 형태로 다시 생성
		ArrayList<Map<String, String>> returnList2 = new ArrayList<Map<String, String>>(hs);
		return returnList2;
	}
  
}


// 숫자 비교용 클래스(내림차순, asc)
final class CntCompare implements Comparator<IdxCnt> {
	@Override
	public int compare(IdxCnt arg0, IdxCnt arg1) {
		//desc
		//return arg0.getCnt() > arg1.getCnt() ? -1 : arg0.getCnt() < arg1.getCnt() ? 1 : 0;
		
		//asc
		return arg0.getCnt() < arg1.getCnt() ? -1 : arg0.getCnt() > arg1.getCnt() ? 1 : 0;
	}
	
	class lbl_class {
		String[] lbls;

		public String[] getLbls() {
			return lbls;
		}

		public void setLbls(String[] lbls) {
			this.lbls = lbls;
		}

		
		
	}
	
	
}