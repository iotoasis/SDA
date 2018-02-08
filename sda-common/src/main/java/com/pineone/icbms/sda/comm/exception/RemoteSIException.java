package com.pineone.icbms.sda.comm.exception;

import org.springframework.http.HttpStatus;

/**
 * SI원격오류 Exception클래스
 */
public class RemoteSIException extends Exception {
	private static final long serialVersionUID = 1043429724714381959L;
	private HttpStatus httpStatus;
	private String msg;
	  
    public RemoteSIException(HttpStatus httpStatus, String msg) {
    	this.httpStatus = httpStatus;
    	this.msg = msg;
    }

    public RemoteSIException(HttpStatus httpStatus) {
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