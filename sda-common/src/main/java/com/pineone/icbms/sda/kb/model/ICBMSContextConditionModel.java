package com.pineone.icbms.sda.kb.model;

import java.util.List;
import java.lang.reflect.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ICBMSContextConditionModel {

	private String subject;
	private String predicate;
	private String object;

	public ICBMSContextConditionModel(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}

	

	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getPredicate() {
		return predicate;
	}



	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}



	public String getObject() {
		return object;
	}



	public void setObject(String object) {
		this.object = object;
	}



	public static void main(String[] args) {

		// uri, arg1, arg2...argn
		String domain = "http://www.loa-cnr.it/ontologies/DUL.owl#PhysicalPlace";
		String samplejson = "[{\"subject\":\"?arg1\", \"predicate\":\"dul:hasLocation\", \"object\":\"uri\"},"
				+ "{\"subject\":\"?arg1\", \"predicate\":\"rdf:type\", \"object\":\"m2m:TemperatureSensor\" }"
				+ "] ";
		Gson gson = new Gson();
		Type type = new TypeToken<List<ICBMSContextConditionModel>>() {
		}.getType();
		List<ICBMSContextConditionModel> clist = gson.fromJson(samplejson, type);
		
		clist.get(1);
		
		System.out.println("type ===>"+type.toString());
		System.out.println("clist.get(1)===>"+clist.get(1).toString());
	  

	}
}
