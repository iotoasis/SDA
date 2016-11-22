package com.pineone.icbms.sda.comm.util;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XmlCalendar2Date {
	public static void main(String args[]) {
		String str = "2012-01-01T01:01:01";
		System.out.println(toDate(str));
		System.out.println(toXmlGregorianCalendar(str));
		
		
		
		
		

	}

	/*
	 * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(date);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException ex) {
//			Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);
		}
		return xmlCalendar;
	}

	/*
	 * Converts XMLGregorianCalendar to java.util.Date in Java
	 */
	public static Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}
	
	public static XMLGregorianCalendar toXmlGregorianCalendar(String str){

		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(str);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public static Date toDate(String str){

		try {
			return toDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(str));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
