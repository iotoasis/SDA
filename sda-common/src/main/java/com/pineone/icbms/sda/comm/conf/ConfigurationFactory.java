package com.pineone.icbms.sda.comm.conf;

import java.io.Serializable;

public class ConfigurationFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static Configuration create(String host, int port) {
		Context ctx = new Context("icbms");
		return new ConfigurationImpl(host, port);
		
	}
	
	public static Configuration createConfiguration() {
		return new ConfigurationImpl();		
	}
	
	public static Configuration createConfiguration(Context ctx) {
		return new ConfigurationImpl(ctx);		
	}
	public static Configuration createConfiguration(String configuration_file) {
		return new ConfigurationImpl(configuration_file);
	}
	
	public static Configuration createConfiguration(String home_path, String configuration_file, Context ctx) {
		return new ConfigurationImpl(home_path, configuration_file, ctx);
	}
}
