package com.pineone.icbms.sda.sf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.sf.service.SparqlService;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.cm.dao.CmDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;

@Service("sfService")
public class SfServiceImpl implements SfService{ 
	private final Log log = LogFactory.getLog(this.getClass());
	private SparqlService sparqlService = new SparqlService();
	
	@Resource(name="cmDAO")
	private CmDAO cmDAO;
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO;

	
	//test수행(ciid여러개를 이용하여 test수행)
	public List<Map<String, String>> test(RequestDTO requestDTO) throws Exception {
		//List<String> list = new ArrayList<String>();
		String[] ciids;
		
		if( ! requestDTO.getExecution_type().equals("test") || requestDTO.getCiid().equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}
		ciids = requestDTO.getCiid().split(",");
		List<CiDTO> list= ciDAO.selectSparqlListByCiids(ciids);
		
		// 요청한 ciids개수와 리턴되는 개수가 다르면 동일한 ciid를 지정했거나 등록되지 않은 ciid를 지정한 경우이므로 오류를 발생시킴
		if(ciids.length != list.size()) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND, "Check out your Ciids ! ");
		}
		
		List<String> sparqlList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			CiDTO ciDTO = list.get(i);
			sparqlList.add(ciDTO.getSparql());
		}
		
		List<Map<String, String>> returnList = sparqlService.runSparqlUniqueResult(sparqlList);
		
		log.debug("sparqlList in test by ciids =>"+sparqlList);
		log.debug("returnList in test by ciids =>"+returnList);
		
		return returnList;
	}

	//test수행(cmid를 받아서 test수행)
	public List<Map<String, String>> test(Map<String, Object> commandMap) throws Exception {
		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		String cmid = (String) commandMap.get("cmid");
		
		if( cmid == null || cmid.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}
		
		// cmid로 sparql목록을 가져옴
		list = cmDAO.selectCmCiList(commandMap);
		
		if( list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}
		
		List<String> sparqlList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			CmCiDTO cmCiDTO = list.get(i);
			sparqlList.add(cmCiDTO.getSparql());
		}
		
		List<Map<String, String>> returnList = sparqlService.runSparqlUniqueResult(sparqlList);
		
		log.debug("sparqlList in test by cmid =>"+sparqlList);
		log.debug("returnList in test by cmid =>"+returnList);
		
		return returnList;
	}

	
	//test수행(cmid및 쿼리조건을 받아서 test수행)
	public List<Map<String, String>> test(Map<String, Object> commandMap, String args) throws Exception {
		List<CmCiDTO> list = new ArrayList<CmCiDTO>();
		String cmid = (String) commandMap.get("cmid");
		
		if( cmid == null || cmid.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}

		if( args == null || args.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}

		// cmid로 sparql목록을 가져옴
		list = cmDAO.selectCmCiList(commandMap);
		
		if( list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}
		
		// arg개수 일치여부 확인(인수로 지정된 개수 확인)
		if( args.split(",").length != list.get(0).getCm_arg_cnt()) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not valid argument count ! ");
		}
		
		// 실행할 sparql을 추출하며 인수 개수(cm과 ci의 개수 확인)가 동일한지 확인하고 다르면 exception을 발생시킨다.
		List<String> sparqlList = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			CmCiDTO cmCiDTO = list.get(i);
			if(cmCiDTO.getCm_arg_cnt() == 0 && cmCiDTO.getCi_arg_cnt() == 0) {
				// pass
			} else {
				if(cmCiDTO.getCm_arg_cnt() != cmCiDTO.getCi_arg_cnt()) {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not valid argument count ! ");
				}
			}
			sparqlList.add(cmCiDTO.getSparql());
		}
		
		// 쿼리 조건을 인자로 받음
		List<Map<String, String>> returnList = sparqlService.runSparqlUniqueResult(sparqlList, args.split(","));
		
		// 쿼리실행결과를 로그로 남김
		
		
		log.debug("sparqlList in test by cmid with args =>"+sparqlList);
		log.debug("returnList in test by cmid with args =>"+returnList);
		
		return returnList;
	}
	
	// SO스케줄등에서 사용됨
	/*
	public List<CmCiDTO> selectCmCiList(Map<String, Object> commandMap) throws Exception {
		return cmDAO.selectCmCiList(commandMap);
	}
	*/

}
