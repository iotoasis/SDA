package com.pineone.icbms.sda.comm.conf;

import java.io.Serializable;

/**
 * 설정정보 Factory클래스
 */
public class ConfigurationFactory implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 설정정보 인스턴스 생성
	 * @param host
	 * @param port
	 * @return Configuration
	 */
	public static Configuration create(String host, int port) {
		return new ConfigurationImpl(host, port);
		
	}
	
	/**
	 * 설정정보 인스턴스 생성
	 * @return Configuration
	 */
	public static Configuration createConfiguration() {
		return new ConfigurationImpl();		
	}
	
	/**
	 * 설정정보 인스턴스 생성
	 * @param ctx
	 * @return Configuration
	 */
	public static Configuration createConfiguration(Context ctx) {
		return new ConfigurationImpl(ctx);		
	}
	/**
	 * 설정정보 인스턴스 생성
	 * @param configuration_file
	 * @return Configuration
	 */
	public static Configuration createConfiguration(String configuration_file) {
		return new ConfigurationImpl(configuration_file);
	}
	
	/**
	 * 설정정보 인스턴스 생성
	 * @param home_path
	 * @param configuration_file
	 * @param ctx
	 * @return Configuration
	 */
	public static Configuration createConfiguration(String home_path, String configuration_file, Context ctx) {
		return new ConfigurationImpl(home_path, configuration_file, ctx);
	}
}
