package com.pineone.icbms.sda.sf.dao;

import org.apache.ibatis.session.SqlSession;
import com.pineone.icbms.sda.sf.dto.AwareHistDTO;

public class AwareHistDAO{
	// mapper에 접근하기 위한 SqlSession
	private SqlSession sqlSession;
	
	public AwareHistDAO(SqlSession sqlSession) {
		super();
		this.sqlSession = sqlSession;
	}

/*	public List<SchHistDTO> selectList() throws Exception{
		return sqlSession.selectList("sf.aware_hist.selectList");
	}
	
	public SchHistDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (SchHistDTO) sqlSession.selectOne("sf.aware_hist.selectOne", commandMap);
	}
*/
	public int updateFinishTime(AwareHistDTO awareHistDTO) throws Exception{
		return sqlSession.update("sf.aware_hist.updateFinishTime", awareHistDTO);
	}
	
	public int insert(AwareHistDTO awareHistDTO) throws Exception{
		return sqlSession.insert("sf.aware_hist.insert", awareHistDTO);
	}

}
