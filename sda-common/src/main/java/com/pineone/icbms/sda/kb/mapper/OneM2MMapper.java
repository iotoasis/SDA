package com.pineone.icbms.sda.kb.mapper;

import java.util.List;

import org.apache.jena.rdf.model.Statement;

public interface OneM2MMapper {
	public void initResource();
	public List<Statement> from();
}
