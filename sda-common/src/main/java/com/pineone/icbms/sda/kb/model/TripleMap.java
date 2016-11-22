package com.pineone.icbms.sda.kb.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pineone.icbms.sda.kb.dto.OneM2MContainerDTO;
import com.pineone.icbms.sda.kb.dto.OneM2MContentInstanceDTO;

public class TripleMap <Statement> {
	private List<Statement> tlist;
	private Iterator<Statement> stmts;
	public TripleMap(){
		tlist = new ArrayList<Statement>();
		stmts = tlist.iterator();
	}
	
	public void add(List<Statement> tripleList){
		tlist = tripleList;
		stmts = tlist.iterator();
	}
	
	public List<Statement>getList(){
		return this.tlist;
	}
	
	public Iterator<Statement> iterator(){
		return this.stmts;
	}
	
	public Statement get(int i){
		return tlist.get(i);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(;stmts.hasNext();){
			sb.append(stmts.next().toString()+"\n");
		}
		return sb.toString();
	}
}
