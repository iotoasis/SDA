package com.pineone.icbms.sda.kb.mapper.service;

import java.util.ArrayList;
import java.util.Iterator;
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

public class AttendanceServiceMapper implements OneM2MMapper {

	private Resource attendance;
	private Model model = ModelFactory.createDefaultModel();
	private String baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	private Resource lecture;
	private Resource condition;
	private Resource user;
	private Literal createDate;


	/**
	 * 
	 * @param lecture
	 * @param createDate : "yyyy-mm-ddThh:mi:ss"^^xsd:dateTime
	 * @param condition : absent/late/attend
	 * @param user
	 */
	public AttendanceServiceMapper(String lecture, String createDate, String condition, String user) {
		this.lecture = model.createResource(lecture);
		this.user = model.createResource(user);
		this.condition = model.createResource(baseuri + "/campus/" + condition);
		this.createDate = model.createTypedLiteral(createDate, XSDDateType.XSDdateTime); 
	}

	@Override
	public void initResource() {
		attendance = model.createResource(baseuri + "/campus/Attendance_" + UUID.randomUUID());
	}

	@Override
	public List<Statement> from() {
		List<Statement> slist = new ArrayList<Statement>();
		initResource();
		slist.add(model.createStatement(this.attendance, RDF.type, model.createResource(baseuri + "/campus/Attendance")));
		slist.add(model.createStatement(this.attendance, model.createProperty(baseuri+"/campus/isAttendanceOf"), this.lecture));
		slist.add(model.createStatement(this.attendance, model.createProperty(baseuri+"/m2m/hasCreateDate"), this.createDate));
		slist.add(model.createStatement(this.attendance, model.createProperty(baseuri+"/campus/hasCondition"), this.condition));
		slist.add(model.createStatement(this.attendance, model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasMember"), this.user));
		return slist;
	}

	public static void main(String[] args) {
		AttendanceServiceMapper map = new AttendanceServiceMapper("http://www.pineone.com/campus/cm001","2015-12-12T00:00:00","absent","http://www.pineone.com/campus/u00001");
		List<Statement> result = map.from();
		Iterator it = result.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
			
		}
	}
}
