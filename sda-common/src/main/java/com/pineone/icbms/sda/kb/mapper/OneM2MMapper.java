package com.pineone.icbms.sda.kb.mapper;

import java.util.List;

import org.apache.jena.rdf.model.Statement;

/**
 * OneM2M에 대한 Mapper 클래스
 */
public interface OneM2MMapper {
	/**
	 * 초기화 
	 * @return void
	 */
	public void initResource();
	
	/**
	 * 문장 목록
	 * @return List<Statement>
	 */
	public List<Statement> from();
}
