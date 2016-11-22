package com.pineone.icbms.sda.comm.sch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

public class Sch2DAO {
	
	// mapper에 접근하기 위한 SqlSession
	private SqlSession sqlSession;
	
	public Sch2DAO(SqlSession sqlSession) {
		super();
		this.sqlSession = sqlSession;
	}

	public List<SchDTO> selectList() throws Exception {
		return  sqlSession.selectList("sch.selectList");
	}

	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		return sqlSession.selectOne("sch.selectOne", commandMap);
	}

	public int updateLastWorkTime(SchDTO schDTO) throws Exception {
		return sqlSession.update("sch.updateLastWorkTime", schDTO);
	}

	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception {
		return sqlSession.update("sch.hist.updateFinishTime", schHistDTO);
		
	}

}
