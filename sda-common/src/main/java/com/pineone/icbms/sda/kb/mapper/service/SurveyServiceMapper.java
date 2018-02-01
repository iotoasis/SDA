package com.pineone.icbms.sda.kb.mapper.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.SurveyInfoDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;

/**
 *   투표결과에 대한 Mapper 클래스
 */
public class SurveyServiceMapper implements OneM2MMapper {

	private Model model = ModelFactory.createDefaultModel();
	private List<Statement> slist = new ArrayList<Statement>();
	private Resource contentinstance;
	private String baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	private String content;

	public SurveyServiceMapper(SurveyInfoDTO surveyInfoDTO) {
		// TODO Auto-generated constructor stub
	}

	public SurveyServiceMapper(Resource contentinstance ,String con) {
		this.contentinstance = contentinstance;
		this.content = con;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#from()
	 */
	@Override
	public List<Statement> from() {
		Type type = new TypeToken<List<SurveyInfoDTO>>() {
		}.getType();
		Gson gson = new Gson();
		List<SurveyInfoDTO> surveyList = gson.fromJson(content, type);

		Iterator<SurveyInfoDTO> it = surveyList.iterator();
		
		while (it.hasNext()) {
			Resource _surveury = model.createResource(baseuri+"/Survey_"+UUID.randomUUID());
			List<Statement> _slist = getStatementList( it.next(), _surveury);
			slist.addAll(_slist);
		}
		
		return slist;
	}

	public List<Statement> getStatementList(SurveyInfoDTO surveydto, Resource surveyuri) {
		List<Statement> _list = new ArrayList<Statement>(); 
		
		Statement instance = model.createStatement(contentinstance, model.createProperty("http://www.iotoasis.org/ontology/hasSurveyInfo"),surveyuri);
		_list.add(instance);
		
		Statement surveyStmt = model.createStatement(surveyuri, RDF.type,
				model.createResource("http://www.iotoasis.org/ontology/Survey"));
		_list.add(surveyStmt);

		Statement valueStmt = model.createStatement(surveyuri,
				model.createProperty("http://purl.oclc.org/NET/ssnx/ssn#hasValue"),
				identifyConditionValue(surveydto.getVal()));
		_list.add(valueStmt);

		Statement zoneStmt = model.createStatement(surveyuri,
				model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasLocation"),
				model.createResource(baseuri + "/" + surveydto.getZone()));
		_list.add(zoneStmt);

		Statement userStmt = model.createStatement(surveyuri,
				model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasMember"),
				model.createResource(baseuri + "/" + surveydto.getUser_id()));
		_list.add(userStmt);

		Statement classStmt = model.createStatement(surveyuri,
				model.createProperty("http://www.iotoasis.org/ontology/surveyOn"),
				model.createResource(baseuri + "/" + surveydto.getClass_code()));
		_list.add(classStmt);

		return _list;
	}

	private Resource identifyConditionValue(String condition) {
		return model.createResource(baseuri + "/" + condition + "Condition");
	}
}