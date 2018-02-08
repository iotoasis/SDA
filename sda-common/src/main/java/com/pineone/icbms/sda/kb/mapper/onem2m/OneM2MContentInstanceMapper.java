package com.pineone.icbms.sda.kb.mapper.onem2m;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pineone.icbms.sda.comm.conf.Configuration;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.DomappTempDTO;
import com.pineone.icbms.sda.kb.dto.OneM2MContentInstanceDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;
import com.pineone.icbms.sda.kb.mapper.service.SurveyServiceMapper;
import com.pineone.icbms.sda.kb.mapper.service.UserInOutServiceMapper;
import com.pineone.icbms.sda.kb.model.ICBMSResource;
import com.pineone.icbms.sda.sf.QueryService;
import com.pineone.icbms.sda.sf.QueryServiceFactory;
import com.pineone.icbms.sda.sf.SparqlFusekiQueryImpl;

/**
 *   ContentInstance용 Mapper클래스
 */
public class OneM2MContentInstanceMapper implements OneM2MMapper {
	private final Log log = LogFactory.getLog(this.getClass());
	private List<Statement> slist = new ArrayList<Statement>();
	private Model model = ModelFactory.createDefaultModel();;
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
	
	private String createDate;
	private String lastModifiedDate;
	
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * 생성일 설정
	 * @param createDate
	 * @return void
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * 수정일
	 * @return String
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * 수정일 설정
	 * @param lastModifiedDate
	 * @return void
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Content Type
	 */
	public enum EnumContentType {
		userinout, survey, normal, presence, present, dormapp_temperature
	}

	public OneM2MContentInstanceMapper(OneM2MContentInstanceDTO dto) {
		this.dto = dto;
		this.baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
		this.setContentType();
	}

	public OneM2MContentInstanceMapper(OneM2MContentInstanceDTO dto, Configuration config) {
		this.dto = dto;
		this.baseuri = config.getStringProperty("com.pineone.icbms.sda.knowledgebase.uri");
		this.setContentType();
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
		contentInstance = model.createResource(baseuri + this.dto.get_uri());
		parentResource = model.createResource(baseuri + Utils.getParentURI(this.dto.get_uri()));
		contentInfo = dto.getCnf();
		resourceName = dto.getRn();

		this.setCreateDate(this.dto.getCt());
		this.setLastModifiedDate(this.dto.getLt());
		
		label = "";
		for (int i = 0; i < dto.getLbl().length; i++) {
			label = label + "," + dto.getLbl()[i];
		}

		// getDecodedContent
		if (contentInfo.contains("text/plain:0")) {
			isEncoded = 0;
		} else if (contentInfo.contains("application/json:1")) {
			isEncoded = 1;
		}
		content = dto.getCon(isEncoded);
	}

	/**
	 *   Content Resource 확인
	 * @return
	 */
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

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#from()
	 */
	@Override
	public List<Statement> from() {

		initResource();

		// type1
		Statement stmtType = model.createStatement(contentInstance, RDF.type,
				model.createResource("http://www.iotoasis.org/ontology/ContentInstance"));
		slist.add(stmtType);

		// name
		Statement stmtName = model.createStatement(contentInstance,
				model.createProperty("http://www.iotoasis.org/ontology/resourceName"), resourceName);
		slist.add(stmtName);

		// parent resource
		Statement stmtParentResource = model.createStatement(contentInstance,
				model.createProperty(ICBMSResource.baseuri_ont + "/hasParentResource"), parentResource);
		slist.add(stmtParentResource);

		// subclass of
		Statement stmtIsResourceOf = model.createStatement(contentInstance,
				model.createProperty(ICBMSResource.baseuri_ont + "/isContentInstanceOf"), parentResource);
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
					model.createProperty(ICBMSResource.baseuri_ont + "/hasStatusCondition"), recognizeContentResource());
		} else {
			stmtContent = model.createStatement(contentInstance,
					model.createProperty("http://www.iotoasis.org/ontology/hasContentValue"),
					this.getTypedContent(this.content));
		}
		slist.add(stmtContent);

		switch (this.getContentType()) {

		case "userinout":
			Resource userinout = model.createResource(baseuri + "/GoInOut_" + UUID.randomUUID());
			byte[] encodedContent = this.dto.getCon().getBytes();
			slist.add(model.createStatement(contentInstance,
					model.createProperty("http://www.iotoasis.org/ontology/hasActivityInfo"), userinout));
			try {

				slist.addAll(
						new UserInOutServiceMapper(new String(Base64.decodeBase64(encodedContent)), userinout)
								.from());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		case "dormapp_temperature":
			String ret = "";
			String delete_hasInHouse_ql =  ""
					+" prefix o: <http://www.iotoasis.org/ontology/> "
					+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "				
					+" delete  { <@{arg0}> o:hasInHouse ?o . } "
					+" where   { <@{arg0}> o:hasInHouse  ?o  .} "   ;
			String insert_hasInHouse_ql =   ""
					+" prefix o: <http://www.iotoasis.org/ontology/> "
					+" prefix xsd: <http://www.w3.org/2001/XMLSchema#> "				
					+" prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+" insert data { <@{arg0}> o:hasInHouse \"@{arg1}\"^^xsd:string . }" ;
			
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			encodedContent = StringEscapeUtils.unescapeJava(this.dto.getCon()).getBytes();
			String tmp_ret = new String(Base64.decodeBase64(encodedContent));
			
			DomappTempDTO dt = gson.fromJson(tmp_ret, DomappTempDTO.class);
			int inCnt  = 0;
			for(int m = 0; m < dt.student.length; m++) {
				if(dt.student[m].getInhouse().equals("Y")) {
					inCnt++;
				} else if(dt.student[m].getInhouse().equals("N")) {
					// pass
				}
			}
			if(dt.student != null && dt.student.length != 0) {
				if(inCnt > 0)
					ret = "Y";
				else 
					ret = "N";
			} else {
				ret =  "N";
			}
			
			Literal inhouse = model.createLiteral(ret);
			slist.add(model.createStatement(contentInstance,
					model.createProperty("http://www.iotoasis.org/ontology/hasInHouse"), inhouse));
			
			try {
				QueryService sparqlService = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL);
				((SparqlFusekiQueryImpl)sparqlService.getImplementClass()).updateSparql(delete_hasInHouse_ql, insert_hasInHouse_ql, new String[]{this.getInstanceUri(), ret}, Utils.QUERY_DEST.DM.toString());
			} catch (Exception e) {
				log.debug("Exception in updating hasInHouse ====>"+e.toString());
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
						model.createProperty(ICBMSResource.baseuri_ont + "/hasPresenceContent"),
						model.createResource(baseuri + "/" + con2));
				slist.add(presenceStatement);
			} else if (con2.contains("present") || con2.contains("presence")) {
				Statement presenceStatement = model.createStatement(contentInstance,
						model.createProperty(ICBMSResource.baseuri_ont + "/hasPresenceContent"),
						model.createResource(baseuri + "/" + con2));
				slist.add(presenceStatement);
			}
			break;
			
		}

		// time
		if(this.dto.getCt() != null && ! this.dto.getCt().equals("")) {
			Statement stmtCreateTime = model.createStatement(contentInstance,
					model.createProperty(ICBMSResource.baseuri_ont + "/hasCreateDate"), 
					this.dto.getCt());
			slist.add(stmtCreateTime);
		}

		if(this.dto.getLt() != null || ! this.dto.getLt().equals("")) {
			Statement stmtLastModifiedTime = model.createStatement(contentInstance,
					model.createProperty(ICBMSResource.baseuri_ont + "/hasLastModifiedDate"),
					    this.dto.getLt());
			slist.add(stmtLastModifiedTime);
		}

		return slist;
	}

	/**
	 * content type 설정
	 * @return void
	 */
	public void setContentType() {
		if (this.dto.get_uri().toLowerCase().contains(EnumContentType.survey.toString())) {
			this.setContentType(EnumContentType.survey);
		} else if (this.dto.get_uri().toLowerCase().contains(EnumContentType.userinout.toString())) {
			this.setContentType(EnumContentType.userinout);
		} else if (this.dto.get_uri().toLowerCase().contains(EnumContentType.presence.toString())) {
			this.setContentType(EnumContentType.present);
		} else if (this.dto.get_uri().toLowerCase().contains(EnumContentType.dormapp_temperature.toString())) {
			this.setContentType(EnumContentType.dormapp_temperature);			
		} else {
			this.setContentType(EnumContentType.normal);
		}
	}

	/**
	 * content type가져오기
	 * @return String
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * content type 설정
	 * @param contentType
	 * @return void
	 */
	public void setContentType(Enum contentType) {
		this.contentType = contentType.toString();
	}

	/**
	 * typed content type 가져오기
	 * @param con
	 * @return Literal
	 */
	public Literal getTypedContent(String con) {
		try {

			return ResourceFactory.createTypedLiteral(Double.valueOf(con));
		} catch (java.lang.NumberFormatException e) {
			try {
				return ResourceFactory.createTypedLiteral(Float.valueOf(con));
			} catch (Exception e2) {
				return ResourceFactory.createTypedLiteral(String.valueOf(con));
			}
		}

	}

	/**
	 * instance uri 가져오기
	 * @return String
	 */
	public String getInstanceUri(){
		return baseuri + this.dto.get_uri();
	}
	
	/**
	 * content 가져오기
	 * @return String
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * 부모 리소스 uri 가져오기
	 * @return String
	 */
	public String getParentResourceUri(){
		return baseuri + Utils.getParentURI(dto.get_uri());
	}
	
	/**
	 * 커넥션 닫기
	 * @return void
	 */
	public void close() {
		if(! model.isClosed()) model.close();
	}

}