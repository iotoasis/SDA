package com.pineone.icbms.sda.kb.mapper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;

import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;

/**
 *   참석여부의 Mapper 클래스
 */
public class AttendanceServiceMapper implements OneM2MMapper {

	private Resource attendance;
	private Model model = ModelFactory.createDefaultModel();
	private String baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	private Resource lecture;
	private Resource condition;
	private Resource user;
	private Literal createDate;

	public AttendanceServiceMapper(String lecture, String createDate, String condition, String user) {
		this.lecture = model.createResource(lecture);
		this.user = model.createResource(user);
		this.condition = model.createResource(baseuri + "/" + condition);
		this.createDate = model.createTypedLiteral(createDate, XSDDateType.XSDdateTime); 
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
		attendance = model.createResource(baseuri + "/Attendance_" + UUID.randomUUID());
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#from()
	 */
	@Override
	public List<Statement> from() {
		List<Statement> slist = new ArrayList<Statement>();
		initResource();
		slist.add(model.createStatement(this.attendance, RDF.type, model.createResource(baseuri + "/Attendance")));
		slist.add(model.createStatement(this.attendance, model.createProperty(baseuri+"/isAttendanceOf"), this.lecture));
		slist.add(model.createStatement(this.attendance, model.createProperty(baseuri+"/hasCreateDate"), this.createDate));
		slist.add(model.createStatement(this.attendance, model.createProperty(baseuri+"/hasCondition"), this.condition));
		slist.add(model.createStatement(this.attendance, model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasMember"), this.user));
		return slist;
	}
}