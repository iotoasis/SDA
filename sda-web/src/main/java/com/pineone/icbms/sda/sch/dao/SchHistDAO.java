package com.pineone.icbms.sda.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

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
		return Integer.parseInt(update("sch.hist.updateFinishTime", schHistDTO).toString());
	}
	
	public int insert(SchHistDTO schHistDTO) throws Exception{
		return Integer.parseInt(insert("sch.hist.insert", schHistDTO).toString());
	}

}
