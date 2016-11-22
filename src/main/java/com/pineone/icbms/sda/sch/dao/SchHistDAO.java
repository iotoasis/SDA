package com.pineone.icbms.sda.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.sch.dto.SchHistDTO;

@Repository("schHistDAO")
public class SchHistDAO extends AbstractDAO{
	@SuppressWarnings("unchecked")
	public List<SchHistDTO> selectList() throws Exception{
		return selectList("sch.hist.selectList");
	}
	
	public SchHistDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (SchHistDTO)selectOne("sch.hist.selectOne", commandMap);
	}

	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception{
		return (int)update("sch.hist.updateFinishTime", schHistDTO);
	}
	
	public int insert(SchHistDTO schHistDTO) throws Exception{
		return (int)insert("sch.hist.insert", schHistDTO);
	}

}
