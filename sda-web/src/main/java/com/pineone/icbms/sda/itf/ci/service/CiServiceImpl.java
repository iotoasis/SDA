package com.pineone.icbms.sda.itf.ci.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;
import com.pineone.icbms.sda.itf.cm.dto.CmDTO;

@Service("ciService")
public class CiServiceImpl implements CiService{ 
	private Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO; 
	
	/*
	// 목록조회
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectList(Map<String, Object> map) throws Exception {
		return ciDAO.selectList("selectList", map);
	}
	*/
	
	// 헌건조회
	public CiDTO selectOne(String idx) throws Exception{
		return (CiDTO)ciDAO.selectOne("selectOne", idx);
	}
	
	// 저장
	public int insert(Map<String, Object> map) throws Exception{
		return Integer.parseInt(ciDAO.insert("insert", map).toString());
	}

	// 수정(단건)
	public int update(String ciid) throws Exception{
		return Integer.parseInt(ciDAO.update("update", ciid).toString());
	}

	// 수정(여러건)
	public int update(CiDTO[] ciDTO) throws Exception{
		return Integer.parseInt(ciDAO.update("updateMany", ciDTO).toString());
	}
	
	// 삭제(단건)
	public int delete(String ciid) throws Exception{
		return Integer.parseInt(ciDAO.delete("delete", ciid).toString());
	}
	
	// DELETE, use_yn을 'N'으로 변경
	public int delete(Map<String, Object> map) throws Exception {
		return Integer.parseInt(ciDAO.update("itf.ci.delete", map).toString());
	}

	// 삭제(여러건)
	public int delete(CiDTO[] ciDTO) throws Exception{
		return Integer.parseInt(ciDAO.delete("deleteMany", ciDTO).toString());
	}
	
	// /ci/ALL
	public List<CiDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<CiDTO> list = new ArrayList<CiDTO>();
		list = ciDAO.selectList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list ;
	}
	
	// /ci/{ciid}
		public CiDTO selectOne(Map<String, Object> commandMap) throws Exception{
			CiDTO ciDTO = new CiDTO();
			ciDTO = ciDAO.selectOne(commandMap);
			
			// 데이타가 없으면 오류발생시킴
			if (ciDTO == null || ciDTO.getCiid() == null) {
				throw new UserDefinedException(HttpStatus.NOT_FOUND);
			}

			return ciDTO ;
			
		}

}
