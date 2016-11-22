package com.pineone.icbms.sda.comm.exception;

import org.springframework.http.HttpStatus;

public class UserDefinedException extends Exception {
	final static long serialVersionUID = 2L;
	private HttpStatus httpStatus;
	private String msg;
  
    public UserDefinedException(HttpStatus httpStatus, String msg) {
    	this.httpStatus = httpStatus;
    	this.msg = msg;
    }

    public UserDefinedException(HttpStatus httpStatus) {
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
 
