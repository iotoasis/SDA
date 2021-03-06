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
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import com.pineone.icbms.sda.comm.conf.Configuration;
import com.pineone.icbms.sda.comm.util.StrUtils;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MAEDTO;
import com.pineone.icbms.sda.kb.mapper.OneM2MMapper;
import com.pineone.icbms.sda.kb.model.ICBMSResource;

/**
 *   AE의 mapper클래스
 */
public class OneM2MAEMapper implements OneM2MMapper {

	private List<Statement> slist = new ArrayList<Statement>();
	private Model model = ModelFactory.createDefaultModel();
	private String baseuri = "";
	private OneM2MAEDTO dto;
	private Resource applicationEntity;
	private String resourceId;
	private String resourceName;
	private Resource parentResource;
	private String label = "";
	private String ontologyReference;

	public OneM2MAEMapper(OneM2MAEDTO dto) {
		this.dto = dto;
		this.baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	}

	public OneM2MAEMapper(OneM2MAEDTO dto, Configuration config) {
		this.dto = dto;
		this.baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.kb.mapper.OneM2MMapper#initResource()
	 */
	@Override
	public void initResource() {
		applicationEntity = model.createResource(baseuri + this.dto.get_uri());
		parentResource = model.createResource(baseuri + "/" + Utils.getParentURI(this.dto.get_uri()));
		resourceName = dto.getRn();
		resourceId = dto.getRi();
		if(dto.getLbl() != null) {
			for (int i = 0; i < dto.getLbl().length; i++) {
				label = label + "," + dto.getLbl()[i];
			}
		} else {
			label = "";
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
		Statement stmtType = model.createStatement(applicationEntity, RDF.type,
				model.createResource(ICBMSResource.baseuri_ont + "/ApplicationEntity"));
		slist.add(stmtType);

		// Ontology Reference Type
		if (dto.getOr() != null) {
			Statement stmtType2 = model.createStatement(applicationEntity, RDF.type,
					model.createResource(ontologyReference));
			slist.add(stmtType2);
		}

		// name
		Statement stmtName = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/resourceName"), resourceName);
		slist.add(stmtName);

		// parent resource
		Statement stmtParentResource = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/hasParentResource"), parentResource);
		slist.add(stmtParentResource);
		
		// parent isResourceOf
		Statement stmtIsAEOf = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/isApplicationEntityOf"), parentResource);
		slist.add(stmtIsAEOf);
		
		// parent resource
		Statement stmtResourceOf = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/isApplicationEntityOf"), parentResource);
		slist.add(stmtResourceOf);

		// label
		Statement stmtLabel = model.createStatement(applicationEntity, RDFS.label, label);
		slist.add(stmtLabel);

		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		Calendar cal1 = Calendar.getInstance();
		try {
			cal1.setTime(sd.parse(StrUtils.makeXsdDateFromOnem2mDate(this.dto.getCt())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Statement stmtCreateTime = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/hasCreateDate"), model.createTypedLiteral(cal1));
		slist.add(stmtCreateTime);

		// last modified time
		Calendar cal2 = Calendar.getInstance();
		try {
			cal2.setTime(sd.parse(StrUtils.makeXsdDateFromOnem2mDate(this.dto.getLt())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Statement stmtLastModifiedTime = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/hasLastModifiedDate"), model.createTypedLiteral(cal2));
		slist.add(stmtLastModifiedTime);

		// resourceid
		Statement stmtResourceId = model.createStatement(applicationEntity,
				model.createProperty(ICBMSResource.baseuri_ont + "/hasResourceId"), resourceId);
		slist.add(stmtResourceId);

		// pointofaccess
		if (dto.getPoa().length != 0) {
			for (int i = 0; i < dto.getPoa().length; i++) {
				slist.add(model.createStatement(applicationEntity,
						model.createProperty(ICBMSResource.baseuri_ont + "/hasPointOfAccess"),
						model.createResource(dto.getPoa()[i])));
			}
		}

		// announce attribute
		if (dto.getAa().length != 0) {
			for (int i = 0; i < dto.getAa().length; i++) {
				slist.add(model.createStatement(applicationEntity, model.createProperty(ICBMSResource.baseuri_ont + "/hasAnnounceTo"),
						model.createResource(dto.getAa()[i])));
			}
		}
		// announce to
		if (dto.getAt().length != 0) {
			for (int i = 0; i < dto.getAt().length; i++) {
				slist.add(model.createStatement(applicationEntity,
						model.createProperty(ICBMSResource.baseuri_ont + "/hasAnnounceAttribute"),
						model.createResource(dto.getAt()[i])));
			}
		}

		// appname
		Statement appname = model.createStatement(applicationEntity, model.createProperty(ICBMSResource.baseuri_ont + "/resourceName"),
				model.createResource(dto.getApn()));
		slist.add(appname);
		// appid
		Statement appid = model.createStatement(applicationEntity, model.createProperty(ICBMSResource.baseuri_ont + "/hasAppID"),
				model.createResource(dto.getApi()));
		slist.add(appid);
		
		// aeid
		Statement aeid = model.createStatement(applicationEntity, model.createProperty(ICBMSResource.baseuri_ont + "/hasApplicationEntityID"),
				model.createResource(dto.getApi()));
		slist.add(aeid);

		slist.add(stmtResourceId);
		return slist;
	}
	
	/**
	 * 커넥션 닫기
	 * @return void
	 */
	public void close() {
		if(! model.isClosed()) model.close();
	}

}