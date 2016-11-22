package com.pineone.icbms.sda.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

@Repository("schDAO")
public class SchDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<SchDTO> selectList() throws Exception {
		return (List<SchDTO>) selectList("sch.selectList");
	}

	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		return (SchDTO) selectOne("sch.selectOne", commandMap);
	}

	public int updateLastWorkTime(SchDTO schDTO) throws Exception {
		return Integer.parseInt(update("sch.updateLastWorkTime", schDTO).toString());
	}

	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception {
		return Integer.parseInt(update("sch.hist.updateFinishTime", schHistDTO).toString());
	}

}
