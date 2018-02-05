package com.pineone.icbms.sda.subscribe.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;
import com.pineone.icbms.sda.subscribe.dto.CallbackDTO;
import com.pineone.icbms.sda.subscribe.dto.SubscribeDTO;

/**
 * subscribe용 DAO
 */
@Repository("subscribeDAO")
public class SubscribeDAO extends AbstractDAO{
	
	/**
	 * 목록조회
	 * @throws Exception
	 * @return List<CiDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<CiDTO> selectList() throws Exception{
		return selectList("subscribe.selectList");
	}

	/**
	 * 목록조회
	 * @param commandMap
	 * @throws Exception
	 * @return List<CiDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<CiDTO> selectList(Map<String, Object> commandMap) throws Exception{
		return selectList("subscribe.selectList", commandMap);
	}

	/**
	 * uri로 목록 조회
	 * @param commandMap
	 * @throws Exception
	 * @return List<SubscribeDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<SubscribeDTO> selectListByUri(Map<String, Object> commandMap) throws Exception{
		return selectList("subscribe.selectListByUri", commandMap);
	}

	/**
	 * 등록
	 * @param subscribeDTO
	 * @throws Exception
	 * @return int
	 */
	public int insert(SubscribeDTO subscribeDTO) throws Exception{
		return Integer.parseInt(insert("subscribe.insert", subscribeDTO).toString());
	}	
	
	/**
	 * 최종수정일 update
	 * @param subscribeDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateLastWorkTime(SubscribeDTO subscribeDTO) throws Exception{
		return Integer.parseInt(update("subscribe.updateLastWorkTime", subscribeDTO).toString());
	}
	
	/**
	 * 종료시간 update
	 * @param callbackDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateFinishTime(CallbackDTO callbackDTO) throws Exception{
		return Integer.parseInt(update("callback.updateFinishTime", callbackDTO).toString());
	}
	
	/**
	 * id로 삭제
	 * @param commandMap
	 * @throws Exception
	 * @return int
	 */
	public int deleteByCmid(Map<String, Object> commandMap) throws Exception{
		return Integer.parseInt(delete("subscribe.deleteByCmid", commandMap).toString());
	}	

	/**
	 * uri로 삭제
	 * @param commandMap
	 * @throws Exception
	 * @return int
	 */
	public int deleteByUri(Map<String, Object> commandMap) throws Exception{
		return Integer.parseInt(delete("subscribe.deleteByUri", commandMap).toString());
	}	


}
