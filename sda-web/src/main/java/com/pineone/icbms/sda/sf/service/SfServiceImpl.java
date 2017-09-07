package com.pineone.icbms.sda.sf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.cm.dao.CmDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.sf.QueryService;
import com.pineone.icbms.sda.sf.SparqlQueryImpl;

@Service("sfService")
public class SfServiceImpl implements SfService{ 
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="cmDAO")
	private CmDAO cmDAO;
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO;

	//getContext수행(cmid를 받아서 getContext수행)
	public List<Map<String, String>> getContext(Map<String, Object> commandMap) throws Exception {
		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		String cmid = (String) commandMap.get("cmid");
		
		if( cmid == null || cmid.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}
		
		// cmid로 쿼리목록을 가져옴
		list = cmDAO.selectCmCiList(commandMap);
		
		if( list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}
		
		List<String> queryList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			CmCiDTO cmCiDTO = list.get(i);
			/*
			//query+구분값+cmid+ciid로 만들어서 보내준다.
			queryList.add(cmCiDTO.getSparql()+Utils.SPLIT_STR+cmCiDTO.getCi_gubun());
			*/
			
			queryList.add(cmCiDTO.getSparql());			
		}
		
		List<Map<String, String>> returnList= new QueryService(new SparqlQueryImpl()).runQuery(queryList);
		
		log.debug("queryList in getContext by cmid =>"+queryList);
		log.debug("returnList in getContext by cmid =>"+returnList);
		
		return returnList;
	}

	
	//getContext수행(cmid및 쿼리조건을 받아서 getContext수행)
	public List<Map<String, String>> getContext(Map<String, Object> commandMap, String args) throws Exception {
		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		String cmid = (String) commandMap.get("cmid");
		
		if( cmid == null || cmid.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}

		if( args == null || args.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}

		// cmid로 쿼리 목록을 가져옴
		list = cmDAO.selectCmCiList(commandMap);
		
		log.debug("query count ================>"+list.size());
		log.debug("query count ================>"+list.size());
		
		if( list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}
		
		// arg개수 일치여부 확인(인수로 지정된 개수 확인)
		if( args.split(",").length != list.get(0).getCm_arg_cnt()) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not a valid argument count ! ");
		}
		
		// 실행할 query를 추출하며 인수 개수(cm과 ci의 개수 확인)가 동일한지 확인하고 다르면 exception을 발생시킨다.
		List<String> queryList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			CmCiDTO cmCiDTO = list.get(i);
			
			//log.debug("cmCiDTO["+i+"]====gooper======> "+cmCiDTO.toString());
			
			if(cmCiDTO.getCm_arg_cnt() == 0 && cmCiDTO.getCi_arg_cnt() == 0) {
				// pass
			} else {
				if(cmCiDTO.getCm_arg_cnt() != cmCiDTO.getCi_arg_cnt()) {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not a valid argument count ! ");
				}
			}
			/*
			//query+구분값+cmid+ciid로 만들어서 보내준다. 
			queryList.add(cmCiDTO.getSparql()+Utils.SPLIT_STR+cmCiDTO.getCi_gubun());
			*/	
			queryList.add(cmCiDTO.getSparql());
		}
		
		// 쿼리 조건을 인자로 받음
		List<Map<String, String>> returnList = new QueryService(new SparqlQueryImpl()).runQuery(queryList, args.split(","));
		
		
		// 쿼리실행결과를 로그로 남김
		log.debug("queryList in getContext by cmid with args =>"+queryList);
		log.debug("returnList in getContext by cmid with args =>"+returnList);
		
		return returnList;
	}

}
