package com.pineone.icbms.sda.kb.query;

import java.io.StringWriter;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

public class DeviceResource {
	
	final String endpoint = "http://166.104.112.43:23030/icbms/sparql";

	final String prefix = getHeaderForTripleFile();
	
	public DeviceResource() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String  getDeviceInfo(String deviceUri) {
		StringWriter out = new StringWriter();
		String query = prefix + "\n"+ "describe "+ deviceUri;
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		Model model =   qe.execDescribe();
		qe.close();
		model.write(out,"RDF/XML");
		return out.toString();
	}
	
	public static final String getHeaderForTripleFile() {
	    String headerForTripleFile = ""+
	    		    "PREFIX dul: <http://www.loa-cnr.it/ontologies/DUL.owl#> \n" +
	    			"PREFIX fn: <http://www.ontologydesignpatterns.org/ont/framenet/tbox/> \n" +
	    			"prefix swrlb: <http://www.w3.org/2003/11/swrlb#>   \n" +
	    			"prefix protege: <http://protege.stanford.edu/plugins/owl/protege#> \n" + 
	    			"prefix ssn: <http://purl.oclc.org/NET/ssnx/ssn#>  \n" +
	    			"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" + 
	    			"prefix dct: <http://purl.org/dc/terms/>   \n" +
	    			"prefix dc: <http://purl.org/dc/elements/1.1/>   \n" +
	    			"prefix j.0: <http://data.qudt.org/qudt/owl/1.0.0/text/> \n" +  
	    			"prefix owl: <http://www.w3.org/2002/07/owl#>   \n" +
	    			"prefix xsp: <http://www.owl-ontologies.com/2005/08/07/xsp.owl#> \n" +  
	    			"prefix swrl: <http://www.w3.org/2003/11/swrl#>   \n" +
	    			"prefix skos: <http://www.w3.org/2004/02/skos/core#>   \n" +
	    			"prefix dul: <http://www.loa-cnr.it/ontologies/DUL.owl#>   \n" +
	    			"prefix cc: <http://creativecommons.org/ns#>   \n" +
	    			"prefix foaf: <http://xmlns.com/foaf/0.1/>   \n" +
	    			"prefix xsd: <http://www.w3.org/2001/XMLSchema#> \n" +  
	    			"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>   \n" +
	    			"prefix qudt: <http://data.nasa.gov/qudt/owl/qudt#>   \n" +
	    			"prefix quantity: <http://data.nasa.gov/qudt/owl/quantity>   \n" +
	    			"prefix unit: <http://data.nasa.gov/qudt/owl/unit#>   \n" +
	    			"prefix dim: <http://data.nasa.gov/qudt/owl/dimension#>   \n" +
	    			"prefix b: <http://www.onem2m.org/ontology/Base_Ontology#>   \n" +
	    			"prefix foaf: <http://xmlns.com/foaf/0.1/>   \n" +
	    			"prefix herit: <http://www.iotoasis.org/herit-in/herit-cse/>  \n" +
	    			"prefix o: <http://www.iotoasis.org/ontology/> \n";
	    return headerForTripleFile;
	}
	public static void main(String[] args) {
		DeviceResource d = new DeviceResource();
		
		System.out.println(  d.getDeviceInfo("<http://www.iotoasis.org/herit-in/herit-cse/ONSB_BleScanner01_001>"));
		
	}
}
