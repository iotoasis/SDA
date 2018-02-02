package com.pineone.icbms.sda.sch.comm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DB접속 추상 DAO
 */
public class AbstractDAO {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	protected void printQueryId(String queryId) {
		log.info("\t QueryId  \t:  " + queryId);
	}
	
	/**
	 * 등록
	 * @param queryId
	 * @param params
	 * @throws Exception
	 * @return Object
	 */
	public Object insert(String queryId, Object params) throws Exception{
		printQueryId(queryId);
		return sqlSession.insert(queryId, params);
	}
	
	/**
	 * 수정
	 * @param queryId
	 * @param params
	 * @throws Exception
	 * @return Object
	 */
	public Object update(String queryId, Object params) throws Exception{
		printQueryId(queryId);
		return sqlSession.update(queryId, params);
	}
	
	/**
	 * 삭제
	 * @param queryId
	 * @param params
	 * @throws Exception
	 * @return Object
	 */
	public Object delete(String queryId, Object params) throws Exception{
		printQueryId(queryId);
		return sqlSession.delete(queryId, params);
	}
	
	/**
	 * 단건 조회
	 * @param queryId
	 * @throws Exception
	 * @return Object
	 */
	public Object selectOne(String queryId) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectOne(queryId);
	}
	
	/**
	 * 단건조쇠
	 * @param queryId
	 * @param params
	 * @throws Exception
	 * @return Object
	 */
	public Object selectOne(String queryId, Object params) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectOne(queryId, params);
	}
	
	/**
	 * 목록 조회
	 * @param queryId
	 * @throws Exception
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List selectList(String queryId) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectList(queryId);
	}
	
	/**
	 * 목록 조회
	 * @param queryId
	 * @param params
	 * @throws Exception
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	public List selectList(String queryId, Object params) throws Exception{
		printQueryId(queryId);
		return sqlSession.selectList(queryId,params);
	}
}

