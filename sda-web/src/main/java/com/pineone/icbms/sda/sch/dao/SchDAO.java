package com.pineone.icbms.sda.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

/**
 * 스케줄러용 DAO
 */
@Repository("schDAO")
public class SchDAO extends AbstractDAO {

	/**
	 * 목록조회
	 * @throws Exception
	 * @return List<SchDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<SchDTO> selectList() throws Exception {
		return (List<SchDTO>) selectList("sch.selectList");
	}

	/**
	 * 단건조회
	 * @param commandMap
	 * @throws Exception
	 * @return SchDTO
	 */
	public SchDTO selectOne(Map<String, Object> commandMap) throws Exception {
		return (SchDTO) selectOne("sch.selectOne", commandMap);
	}

	/**
	 * 최종 작업시간 수정
	 * @param schDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateLastWorkTime(SchDTO schDTO) throws Exception {
		return Integer.parseInt(update("sch.updateLastWorkTime", schDTO).toString());
	}

	/**
	 * 작업종료시간 수정
	 * @param schHistDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception {
		return Integer.parseInt(update("sch.hist.updateFinishTime", schHistDTO).toString());
	}

}
