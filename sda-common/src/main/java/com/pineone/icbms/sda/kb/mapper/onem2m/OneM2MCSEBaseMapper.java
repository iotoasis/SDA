package com.pineone.icbms.sda.kb.mapper.onem2m;

import java.util.List;

import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.dto.OneM2MCSEBaseDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;

/**
 *   CSEBase의 Mapper 클래스
 */
public class OneM2MCSEBaseMapper implements OneM2MMapper{

	public OneM2MCSEBaseMapper(OneM2MCSEBaseDTO oneM2MCSEBaseDTO) {
		// TODO Auto-generated constructor stub
	}


	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#from()
	 */
	@Override
	public List<Statement> from() {
		return null;
	}

}
