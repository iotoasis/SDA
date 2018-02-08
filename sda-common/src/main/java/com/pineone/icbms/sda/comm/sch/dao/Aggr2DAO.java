package com.pineone.icbms.sda.comm.sch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pineone.icbms.sda.comm.sch.dto.AggrDTO;

/**
 * 집계용 DAO
 */
public class Aggr2DAO {
	
	// mapper에 접근하기 위한 SqlSession
	private SqlSession sqlSession;
	
	public Aggr2DAO(SqlSession sqlSession) {
		super();
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 목록조회
	 * @param commandMap
	 * @throws Exception
	 * @return List<AggrDTO>
	 */
	public List<AggrDTO> selectList(Map<String, String> commandMap) throws Exception {
		return sqlSession.selectList("aggr.selectList", commandMap);
	}
}