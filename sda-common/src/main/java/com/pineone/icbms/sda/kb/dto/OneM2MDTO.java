package com.pineone.icbms.sda.kb.dto;

import org.apache.jena.rdf.model.Statement;
import com.pineone.icbms.sda.kb.model.TripleMap;

/**
 *   OneM2M DTO의 인터페이스
 */
public interface OneM2MDTO {
	public String toString();
	public void print();
	public TripleMap<Statement> getTriples();
	public String getStringId();
	
}
