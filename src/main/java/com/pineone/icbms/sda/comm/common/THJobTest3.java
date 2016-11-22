package com.pineone.icbms.sda.comm.common;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class THJobTest3 implements Job {
	Date startTime;
	
    public THJobTest3() {
		super();
		startTime = new Date();
	}

	public void execute(JobExecutionContext context)  {
        //System.out.println("Test Job3 = " + new Date());
        System.out.println("Test Job3(startTime : "+startTime+") = " + new Date());
    }
}