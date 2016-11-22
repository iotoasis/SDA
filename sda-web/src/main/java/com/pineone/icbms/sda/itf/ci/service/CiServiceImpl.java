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
	public List<Map<String, Object>> selectList(Map<String, Object> map) throws Exception {
		return ciDAO.selectList("selectList", map);
	}
	
	// 헌건조회
	public CiDTO selectOne(String idx) throws Exception{
		return (CiDTO)ciDAO.selectOne("selectOne", idx);
	}
	
	// 저장
	public int insert(Map<String, Object> map) throws Exception{
		return Integer.parseInt(ciDAO.insert("insert", map).toString());
	}

	// 수정(단건)
	public int update(String idx) throws Exception{
		return Integer.parseInt(ciDAO.update("update", idx).toString());
	}

	// 수정(여러건)
	public int update(CiDTO[] ciDTO) throws Exception{
		return Integer.parseInt(ciDAO.update("updateMany", ciDTO).toString());
	}
	
	// 삭제(단건)
	public int delete(String idx) throws Exception{
		return Integer.parseInt(ciDAO.delete("delete", idx).toString());
	}

	// 삭제(여러건)
	public int delete(CiDTO[] ciDTO) throws Exception{
		return Integer.parseInt(ciDAO.delete("deleteMany", ciDTO).toString());
	}

}
