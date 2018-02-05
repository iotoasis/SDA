package com.pineone.icbms.sda.sch.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.pineone.icbms.sda.comm.sch.dto.SchHistDTO;
import com.pineone.icbms.sda.sch.comm.dao.AbstractDAO;

/**
 * 스케줄 history용 DAO
 */
@Repository("schHistDAO")
public class SchHistDAO extends AbstractDAO{
	/**
	 * 목록 조회
	 * @throws Exception
	 * @return List<SchHistDTO>
	 */
	@SuppressWarnings("unchecked")
	public List<SchHistDTO> selectList() throws Exception{
		return selectList("sch.hist.selectList");
	}
	
	/**
	 * 단건 조회
	 * @param commandMap
	 * @throws Exception
	 * @return SchHistDTO
	 */
	public SchHistDTO selectOne(Map<String, Object> commandMap) throws Exception{
		return (SchHistDTO)selectOne("sch.hist.selectOne", commandMap);
	}

	/**
	 * 종료시간 수정
	 * @param schHistDTO
	 * @throws Exception
	 * @return int
	 */
	public int updateFinishTime(SchHistDTO schHistDTO) throws Exception{
		return Integer.parseInt(update("sch.hist.updateFinishTime", schHistDTO).toString());
	}
	
	/**
	 * 등록
	 * @param schHistDTO
	 * @throws Exception
	 * @return int
	 */
	public int insert(SchHistDTO schHistDTO) throws Exception{
		return Integer.parseInt(insert("sch.hist.insert", schHistDTO).toString());
	}

}
