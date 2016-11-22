package com.pineone.icbms.sda.comm.util;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class SDADatasetAccessor implements DatasetAccessor {
	private String serviceURI = "http://localhost:13030/ds";
	private DatasetAccessor accessor;

	public SDADatasetAccessor() {
		accessor = DatasetAccessorFactory.createHTTP(serviceURI);
	}

	public SDADatasetAccessor(String str_uri) {
		accessor = DatasetAccessorFactory.createHTTP(str_uri);
	}

	public void put(Model model) {

		accessor.putModel(model);
	}

	// public static void main(String[] args) {
	// String serviceURI = "http://localhost:13030/ds";
	// DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
	// accessor.putModel(makeModel());
	//
	// System.out.println(Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.host.port"));
	//
	// }

	private static Model makeSampleModel() {
		String BASE = "http://example/";
		
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("", BASE);
		Resource r1 = model.createResource(BASE + "r1");
		Resource r2 = model.createResource(BASE + "r2");
		Property p1 = model.createProperty(BASE + "p");
		Property p2 = model.createProperty(BASE + "p2");
		RDFNode v1 = model.createTypedLiteral("1", XSDDatatype.XSDinteger);
		RDFNode v2 = model.createTypedLiteral("2", XSDDatatype.XSDinteger);

		r1.addProperty(p1, v1).addProperty(p1, v2);
		r1.addProperty(p2, v1).addProperty(p2, v2);
		r2.addProperty(p1, v1).addProperty(p1, v2);

		return model;
	}

	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getModel(String graphUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsModel(String graphURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putModel(Model data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void putModel(String graphUri, Model data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteDefault() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteModel(String graphUri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(Model data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(String graphUri, Model data) {
		// TODO Auto-generated method stub

	}
}
