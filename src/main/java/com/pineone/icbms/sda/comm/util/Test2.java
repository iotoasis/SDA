package com.pineone.icbms.sda.comm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class Test2 {
	public static void main(String[] args) {
		 String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		 String test = "/herit-in/herit-cse/TempSensor_LR0001TS0001/status/Data/CONTENT_INST_29529";
		 
		 String sparql = " SELECT  distinct ?location where { " 
		  +"?s  rdf:type icbms:Lecture ; @{aaa}XX "
		  +"icbms:starttime ?stime ; "
		  +"DUL:hasLocation ?location ; @{arg1}, @{arg3} "
          +"icbms:hasWeekday icbms:@{sysweekday} . "
	      +"filter(?stime > \"@{systime}\") . "
		  +"} "
		  +"# sysweekday : monday "
		  +"# systime : 0855 ";
		 
		 
		 // SELECT  distinct ?location where { ?s  rdf:type icbms:Lecture ; icbms:starttime ?stime ; DUL:hasLocation ?location ; icbms:hasWeekday 
		 // icbms:sunday . filter(?stime > "0855") . } # sysweekday : monday # systime : 0855 
		 String sysdateFormat2 = Utils.sysdateFormat2.format(new Date());
		 System.out.println("sysdateFormat2 : "+sysdateFormat2);
		 
		 String[] idxVal = new String[]{"aa","bb","cc","dd"};
		 int cnt = 0; 
			
		 int cnt2 = 0;
		 
			StringBuffer parseQl = new StringBuffer();
			String addStr = "";
			String lastStr = "";
			String argStr = "";
			String idx = "";
			int skipCnt = 0;
			while (! sparql.equals("")) {
				try {
					addStr = sparql.substring(0, sparql.indexOf("@{"));
				} catch (StringIndexOutOfBoundsException e) {		// 더이상 "@{"이 없다면 나머지 문자열은 그대로 적용
					parseQl.append(sparql);
					break;
				}
				
				lastStr = sparql.substring(sparql.indexOf("@{"));
				skipCnt += 2;
				argStr = lastStr.substring(skipCnt, lastStr.indexOf("}"));

				System.out.println("addStr["+cnt+"]==========>"+addStr);
				System.out.println("lastStr["+cnt+"]==========>"+lastStr);
				System.out.println("argStr["+cnt+"]==========>"+argStr);
				
				SimpleDateFormat systimeFormat = new SimpleDateFormat("HHmm" );
				SimpleDateFormat sysdateFormat = new SimpleDateFormat("yyyyMMdd" );
				SimpleDateFormat sysdatetimeFormat = new SimpleDateFormat("yyyyMMddHHmmss" );
				SimpleDateFormat sysweekdayFormat = new SimpleDateFormat("u");

				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
				Date now = new Date();
				
				parseQl.append(addStr);			
				if(argStr.equals("systime")) {
					parseQl.append(systimeFormat.format(now));
					skipCnt += 7;
				} else if(argStr.equals("sysdate")) {
					parseQl.append(sysdateFormat.format(now));				
					skipCnt += 7;
				} else if(argStr.equals("sysdatetime")) {
					parseQl.append(sysdatetimeFormat.format(now));				
					skipCnt += 11;
				} else if(argStr.equals("sysweekday")) {
					String d = sysweekdayFormat.format(now);
					String dStr = "";
					if(d.equals("1")) { // 월요일
						dStr = "monday";
					} else if(d.equals("2")) {
						dStr = "tuesday";
					} else if(d.equals("3")) {
						dStr = "wednesday";
					} else if(d.equals("4")) {
						dStr = "thursday";
					} else if(d.equals("5")) {
						dStr = "friday";
					} else if(d.equals("6")) {
						dStr = "saturday";
					} else if(d.equals("7")) { // 일요일
						dStr = "sunday";
					}
					parseQl.append(dStr);
					skipCnt += 10;
				} else if(argStr.startsWith("arg")) {
					skipCnt += 3;
					idx = argStr.substring(3); 			// "arg"이후의 숫자값을 취함
					for(int i = 0;i < 10000; i++) {
						if(Integer.parseInt(idx) == i) {
							System.out.println("idx.length() ===========>"+idx.length());
							parseQl.append(idxVal[i]);
							skipCnt += idx.length();
							break;
						}
					}
				} else {
					skipCnt += argStr.length();
					parseQl.append("@{"+argStr+"}");
				}
				
				// skipCnt만큼 지난 이후의 문자열을 설정
				System.out.println("skipCnt["+cnt+"]==========>"+skipCnt);				
				sparql = lastStr.substring(skipCnt+1);		// '}'에 대한 1 증가
				skipCnt = 0;
				lastStr = "";
				argStr = "";
				cnt++;
			}  // end of while
			
			System.out.println("parseQl==========>"+parseQl.toString());
			
			
			
			int style = DateFormat.MEDIUM;
		    //Also try with style = DateFormat.FULL and DateFormat.SHORT
		    Date date = new Date();
		    DateFormat df;
		    df = DateFormat.getDateInstance(style, Locale.UK);
		    System.out.println("United Kingdom: " + df.format(date));
		    df = DateFormat.getDateInstance(style, Locale.US);
		    System.out.println("USA: " + df.format(date));   
		    df = DateFormat.getDateInstance(style, Locale.FRANCE);
		    System.out.println("France: " + df.format(date));
		    df = DateFormat.getDateInstance(style, Locale.ITALY);
		    System.out.println("Italy: " + df.format(date));
		    df = DateFormat.getDateInstance(style, Locale.JAPAN);
		    System.out.println("Japan: " + df.format(date));
		    
		    
		    System.out.println("현재+10일 => " +Utils.getDate ( 10 )); 
		    
		    System.out.println("aaaa=>"+XmlCalendar2Date.toDate("20150101T010205".replace("T", "")));
		    
		    System.out.println("format ==>"+String.format("%010d", 4210));

	}
}