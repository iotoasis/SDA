package com.pineone.icbms.sda.comm.common;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class THJobTest2 implements Job{   
	Date startTime;
	
    public THJobTest2() {
		super();
		startTime = new Date();
	}	
	public void execute(JobExecutionContext context)   {
        //System.out.println("Test Job2 = " + new Date());
        System.out.println("Test Job2(startTime : "+startTime+") = " + new Date());
    }
}