package com.pineone.icbms.sda.comm.util;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;

public class Test {
	public void updateTest() {
		// Add a new book to the collection
//		String service = "http://219.248.137.7:13030/icbms/update";
//		UpdateRequest ur = UpdateFactory
//				.create(""
//						+ "delete  { <http://www.pineone.com/campus/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue>  ?value . } "
//						+ "insert  { <http://www.pineone.com/campus/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue>  \"19\" . } " //<-- 19대신 여기 온도를 넣어주세
//						+ "WHERE   { <http://www.pineone.com/campus/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue> ?value . }");
//		UpdateProcessor up = UpdateExecutionFactory.createRemote(ur, service);
//		up.execute();

		// Query the collection, dump output
//		String service1 = "http://219.248.137.7:13030/icbms/";
//		QueryExecution qe = QueryExecutionFactory.sparqlService(service1,
//				"SELECT * WHERE {<http://www.pineone.com/campus/Student_S00001> ?r ?y} limit 20 ");
//
//		ResultSet results = qe.execSelect();
//		ResultSetFormatter.out(System.out, results);
//		qe.close();
		Model model = ModelFactory.createDefaultModel();
		Statement s = model.createStatement(model.createResource("ex:e1"),model.createProperty("ex:p1"), ResourceFactory.createTypedLiteral(new String("0.6")));
		model.add(s);
		String query = "select * {?s ?p ?o }";
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		ResultSetFormatter.out(System.out, results);
		qe.close();
	}

	public static void main(String[] args) {
		Test t = new Test();
//		t.insertTest();
		t.updateTest();
	}
}