package com.pineone.icbms.sda.comm.sch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

/**
 * 스케줄러용 DAO
 */
public class Sch2DAO {
	
	// mapper에 접근하기 위한 SqlSession
	private SqlSession sqlSession;
	
	public Sch2DAO(SqlSession sqlSession) {
		super();
		this.sqlSession = sqlSession;
	}

	/**
	 * 목록 조회
	 * @throws Exception
	 * @return List<SchDTO>
	 */
	public List<SchDTO> selectList() throws Exception {
		return  sqlSession.selectList("sch.selectList");
	}

	/**
	 * 단건조회
	 * @param commandMap
	 * @throws Exception
	 * @return SchDTO
	 */
	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		return sqlSession.selectOne("sch.selectOne", commandMap);
	}

	/**
	 * 최종 작업시간 업데이트
	 * @param schDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateLastWorkTime(SchDTO schDTO) throws Exception {
		return sqlSession.update("sch.updateLastWorkTime", schDTO);
	}

	/**
	 * 종료시간 업데이트
	 * @param schHistDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception {
		return sqlSession.update("sch.hist.updateFinishTime", schHistDTO);
		
	}

}
