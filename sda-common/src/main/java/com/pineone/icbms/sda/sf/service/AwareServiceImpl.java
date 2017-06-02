package com.pineone.icbms.sda.sf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.pineone.icbms.sda.comm.SqlMapConfig;

import com.pineone.icbms.sda.sf.dao.AwareHistDAO;
import com.pineone.icbms.sda.sf.dto.AwareHistDTO;

public class AwareServiceImpl implements AwareService{ 
	private final Log log = LogFactory.getLog(this.getClass());
	
	// 마이바티스 셋팅값 불러오기
	private SqlSessionFactory factory = SqlMapConfig.getSqlSession();
	
	
	public AwareServiceImpl() {
		factory = SqlMapConfig.getSqlSession();
	}

/*	
	// 목록조회
	public List<AwareHistDTO> selectList() throws Exception {
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		AwareHistDAO AwareHistDAO = new AwareHistDAO(sqlSession);
		return AwareHistDAO.selectList();
	}

	public List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public AwareHistDTO selectOne(Map<String, Object> commandMap) throws Exception {
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		AwareHistDAO AwareHistDAO = new AwareHistDAO(sqlSession);
		return (AwareHistDTO)AwareHistDAO.selectOne(commandMap);
	}
	
	// last_work_time컬럼에 값을 업데이트한다.
	public int updateLastWorkTime(Map<String, Object> map) throws Exception {
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		int cnt = -1; 
		AwareHistDAO awareHistDAO = new AwareHistDAO(sqlSession);
		
		@SuppressWarnings("unchecked")
		List<AwareHistDTO> list = (ArrayList<AwareHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			AwareHistDTO awareHistDTO = (AwareHistDTO)list.get(i);
			try { 
				cnt = awareHistDAO.updateLastWorkTime(awareHistDTO);
				if(cnt >= 0) {
					sqlSession.commit();
				} else {
					sqlSession.rollback();
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("Exception =====> "+e.getMessage());
				throw e;
			} finally {
				sqlSession.close();
			}
		}
		return cnt;
	}
	*/

	// sch_hist
	public int insertAwareHist(Map<String, List<AwareHistDTO>> map) throws Exception {
		int cnt = -1;
		
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		AwareHistDAO awareHistDAO = new AwareHistDAO(sqlSession);
		
		List<AwareHistDTO> list = map.get("list");
		for(int i = 0; i < list.size(); i++) {
			AwareHistDTO AwareHistDTO = (AwareHistDTO)list.get(i);
			try {
				log.debug("insertAwareHist() ......................... start ");
				
				cnt = awareHistDAO.insert(AwareHistDTO);

				if(cnt > 0) {
					sqlSession.commit();
				} else {
					sqlSession.rollback();
				}
				//log.debug("insertAwareHist()......................... commit ");				
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("Exception in insertAwareHist()=====> "+e.getMessage());				
				throw e;
			} finally {
				sqlSession.close();				
				//log.debug("insertAwareHist() ......................... close ");				
			}
		}
		log.debug("insertAwareHist() ......................... end ");
		return cnt;
	}

	
	public int updateFinishTime(Map<String, Object> map) throws Exception {
		int cnt = -1; 
		
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		AwareHistDAO AwareHistDAO = new AwareHistDAO(sqlSession);
		@SuppressWarnings("unchecked")
		List<AwareHistDTO> list = (ArrayList<AwareHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			AwareHistDTO AwareHistDTO = (AwareHistDTO)list.get(i);
			try {
				log.debug("updateFinishTime() ......................... start ");
				cnt = AwareHistDAO.updateFinishTime(AwareHistDTO);
				if(cnt >= 0) {
					sqlSession.commit();
					//log.debug("updateFinishTime() ......................... commit ");					
				} else {
					sqlSession.rollback();
					//log.debug("updateFinishTime() ......................... rollback ");					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("Exception in updateFinishTime()=====> "+e.getMessage());
				throw e;
			} finally {
				//log.debug("updateFinishTime() ......................... close ");				
				sqlSession.close();
			}
		}
		log.debug("updateFinishTime() ......................... end ");
		return cnt;
	}

}
