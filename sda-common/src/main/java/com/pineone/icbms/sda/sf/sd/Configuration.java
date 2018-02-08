package com.pineone.icbms.sda.sf.sd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *   설정정보 클래스 파일
 */
public class Configuration extends Properties {
	private static final long serialVersionUID = 1L;

	public Configuration() {
		new Configuration(new File("query.sql"));
	}

	public Configuration(File config) {
		BufferedReader br = null;
		try {
			String key  = "";
			String value = "";
			br = new BufferedReader(new InputStreamReader(new FileInputStream(config)));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.startsWith("^")) {
					if(value.length()>1){
						this.put(new String(key), new String(value));
						key="";
						value="";
					}
					key = line.trim().replaceAll("\\^", "");
				} else {
					value = value + line +"\n";
				}
			}
			this.put(new String(key),new String(value));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) 
				try { br.close(); } catch (Exception e) {}
		}

	}

	public Configuration(String config) {
		new Configuration(new File(config));
	}
	

}
