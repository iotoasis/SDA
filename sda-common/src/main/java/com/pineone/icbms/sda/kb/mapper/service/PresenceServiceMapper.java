package com.pineone.icbms.sda.kb.mapper.service;

import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;

/**
 *   참석여부의 Mapper 클래스
 */
public class PresenceServiceMapper implements OneM2MMapper{
	public PresenceServiceMapper(Resource contentInstance, String con2) {
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#from()
	 */
	public List<Statement> from() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
		// TODO Auto-generated method stub
	}
}