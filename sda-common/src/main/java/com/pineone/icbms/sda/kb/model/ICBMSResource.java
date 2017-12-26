package com.pineone.icbms.sda.kb.model;

public class ICBMSResource {
	public static final String open = "open";
	public static final String close = "close";
	public static final String success = "success";
	public static final String failure = "failure";
	public static final String contacted = "contacted";
	public static final String notContacted = "not contacted";
	public static final String on = "on";
	public static final String off = "off";
	public static final String detected = "detected";
	public static final String notDetected = "not detected";
	public static final String retrieve = "retrieve";
	public static final String wet = "wet";
	public static final String dry = "dry";
	public static final String reserved = "reserved";
	public static final String siren = "siren";
	public static final String silent = "silent";
	public static final String inhouse = "dormapp_temperature";
	public static final String baseuri_ont = "http://www.iotoasis.org/ontology";
	
	public static final String openUri = baseuri_ont + "/openCondition";
	public static final String closeUri = baseuri_ont + "/closeCondition";
	public static final String successUri = baseuri_ont + "/successCondition";
	public static final String failureUri = baseuri_ont + "/failureCondition";
	public static final String contactedUri = baseuri_ont + "/contactedCondition";
	public static final String notContactedUri = baseuri_ont + "/not-contactedCondition";
	public static final String onUri = baseuri_ont + "/onCondition";
	public static final String offUri = baseuri_ont + "/offCondition";
	public static final String detectedUri = baseuri_ont + "/detectedCondition";
	public static final String notDetectedUri = baseuri_ont + "/not-detectedCondition";
	public static final String retrieveUri = baseuri_ont + "/retrieve";
	public static final String wetUri = baseuri_ont + "/wetCondition";
	public static final String dryUri = baseuri_ont + "/dryCondition";
	public static final String reservedUri = baseuri_ont + "/reserved";
	public static final String sirenUri = baseuri_ont + "/sirenCondition";
	public static final String silentUri = baseuri_ont + "/sirentCondition";
	public static final String inhouseUri = baseuri_ont + "/inhouseCondition";
	public static final String undefiendResourceUri = baseuri_ont+"/undefinedResource";
}
