package com.pineone.icbms.sda.itf.ci.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.itf.ci.dao.CiDAO;
import com.pineone.icbms.sda.itf.ci.dto.CiDTO;

/**
 * CiService 구현체
 */
@Service("ciService")
public class CiServiceImpl implements CiService{ 
	@Resource(name="ciDAO")
	private CiDAO ciDAO; 
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#selectOne(java.lang.String)
	 */
	public String selectOne(String ciid) throws Exception{
		return ciDAO.selectOne("itf.ci.selectCiid", ciid).toString();
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#insert(java.util.Map)
	 */
	public int insert(Map<String, Object> map) throws Exception{
		return Integer.parseInt(ciDAO.insert("itf.ci.insert", map).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#update(java.lang.String)
	 */
	public int update(String ciid) throws Exception{
		return Integer.parseInt(ciDAO.update("update", ciid).toString());
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#update(com.pineone.icbms.sda.itf.ci.dto.CiDTO[])
	 */
	public int update(CiDTO[] ciDTO) throws Exception{
		return Integer.parseInt(ciDAO.update("updateMany", ciDTO).toString());
	}
	
	/**
	 * 삭제(단건)
	 * @param ciid
	 * @return int
	 * @throws Exception
	 */
	public int delete(String ciid) throws Exception{
		return Integer.parseInt(ciDAO.delete("delete", ciid).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#delete(java.util.Map)
	 */
	public int delete(Map<String, Object> map) throws Exception {
		return Integer.parseInt(ciDAO.update("itf.ci.delete", map).toString());
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#delete(com.pineone.icbms.sda.itf.ci.dto.CiDTO[])
	 */
	public int delete(CiDTO[] ciDTO) throws Exception{
		return Integer.parseInt(ciDAO.delete("deleteMany", ciDTO).toString());
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#selectList(java.util.Map)
	 */
	public List<CiDTO> selectList(Map<String, Object> commandMap) throws Exception {
		List<CiDTO> list = new ArrayList<CiDTO>();
		list = ciDAO.selectList(commandMap);

		if (list == null || list.size() == 0) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}

		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#selectOne(java.util.Map)
	 */
	public CiDTO selectOne(Map<String, Object> commandMap) throws Exception{
		CiDTO ciDTO = new CiDTO();
		ciDTO = ciDAO.selectOne(commandMap);
			
		// 데이타가 없으면 오류발생시킴
		if (ciDTO == null || ciDTO.getCiid() == null) {
			throw new UserDefinedException(HttpStatus.NOT_FOUND);
		}
		return ciDTO ;
			
	}
		
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#checkId(java.lang.String)
	 */
	public int checkId(String ciid) throws Exception {
		return ciDAO.checkId(ciid);
	}
		
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.itf.ci.service.CiService#update(java.util.Map)
	 */
	public int update(Map<String, Object> commandMap) throws Exception {
		return Integer.parseInt(ciDAO.update("itf.ci.update", commandMap).toString());	
	}

}