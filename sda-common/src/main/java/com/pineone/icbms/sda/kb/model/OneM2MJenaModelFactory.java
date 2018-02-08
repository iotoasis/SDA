package com.pineone.icbms.sda.kb.model;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Statement;

import com.pineone.icbms.sda.kb.dto.OneM2MAEDTO;
import com.pineone.icbms.sda.kb.dto.OneM2MCSEBaseDTO;
import com.pineone.icbms.sda.kb.dto.OneM2MContainerDTO;
import com.pineone.icbms.sda.kb.dto.OneM2MContentInstanceDTO;
import com.pineone.icbms.sda.kb.dto.OneM2MRemoteCSEDTO;

/**
 *   OneM2M의 JenaModel Factory
 */
public class OneM2MJenaModelFactory {
	TripleMap<Statement> triples ;
	
	public OneM2MJenaModelFactory(OneM2MContainerDTO dto){
		triples = dto.getTriples();
	}
	
	public OneM2MJenaModelFactory(OneM2MContentInstanceDTO dto){
		triples = dto.getTriples();
	}
	
	public OneM2MJenaModelFactory(OneM2MAEDTO dto){
		triples = dto.getTriples();
	}
	
	public OneM2MJenaModelFactory(OneM2MCSEBaseDTO dto){
		triples = dto.getTriples();
	}
	
	public OneM2MJenaModelFactory(OneM2MRemoteCSEDTO dto){
		triples = dto.getTriples();
	}
	
	public String makeTriple(){
		return "http://somewhere/JohnSmith http://www.w3.org/2001/vcard-rdf/3.0#FN  \"John Smith\" ";
	}

	/**
	 *   트리플 생성
	 * @return Triple
	 */
	public static Triple createDefaultTriple() {
		return null;
	}

}
