package com.pineone.icbms.sda.kb.mapper.onem2m;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.pineone.icbms.sda.comm.conf.Configuration;
import com.pineone.icbms.sda.comm.util.StrUtils;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MContainerDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;
import com.pineone.icbms.sda.kb.model.ICBMSResource;

/**
 *   Container의 Mapper 클래스
 */
public class OneM2MContainerMapper implements OneM2MMapper {
	private List<Statement> slist = new ArrayList<Statement>();
	private Model model = ModelFactory.createDefaultModel();
	private String baseuri = "";
	private OneM2MContainerDTO dto;
	private Resource container;
	private String resourceId;
	private String resourceName;
	private Resource parentResource;
	private String label="";
	private String ontologyReference ;

	public OneM2MContainerMapper(OneM2MContainerDTO dto) {
		this.dto = dto;
		this.baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	}

	public OneM2MContainerMapper(OneM2MContainerDTO dto, Configuration config) {
		this.dto = dto;
		this.baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
		container = model.createResource(baseuri + this.dto.get_uri());
		parentResource = model.createResource(baseuri + "/" + Utils.getParentURI(this.dto.get_uri()));
		resourceName = dto.getRn();
		resourceId = dto.getRi();
		for(int i=0;i<dto.getLbl().length; i++){
			label = label+","+dto.getLbl()[i];
		}
		ontologyReference = this.dto.getOr();
		
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#from()
	 */
	@Override
	public List<Statement> from() {
		initResource();

		// Container type 
		Statement stmtType = model.createStatement(container, RDF.type,
				model.createResource(ICBMSResource.baseuri_ont+"/Container"));
		slist.add(stmtType);

		// Ontology Reference Type
		if (dto.getOr() != null) {
			Statement stmtType2 = model.createStatement(container, RDF.type, model.createResource(ontologyReference));
			slist.add(stmtType2);
		}

		// name
		Statement stmtName = model.createStatement(container,
				model.createProperty(ICBMSResource.baseuri_ont+"/resourceName"), resourceName);
		slist.add(stmtName);

		// parent resource
		Statement stmtParentResource = model.createStatement(container,
				model.createProperty(ICBMSResource.baseuri_ont+"/hasParentResource"), parentResource);
		slist.add(stmtParentResource);
		
		// parent resource
		Statement stmtIsResoucrOf = model.createStatement(container,
				model.createProperty(ICBMSResource.baseuri_ont+"/isContainerOf"), parentResource);
		slist.add(stmtIsResoucrOf);

		// label
		Statement stmtLabel = model.createStatement(container, RDFS.label, label);
		slist.add(stmtLabel);

		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		Calendar cal1 = Calendar.getInstance();
		try {
			cal1.setTime(sd.parse(StrUtils.makeXsdDateFromOnem2mDate(this.dto.getCt())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Statement stmtCreateTime = model.createStatement(container,
				model.createProperty(ICBMSResource.baseuri_ont+"/hasCreateDate"), model.createTypedLiteral(cal1));
		slist.add(stmtCreateTime);

		// last modified time
		Calendar cal2 = Calendar.getInstance();
		try {
			cal2.setTime(sd.parse(StrUtils.makeXsdDateFromOnem2mDate(this.dto.getLt())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement stmtLastModifiedTime = model.createStatement(container,
				model.createProperty(ICBMSResource.baseuri_ont+"/hasLastModifiedDate"),
				model.createTypedLiteral(cal2));
		slist.add(stmtLastModifiedTime);

		// creator
		Statement stmtCreator = model.createStatement(container, DC.creator, baseuri+this.dto.getCr());
		slist.add(stmtCreator);

		//resourceid
		Statement stmtResourceId = model.createStatement(container,
				model.createProperty(ICBMSResource.baseuri_ont+"/hasResourceId"), resourceId);
		slist.add(stmtResourceId);
		
		return slist;
	}
	
	// gooper
	public void close() {
		if(! model.isClosed()) model.close();
	}
}
