package com.pineone.icbms.sda.comm.exception;

import org.springframework.http.HttpStatus;

/**
 * 원격 SO Exception
 */
public class RemoteSOException extends Exception {
	private static final long serialVersionUID = 6753769994807749L;
	private HttpStatus httpStatus;
	private String msg;
	  
    public RemoteSOException(HttpStatus httpStatus, String msg) {
    	this.httpStatus = httpStatus;
    	this.msg = msg;
    }

    public RemoteSOException(HttpStatus httpStatus) {
    	this.httpStatus = httpStatus;
    }

    public String getMsg() {
 	   if(msg == null || msg.equals("")) {
		   return httpStatus.getReasonPhrase();		   
	   } else {
		   return msg;
	   }
    }
   
   public int getCode() {
      return httpStatus.value();
   }
  
 }