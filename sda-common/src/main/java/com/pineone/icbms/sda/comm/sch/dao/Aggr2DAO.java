package com.pineone.icbms.sda.comm.sch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import com.pineone.icbms.sda.comm.sch.dto.AggrDTO;

public class Aggr2DAO {
	
	// mapper에 접근하기 위한 SqlSession
	private SqlSession sqlSession;
	
	public Aggr2DAO(SqlSession sqlSession) {
		super();
		this.sqlSession = sqlSession;
	}
	
	public List<AggrDTO> selectList(Map<String, String> commandMap) throws Exception {
		return sqlSession.selectList("aggr.selectList", commandMap);
	}
	
	public AggrDTO selectOne_(Map<String, String> commandMap) throws Exception {
		return sqlSession.selectOne("aggr.selectOne", commandMap);
	}
	
	
}