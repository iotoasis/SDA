package com.pineone.icbms.sda.itf.cmi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.cm.dao.CmDAO;
import com.pineone.icbms.sda.itf.cm.dto.CmCiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;
import com.pineone.icbms.sda.itf.cmi.dao.CmiDAO;
import com.pineone.icbms.sda.itf.cmi.dto.CmiDTO;

@Service("cmiService")
public class CmiServiceImpl implements CmiService{ 
	private Log log = LogFactory.getLog(this.getClass());

	@Resource(name="cmiDAO")
	private CmiDAO cmiDAO;
	
	// /cmi/ALL
	public List<CmiDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<CmiDTO> list = new ArrayList<CmiDTO>();
		//list = cmiDAO.selectList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	public int insert(Map<String, Object> map) throws Exception{
		return Integer.parseInt(cmiDAO.insert("itf.cmi.insert", map).toString());
	}
	
}