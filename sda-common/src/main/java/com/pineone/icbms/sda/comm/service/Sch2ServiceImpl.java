package com.pineone.icbms.sda.comm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.pineone.icbms.sda.comm.SqlMapConfig;
import com.pineone.icbms.sda.comm.sch.dao.Sch2DAO;
import com.pineone.icbms.sda.comm.sch.dao.SchHist2DAO;
import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;

public class Sch2ServiceImpl implements Sch2Service{ 
	private final Log log = LogFactory.getLog(this.getClass());
	
	// 마이바티스 셋팅값 불러오기
	private SqlSessionFactory factory = SqlMapConfig.getSqlSession();
	
	
	public Sch2ServiceImpl() {
		factory = SqlMapConfig.getSqlSession();
	}

	// 목록조회
	public List<SchDTO> selectList() throws Exception {
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		Sch2DAO sch2DAO = new Sch2DAO(sqlSession);
		return sch2DAO.selectList();
	}

	public List<Map<String, Object>> select(Map<String, Object> commandMap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		Sch2DAO sch2DAO = new Sch2DAO(sqlSession);
		return (SchDTO)sch2DAO.selectOne(commandMap);
	}
	
	// last_work_time컬럼에 값을 업데이트한다.
	public int updateLastWorkTime(Map<String, Object> map) throws Exception {
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		int cnt = -1; 
		Sch2DAO sch2DAO = new Sch2DAO(sqlSession);
		
		@SuppressWarnings("unchecked")
		List<SchDTO> list = (ArrayList<SchDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchDTO schDTO = (SchDTO)list.get(i);
			try { 
				cnt = sch2DAO.updateLastWorkTime(schDTO);
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
	
	// sch_hist
	/*
	public int insertSchHist(Map<String, Object> map) throws Exception {
		int cnt = 0; 
		List<SchHistDTO> list = (ArrayList<SchHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			cnt = schHist2DAO.insert(schHistDTO);
		}
		return cnt;
	}
	*/

	// sch_hist
	public int insertSchHist(Map<String, List<SchHistDTO>> map) throws Exception {
		int cnt = -1;
		
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		SchHist2DAO schHist2DAO = new SchHist2DAO(sqlSession);
		
		List<SchHistDTO> list = map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			try {
				log.debug("insertSchHist() ......................... start ");
				
				cnt = schHist2DAO.insert(schHistDTO);

				if(cnt > 0) {
					sqlSession.commit();
				} else {
					sqlSession.rollback();
				}
				//log.debug("insertSchHist()......................... commit ");				
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("Exception in insertSchHist()=====> "+e.getMessage());				
				throw e;
			} finally {
				sqlSession.close();				
				//log.debug("insertSchHist() ......................... close ");				
			}
		}
		log.debug("insertSchHist() ......................... end ");
		return cnt;
	}

	
	public int updateFinishTime(Map<String, Object> map) throws Exception {
		int cnt = -1; 
		
		// mapper에 접근하기 위한 SqlSession
		SqlSession sqlSession = factory.openSession(); 

		Sch2DAO sch2DAO = new Sch2DAO(sqlSession);
		@SuppressWarnings("unchecked")
		List<SchHistDTO> list = (ArrayList<SchHistDTO>)map.get("list");
		for(int i = 0; i < list.size(); i++) {
			SchHistDTO schHistDTO = (SchHistDTO)list.get(i);
			try {
				log.debug("updateFinishTime() ......................... start ");
				cnt = sch2DAO.updateFinishTime(schHistDTO);
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
