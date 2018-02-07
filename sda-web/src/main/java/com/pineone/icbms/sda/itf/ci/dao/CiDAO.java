package com.pineone.icbms.sda.itf.ci.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

/**
 * CI용 DAO
 */
@Repository("ciDAO")
public class CiDAO extends AbstractDAO{
	/**
	 * ciid를 이용한 sparql 목록
	 * @param ciids
	 * @throws Exception
	 * @return List<CiDTO>
	 */
	@SuppressWarnings("unchecked") 
	public List<CiDTO> selectSparqlListByCiids(String[] ciids) throws Exception{
		List<CiDTO> ciDTO = new ArrayList<CiDTO>();
		ciDTO = selectList("itf.ci.selectSparqlListByCiids", ciids);
		return ciDTO;
	}
	
	/**
	 * 목록조회
	 * @param commandMap
	 * @throws Exception
	 * @return List<CiDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<CiDTO> selectList(Map<String, Object> commandMap) throws Exception {
		return (List<CiDTO>)selectList("itf.ci.selectList", commandMap);
	}
	
	/**
	 * 단건조회
	 * @param commandMap
	 * @throws Exception
	 * @return CiDTO
	 */
	public CiDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (CiDTO)selectOne("itf.ci.selectOne", commandMap);
	}
	
	/**
	 * 삭제
	 * @param commandMap
	 * @throws Exception
	 * @return int
	 */
	public int delete(Map<String, Object> commandMap) throws Exception {
		return Integer.parseInt( (String) update("itf.ci.delete", commandMap));
	}
	
	/**
	 * ID확인
	 * @param ciid
	 * @throws Exception
	 * @return int
	 */
	public int checkId(String ciid) throws Exception {
		return Integer.parseInt(selectOne("itf.ci.checkId", ciid).toString());
	}
	
	
}
