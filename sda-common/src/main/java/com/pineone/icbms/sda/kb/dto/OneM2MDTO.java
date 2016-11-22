package com.pineone.icbms.sda.kb.dto;

import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.model.TripleMap;

public interface OneM2MDTO {
	public String toString();
	public void print();
	public TripleMap<Statement> getTriples();
	public String getStringId();
	
}
