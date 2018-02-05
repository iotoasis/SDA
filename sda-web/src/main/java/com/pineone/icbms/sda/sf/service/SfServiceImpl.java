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
import com.pineone.icbms.sda.sf.MultiPurposeQueryService;
import com.pineone.icbms.sda.sf.QueryServiceFactory;

/**
 * 시멘틱프레임 서비스 구현체
 */
@Service("sfService")
public class SfServiceImpl implements SfService{ 
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="cmDAO")
	private CmDAO cmDAO;
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO;

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.service.SfService#getContext(java.util.Map)
	 */
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
			queryList.add(cmCiDTO.getSparql()+Utils.SPLIT_STR+cmCiDTO.getCi_gubun());
		}
		
		List<Map<String, String>> returnList  = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL).runQuery(queryList);
		
		log.debug("queryList in getContext by cmid =>"+queryList);
		log.debug("returnList in getContext by cmid =>"+returnList);
		
		return returnList;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.service.SfService#getContext(java.util.Map, java.lang.String)
	 */
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
			
			if(cmCiDTO.getCm_arg_cnt() == 0 && cmCiDTO.getCi_arg_cnt() == 0) {
				// pass
			} else {
				if(cmCiDTO.getCm_arg_cnt() != cmCiDTO.getCi_arg_cnt()) {
					throw new UserDefinedException(HttpStatus.BAD_REQUEST, "Not a valid argument count ! ");
				}
			}
			
			queryList.add(cmCiDTO.getSparql()+Utils.SPLIT_STR+cmCiDTO.getCi_gubun());
		}
		
		MultiPurposeQueryService mpQueryService = new MultiPurposeQueryService();
		List<Map<String, String>> returnList = mpQueryService.runQuery(queryList, args.split(","));
		
		// 쿼리실행결과를 로그로 남김
		log.debug("queryList in getContext by cmid with args =>"+queryList);
		log.debug("returnList in getContext by cmid with args =>"+returnList);
		
		return returnList;
	}
}