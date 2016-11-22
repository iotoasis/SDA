package com.pineone.icbms.sda.kb.mapper.onem2m;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.pineone.icbms.sda.comm.conf.Configuration;
import com.pineone.icbms.sda.comm.conf.ConfigurationFactory;
import com.pineone.icbms.sda.comm.util.StrUtils;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MContainerDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;
import com.pineone.icbms.sda.kb.model.ICBMSResource;

public class OneM2MContainerMapper implements OneM2MMapper {

	// private Configuration config = null;
	private List<Statement> slist = new ArrayList<Statement>();
	private Model model = ModelFactory.createDefaultModel();
	private String baseuri = ICBMSResource.baseuri;
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

	/**
	 * essential resource initialize
	 */
	@Override
	public void initResource() {
		// container = model.createResource(baseuri + "/" + this.dto.getRi());

		container = model.createResource(baseuri + this.dto.get_uri());
		parentResource = model.createResource(baseuri + "/" + Utils.getParentURI(this.dto.get_uri()));
		resourceName = dto.getRn();
		resourceId = dto.getRi();
		for(int i=0;i<dto.getLbl().length; i++){
			label = label+","+dto.getLbl()[i];
		}
		ontologyReference = this.dto.getOr();
		
	}

	/**
	 * triple object retrieve
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public List<Statement> from() {
		initResource();

		// Container type 
		Statement stmtType = model.createStatement(container, RDF.type,
				model.createResource(baseuri+"/m2m/Container"));
		slist.add(stmtType);

		// Ontology Reference Type
		if (dto.getOr() != null) {
			Statement stmtType2 = model.createStatement(container, RDF.type, model.createResource(ontologyReference));
			slist.add(stmtType2);
		}

		// name
		Statement stmtName = model.createStatement(container,
				model.createProperty(baseuri+"/m2m/resourceName"), resourceName);
		slist.add(stmtName);

		// parent resource
		Statement stmtParentResource = model.createStatement(container,
				model.createProperty(baseuri+"/m2m/hasParentResource"), parentResource);
		slist.add(stmtParentResource);
		
		// parent resource
		Statement stmtIsResoucrOf = model.createStatement(container,
				model.createProperty(baseuri+"/m2m/isContainerOf"), parentResource);
		slist.add(stmtIsResoucrOf);

		// label
		Statement stmtLabel = model.createStatement(container, RDFS.label, label);
		slist.add(stmtLabel);

		// createtime xsd datetime
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Calendar cal1 = Calendar.getInstance();
		try {
			cal1.setTime(sd.parse(StrUtils.makeXsdDateFromOnem2mDate(this.dto.getCt())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Statement stmtCreateTime = model.createStatement(container,
				model.createProperty(baseuri+"/m2m/hasCreateDate"), model.createTypedLiteral(cal1));
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
				model.createProperty(baseuri+"/m2m/hasLastModifiedDate"),
				model.createTypedLiteral(cal2));
		slist.add(stmtLastModifiedTime);

		// creator
		Statement stmtCreator = model.createStatement(container, DC.creator, baseuri+this.dto.getCr());
		slist.add(stmtCreator);

		//resourceid
		Statement stmtResourceId = model.createStatement(container,
				model.createProperty(baseuri+"/m2m/hasResourceId"), resourceId);
		slist.add(stmtResourceId);
		
		return slist;
	}

	public static void main(String[] args) {	

	}
}
