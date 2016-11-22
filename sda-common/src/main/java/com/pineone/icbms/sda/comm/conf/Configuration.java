package com.pineone.icbms.sda.comm.conf;

import java.io.Serializable;

public interface Configuration extends Serializable {
	void prepare() throws ConfigException;
	void addProjectConfiguration()  throws ConfigException;
	String getStringProperty(String key) throws ConfigException;
	String getStringProperty(String key, String base) throws ConfigException;
	int getIntProperty(String key) throws ConfigException;	
	int getIntProperty(String key, int base) throws ConfigException;	
	boolean contains(String key) throws ConfigException;
	boolean getBooleanProperty(String key) throws ConfigException;	
	boolean getBooleanProperty(String key, boolean base) throws ConfigException;	
	void setMainPath(String path) throws ConfigException;
	String getMainPath() throws ConfigException;
	String getRoot() throws ConfigException;
	void putProperty(String key, Object value);
	public String removeEndPathSeperator(String path) throws ConfigException;
	public String reverseRoot(String path) throws ConfigException;
	public Object reversePrefix(String str) throws ConfigException;

}
