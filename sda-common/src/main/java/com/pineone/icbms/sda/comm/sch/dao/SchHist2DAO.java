package com.pineone.icbms.sda.comm.sch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

public class SchHist2DAO {
	
	// mapper에 접근하기 위한 SqlSession
	private SqlSession sqlSession;
	
	public SchHist2DAO(SqlSession sqlSession) {
		super();
		this.sqlSession = sqlSession;
	}


	public List<SchHistDTO> selectList() throws Exception{
		return sqlSession.selectList("sch.hist.selectList");
	}
	
	public SchHistDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (SchHistDTO) sqlSession.selectOne("sch.hist.selectOne", commandMap);
	}

	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception{
		return sqlSession.update("sch.hist.updateFinishTime", schHistDTO);
	}
	
	public int insert(SchHistDTO schHistDTO) throws Exception{
		return sqlSession.insert("sch.hist.insert", schHistDTO);
	}

}
