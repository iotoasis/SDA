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

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.util.StrUtils;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.UserInOutInfoDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;

public class UserInOutServiceMapper implements OneM2MMapper {
	private List<Statement> slist = new ArrayList<Statement>();
	private Model model = ModelFactory.createDefaultModel();
	private UserInOutInfoDTO dto;
	private Gson gson = new Gson();
	private String strDate;
	private Literal createDate;
	private Resource user;
	private Resource location;
	private String baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	private Resource direction;
	private Resource userinout;

	public UserInOutServiceMapper(String con) {

		this.dto = gson.fromJson(con, UserInOutInfoDTO.class);
	}

	public UserInOutServiceMapper(String dto, String date, Resource userinout2) {
		this.dto = gson.fromJson(dto, UserInOutInfoDTO.class);
		this.strDate = date;
		this.userinout = userinout2;
	}

	public UserInOutServiceMapper(UserInOutInfoDTO dto) {
		this.dto = dto;
	}

	public UserInOutServiceMapper(UserInOutInfoDTO dto, String date) {
		this.dto = dto;
		this.strDate = date;
	}

	public UserInOutServiceMapper(String con, Resource userinout2) {
		this.dto = gson.fromJson(con, UserInOutInfoDTO.class);
		this.userinout = userinout2;
	}

	@Override
	public void initResource() {
		this.user = model.createResource(this.baseuri + "/" + this.dto.getUser_id());
		this.createDate = model.createTypedLiteral(StrUtils.makeXsdDateFromOnem2mDate(this.strDate),
				XSDDateType.XSDdateTime);
		this.location = model.createResource(this.baseuri + "/" + this.dto.getZone());

		this.direction = model.createResource(this.baseuri + "/" + this.dto.getDirection());
	}

	@Override
	public List<Statement> from() {
		initResource();
		// type
		Statement typestmt = model.createStatement(this.userinout, RDF.type,
				model.createResource(baseuri + "/GoInOut"));
		slist.add(typestmt);

		if (this.direction == null || this.user == null || this.location == null
				|| this.direction.toString() == "null" || this.user.toString()=="null" || this.location.toString()=="null") {
			slist.clear();
			return slist;
		}

		// direction
		Statement directionstmt = model.createStatement(this.userinout,
				model.createProperty(baseuri + "/hasDirection"), this.direction);
		slist.add(directionstmt);

		// userid
		Statement userstmt = model.createStatement(this.userinout,
				model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasComponent"), this.user);
		slist.add(userstmt);

		// location
		Statement locationstmt = model.createStatement(this.userinout,
				model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasLocation"), this.location);
		slist.add(locationstmt);

		return slist;
	}

	public String toString() {
		return this.dto.toString();
	}

	public static void main(String[] args) {
		UserInOutServiceMapper mapper = new UserInOutServiceMapper(
				"{\"direction\":\"in\",\"user_id\":\"u00002\"}");
		
		System.out.println(mapper.toString());
	}
}
