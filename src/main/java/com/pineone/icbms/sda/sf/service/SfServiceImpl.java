package com.pineone.icbms.sda.sf.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.dto.RequestDTO;
import com.pineone.icbms.sda.comm.dto.ResponseMessage;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
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

	
	//getContext수행(ciid여러개를 이용하여 getContext수행)
	public List<Map<String, String>> getContext(RequestDTO requestDTO) throws Exception {
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
		
		log.debug("sparqlList in getContext by ciids =>"+sparqlList);
		log.debug("returnList in getContext by ciids =>"+returnList);
		
		return returnList;
	}

	//getContext수행(cmid를 받아서 getContext수행)
	public List<Map<String, String>> getContext(Map<String, Object> commandMap) throws Exception {
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
		
		log.debug("sparqlList in getContext by cmid =>"+sparqlList);
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
		
		
		log.debug("sparqlList in getContext by cmid with args =>"+sparqlList);
		log.debug("returnList in getContext by cmid with args =>"+returnList);
		
		return returnList;
	}
	
	// test
	//getContext2수행(cmid및 쿼리조건을 받아서 getContext2수행)
	public List<Map<String, String>> getContext2(String cmid, String args) throws Exception {
		Gson gson = new Gson();
		if( cmid == null || cmid.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}

		if( args == null || args.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}

		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		String mode = "";

		if(cmid.equals("CM-1-2-100")) {	 // http://localhost:8080/sda/ctx2/CM-1-2-100/?p=http://www.pineone.com/LB0001
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-140")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("dev", arg[0]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-2-150")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("dev", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-160")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-170")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-200")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-210")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-220")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("person_id", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-230")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-240")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-250")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-260")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-270")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-2-280")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-290")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   

		} else if(cmid.equals("CM-1-2-300")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			tmp_map.put("cond", arg[1]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-310")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("person_id", arg[0]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-2-320")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-330")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-340")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-350")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-360")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-370")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-380")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-390")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-2-400")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		}
		
		
		if(cmid.equals("CM-1-1-100")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-110")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("dev", arg[0]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-1-120")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("dev", arg[0]);
			tmp_map.put("type", arg[1]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-130")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("dev", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-140")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("dev", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-150")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("student_id", arg[0]);
			tmp_map.put("lecture", arg[1]);
			tmp_map.put("cond", arg[2]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-160")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("min_value", arg[0]);
			tmp_map.put("max_value", arg[1]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-170")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("manager", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-190")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("user_id", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-200")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-210")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("uri", arg[0]);
			tmp_map.put("camurl", arg[1]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-1-220")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("user_id", arg[0]);
			tmp_map.put("student_id", arg[1]);
			tmp_map.put("name", arg[2]);
			tmp_map.put("phone", arg[3]);
			returnList.add(tmp_map);		   
		} else if(cmid.equals("CM-1-1-230")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			tmp_map.put("cond", arg[1]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-1-240")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			tmp_map.put("cond", arg[1]);
			returnList.add(tmp_map);
		} else if(cmid.equals("CM-1-1-250")) {
			mode = Utils.CALLBACK_TEST;
			Map<String, String> tmp_map = new HashMap<String, String>();
			String[] arg = args.split(",");
			tmp_map.put("loc", arg[0]);
			tmp_map.put("cond", arg[1]);
			returnList.add(tmp_map);
		}
		
		// SO에 결과 전송 시작
		String so_notice_time = Utils.dateFormat.format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmd", mode);
		map.put("contextId", cmid);
		map.put("time", so_notice_time);
		map.put("domains", returnList);

		String jsonMsg = gson.toJson(map);

		log.debug("Request message of Schedule for sending to SO =>  " + jsonMsg);
		ResponseMessage responseMessage = Utils.requestData(Utils.getSdaProperty("com.pineone.icbms.sda.so.callback_result_uri"), jsonMsg); // POST
		log.debug("responseMessage of Schedule from SO => " + responseMessage.toString());
		// SO에 결과 전송 끝
		
		// 쿼리실행결과를 로그로 남김
		log.debug("returnList in getContext2 by cmid with args =>"+returnList);
		
		return returnList;
	}}
