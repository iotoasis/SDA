package com.pineone.icbms.sda.kb.mapper.onem2m;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.google.gson.Gson;
import com.pineone.icbms.sda.comm.conf.Configuration;
import com.pineone.icbms.sda.comm.util.StrUtils;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MContentInstanceDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;
import com.pineone.icbms.sda.kb.mapper.service.PresenceServiceMapper;
import com.pineone.icbms.sda.kb.mapper.service.SurveyServiceMapper;
import com.pineone.icbms.sda.kb.mapper.service.UserInOutServiceMapper;
import com.pineone.icbms.sda.kb.model.ICBMSResource;

public class OneM2MContentInstanceMapper implements OneM2MMapper {
	// private Configuration config = null;
	private List<Statement> slist = new ArrayList<Statement>();
	private Model model = ModelFactory.createDefaultModel();
	private String baseuri = "";
	private OneM2MContentInstanceDTO dto;
	private Resource contentInstance;
	private String resourceName;
	private Resource parentResource;
	private String label;
	private String contentInfo;
	private String content;
	private String contentType;
	int isEncoded = 0;

	public enum EnumContentType {
		userinout, survey, normal, presence, present
	}

	public OneM2MContentInstanceMapper(OneM2MContentInstanceDTO dto) {
		this.dto = dto;
		this.baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
		this.setContetnType();
	}

	public OneM2MContentInstanceMapper(OneM2MContentInstanceDTO dto, Configuration config) {
		this.dto = dto;
		this.baseuri = config.getStringProperty("com.pineone.icbms.sda.knowledgebase.uri");
		this.setContetnType();
	}

	@Override
	public void initResource() {
		// contentInstance = model.createResource(baseuri + "/" + dto.getRi());
		contentInstance = model.createResource(baseuri + this.dto.get_uri());
		parentResource = model.createResource(baseuri + Utils.getParentURI(dto.get_uri()));
		contentInfo = dto.getCnf();
		resourceName = dto.getRn();
		contentInfo = dto.getCnf();
		label = "";
		for (int i = 0; i < dto.getLbl().length; i++) {
			label = label + "," + dto.getLbl()[i];
		}
		Resource Content = recognizeContentResource();

		// getDecodedContent
		if (contentInfo.contains("text/plain:0")) {
			isEncoded = 0;
			content = dto.getCon(0);
		} else if (contentInfo.contains("application/json:1")) {
			isEncoded = 1;
		}
		content = dto.getCon(isEncoded);
	}

	private Resource recognizeContentResource() {
		String contentString = dto.getCon().trim().toLowerCase();
		Resource resource = null;

		if (contentString.equals(ICBMSResource.open)) {
			resource = model.createResource(ICBMSResource.openUri);
		} else if (contentString.equals(ICBMSResource.close)) {
			resource = model.createResource(ICBMSResource.closeUri);
		} else if (contentString.equals(ICBMSResource.success)) {
			resource = model.createResource(ICBMSResource.successUri);
		} else if (contentString.equals(ICBMSResource.failure)) {
			resource = model.createResource(ICBMSResource.failureUri);
		} else if (contentString.equals(ICBMSResource.contacted)) {
			resource = model.createResource(ICBMSResource.contactedUri);
		} else if (contentString.equals(ICBMSResource.notContacted)) {
			resource = model.createResource(ICBMSResource.notContactedUri);
		} else if (contentString.equals(ICBMSResource.on)) {
			resource = model.createResource(ICBMSResource.onUri);
		} else if (contentString.equals(ICBMSResource.off)) {
			resource = model.createResource(ICBMSResource.offUri);
		} else if (contentString.equals(ICBMSResource.detected)) {
			resource = model.createResource(ICBMSResource.detectedUri);
		} else if (contentString.equals(ICBMSResource.notDetected)) {
			resource = model.createResource(ICBMSResource.notDetectedUri);
		} else if (contentString.equals(ICBMSResource.wet)) {
			resource = model.createResource(ICBMSResource.wetUri);
		} else if (contentString.equals(ICBMSResource.dry)) {
			resource = model.createResource(ICBMSResource.dryUri);
		} else if (contentString.equals(ICBMSResource.reserved)) {
			resource = model.createResource(ICBMSResource.reservedUri);
		} else if (contentString.equals(ICBMSResource.siren)) {
			resource = model.createResource(ICBMSResource.sirenUri);
		} else if (contentString.equals(ICBMSResource.silent)) {
			resource = model.createResource(ICBMSResource.silentUri);
		} else {
			return null;
		}
		return resource;
	}

	@Override
	public List<Statement> from() {

		initResource();

		// type1
		Statement stmtType = model.createStatement(contentInstance, RDF.type,
				model.createResource("http://www.pineone.com/m2m/ContentInstance"));
		slist.add(stmtType);

		// type2

		if (dto.getOr() != null) {
			Statement stmtOntologyReferenceType = model.createStatement(contentInstance, RDF.type,
					model.createResource(dto.getOr()));
			slist.add(stmtOntologyReferenceType);
		}

		// name
		Statement stmtName = model.createStatement(contentInstance,
				model.createProperty("http://www.pineone.com/m2m/resourceName"), resourceName);
		slist.add(stmtName);

		// parent resource
		Statement stmtParentResource = model.createStatement(contentInstance,
				model.createProperty(ICBMSResource.baseuri + "/m2m/hasParentResource"), parentResource);
		slist.add(stmtParentResource);

		// subclass of
		Statement stmtIsResourceOf = model.createStatement(contentInstance,
				model.createProperty(ICBMSResource.baseuri + "/m2m/isContentInstanceOf"), parentResource);
		slist.add(stmtIsResourceOf);

		// label
		Statement stmtLabel = model.createStatement(contentInstance, RDFS.label, label);
		slist.add(stmtLabel);

		// creator
		Statement stmtCreator = model.createStatement(contentInstance, DC.creator, this.dto.getCr());
		slist.add(stmtCreator);

		// content
		Statement stmtContent = null;
		if (recognizeContentResource() != null) {
			stmtContent = model.createStatement(contentInstance,
					model.createProperty(ICBMSResource.baseuri + "hasStatusCondition"), recognizeContentResource());
		} else {
			stmtContent = model.createStatement(contentInstance,
					model.createProperty("http://www.pineone.com/m2m/hasContentValue"),
					this.getTypedContent(dto.getCon()));
		}
		slist.add(stmtContent);

		// call UserInoutServiceMapper when _uri contain "UserInOut"
		// officially service depended mapper

		System.out.println("contentType : " + this.getContentType());
		switch (this.getContentType()) {

		case "userinout":
			Resource userinout = model.createResource(baseuri + "/campus/GoInOut_" + UUID.randomUUID());
			byte[] encodedContent = this.dto.getCon().getBytes();
			slist.add(model.createStatement(contentInstance,
					model.createProperty("http://www.pineone.com/campus/hasActivityInfo"), userinout));
			try {

				slist.addAll(
						new UserInOutServiceMapper(new String(Base64.getDecoder().decode(encodedContent)), userinout)
								.from());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case "survey":
			String con1 = this.dto.getCon();
			SurveyServiceMapper mapper1 = new SurveyServiceMapper(contentInstance, con1);
			List<Statement> slist2 = mapper1.from();
			slist.addAll(slist2);
			break;

		case "present":
			String con2 = this.dto.getCon();
			if (con2.contains("not presence") || con2.contains("not present")) {
				con2 = con2.replaceAll(" ", "-");
				Statement presenceStatement = model.createStatement(contentInstance,
						model.createProperty(baseuri + "/campus/hasPresenceContent"),
						model.createResource(baseuri + "/campus/" + con2));
				slist.add(presenceStatement);
			} else if (con2.contains("present") || con2.contains("presence")) {
				Statement presenceStatement = model.createStatement(contentInstance,
						model.createProperty(baseuri + "/campus/hasPresenceContent"),
						model.createResource(baseuri + "/campus/" + con2));
				slist.add(presenceStatement);
			}
			break;
		}
		// case "normal":
		// Statement normalStatement = model.createStatement(contentInstance,
		// model.createProperty(baseuri + "/m2m/hasContentValue"),
		// model.createTypedLiteral(dto.getCon()));
		// slist.add(normalStatement);\

		// time
		Statement stmtCreateTime = model.createStatement(contentInstance,
				model.createProperty(baseuri + "/m2m/hasCreateDate"), model.createTypedLiteral(
						StrUtils.makeXsdDateFromOnem2mDate(this.dto.getCt()), XSDDateType.XSDdateTime));
		slist.add(stmtCreateTime);

		Statement stmtLastModifiedTime = model.createStatement(contentInstance,
				model.createProperty(baseuri + "/m2m/hasLastModifiedDate"),
				model.createTypedLiteral(StrUtils.makeXsdDateFromOnem2mDate(dto.getLt()), XSDDateType.XSDdateTime));
		slist.add(stmtLastModifiedTime);

		return slist;
	}

	public void setContetnType() {
		if (this.dto.get_uri().toLowerCase().contains(EnumContentType.survey.toString())) {
			this.setContentType(EnumContentType.survey);
		} else if (this.dto.get_uri().toLowerCase().contains(EnumContentType.userinout.toString())) {
			this.setContentType(EnumContentType.userinout);
		} else if (this.dto.get_uri().toLowerCase().contains(EnumContentType.presence.toString())) {
			this.setContentType(EnumContentType.present);
		} else {
			this.setContentType(EnumContentType.normal);
		}

	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(Enum contentType) {
		this.contentType = contentType.toString();
	}

	public Literal getTypedContent(String con) {
		try {

			return ResourceFactory.createTypedLiteral(Double.valueOf(con));
			// return "\"" + Double.valueOf(con) + "\"^^xsd:double";
		} catch (java.lang.NumberFormatException e) {
			try {
				return ResourceFactory.createTypedLiteral(Float.valueOf(con));
				// return "\"" + Float.valueOf(con) + "\"^^xsd:float";
			} catch (Exception e2) {
				return ResourceFactory.createTypedLiteral(String.valueOf(con));
				// return "\"" + con + "\"^^xsd:string";
			}
		}

	}

	public static void main(String[] args) throws IOException {

		File f = new File("/Users/rosenc/Documents/business/[2015]icbms/json_sample1.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		String s = "";
		while ((line = br.readLine()) != null) {
			s = s + line + "\n";
		}

		System.out.println(s);
		Gson gson = new Gson();
		OneM2MContentInstanceDTO cont = gson.fromJson(s, OneM2MContentInstanceDTO.class);
		OneM2MContentInstanceMapper mapper = new OneM2MContentInstanceMapper(cont);

		Model model = ModelFactory.createDefaultModel();
		model.add(mapper.from());
		System.out.println("content type ; " + mapper.getContentType());
		// 스트링 변환부분
		RDFDataMgr.write(System.out, model, RDFFormat.NTRIPLES);
		// System.out.println(mapper.getTypedContent("2k42kk"));
		// mapper.getTypedContent("2.4");

	}

}
