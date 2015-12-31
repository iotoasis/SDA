package com.pineone.icbms.sda.kb.dto;

import org.apache.jena.rdf.model.Statement;

import com.google.gson.Gson;
import com.pineone.icbms.sda.kb.model.TripleMap;

public class UserInOutInfoDTO implements OneM2MDTO{
	
	private String direction;
	private String user_id;
	private String zone;

	public UserInOutInfoDTO(String direction, String user_id, String zone){
		this.setDirection(direction);
		this.setUser_id(user_id);
		this.setZone(zone);
	}
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TripleMap<Statement> getTriples() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return "{\"direction\":\""+this.getDirection()+"\",\"user_id\":\""+this.getUser_id()+"\",\"zone\":\""+this.getZone()+"\"}";
	}
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		String sample = "{\"direction\":\"in\",\"user_id\":\"u00002\",\"zone\":\"강의실\"}";
		UserInOutInfoDTO cont = gson.fromJson(sample, UserInOutInfoDTO.class);
		System.out.println(cont);
	}

}
