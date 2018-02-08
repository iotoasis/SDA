package com.pineone.icbms.sda.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.sch.dto.AggrDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO; 

/**
 * 집계 작업용 DAO
 */
@Repository("aggrDAO")
public class AggrDAO extends AbstractDAO {
	@SuppressWarnings("unchecked")
	public List<AggrDTO> selectList(Map<String, String> commandMap) throws Exception {
		return (List<AggrDTO>) selectList("aggr.selectList", commandMap);
	}
}