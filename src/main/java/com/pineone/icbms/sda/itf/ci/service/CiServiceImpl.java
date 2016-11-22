package com.pineone.icbms.sda.itf.ci.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

@Service("ciService")
public class CiServiceImpl implements CiService{ 
	private Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="ciDAO")
	private CiDAO ciDAO; 
	
	// 목록조회
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> map) throws Exception {
		return ciDAO.selectList("selectList", map);
	}
	
	// 헌건조회
	@Override
	public CiDTO selectOne(String idx) throws Exception{
		return (CiDTO)ciDAO.selectOne("selectOne", idx);
	}
	
	// 저장
	@Override
	public int insert(Map<String, Object> map) throws Exception{
		return (int)ciDAO.insert("insert", map);
	}

	// 수정(단건)
	@Override
	public int update(String idx) throws Exception{
		return (int)ciDAO.update("update", idx);
	}

	// 수정(여러건)
	@Override
	public int update(CiDTO[] ciDTO) throws Exception{
		return (int)ciDAO.update("updateMany", ciDTO);
	}
	
	// 삭제(단건)
	@Override
	public int delete(String idx) throws Exception{
		return (int)ciDAO.delete("delete", idx);
	}

	// 삭제(여러건)
	@Override
	public int delete(CiDTO[] ciDTO) throws Exception{
		return (int)ciDAO.delete("deleteMany", ciDTO);
	}

}
