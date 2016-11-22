package com.pineone.icbms.sda.comm.service;

import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.sch.dto.SchDTO;
import com.pineone.icbms.sda.sch.service.SchService;

@Service
public class SchedulerMainService implements ApplicationContextAware {
	private static ApplicationContext context;
	private SchedulerFactory schedulFactoty = null;
	private Scheduler scheduler = null;
	private JobDetail jobDetail = null;
	private CronTrigger trigger = null;
	private SchDTO[] schDTO;

	private Log log = LogFactory.getLog(this.getClass());

	public void JobInit() throws Exception {
		try {
			if (scheduler != null && scheduler.isStarted())
				return;

			schedulFactoty = new StdSchedulerFactory();
			scheduler = schedulFactoty.getScheduler();
			scheduler.start();
			JobRegist();
		} catch (Exception e) {
			throw e;
		}
	}

	public Scheduler getScheduler() throws Exception {

		if (scheduler == null) {
			schedulFactoty = new StdSchedulerFactory();
			scheduler = schedulFactoty.getScheduler();
		}
		return scheduler;
	}

	public void JobRegist() throws Exception {
		Class c = null;
		// 등록할 스케줄 정보 설정
		setSchList();

		for (int j = 0; j < schDTO.length; j++) {
			try {
				c = Class.forName(schDTO[j].getTask_class());

				jobDetail = new JobDetail(schDTO[j].getTask_id(), schDTO[j].getTask_group_id(), c);
				trigger = new CronTrigger(schDTO[j].getTask_id(), schDTO[j].getTask_group_id());

				trigger.setCronExpression(schDTO[j].getTask_expression());
				scheduler.scheduleJob(jobDetail, trigger);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	// 상태보기
	public void statusSchList() throws Exception {
		setSchList();
		for (int j = 0; j < schDTO.length; j++) {
			log.debug("trigger[" + j + "] : "
					+ scheduler.getTriggerState(schDTO[j].getTask_id(), schDTO[j].getTask_group_id())
					+ " (not initiated:-1, running/standby:0,  paused:1)");
		}
	}

	// 상태값
	public String getStatusList() throws Exception {
		StringBuffer sb = new StringBuffer();
		setSchList();

		for (int j = 0; j < schDTO.length; j++) {
			sb.append("trigger[");
			sb.append(j);
			sb.append("](group_id:");
			sb.append(schDTO[j].getTask_group_id());
			sb.append(",");
			sb.append(" task_id:");
			sb.append(schDTO[j].getTask_id());
			sb.append(") : ");
			sb.append(scheduler.getTriggerState(schDTO[j].getTask_id(), schDTO[j].getTask_group_id()));
			sb.append(" (not initiated:-1, running/standby:0, paused:1)");
			if ((j + 1) != schDTO.length) {
				sb.append("<br>");
			}
		}
		return sb.toString();
	}

	// pause
	public void pauseSch() throws Exception {
		setSchList();
		for (int j = 0; j < schDTO.length; j++) {
			scheduler.pauseJob(schDTO[j].getTask_id(), schDTO[j].getTask_group_id());
			log.debug("[" + j + "] paused......");
		}
	}

	// resume
	public void resumeSch() throws Exception {
		setSchList();
		for (int j = 0; j < schDTO.length; j++) {
			scheduler.resumeJob(schDTO[j].getTask_id(), schDTO[j].getTask_group_id());
			log.debug("[" + j + "] resumed......");
		}
	}

	// standby
	public void standbySch() throws Exception {
		setSchList();
		scheduler.standby();
		log.debug("scheduler is in state of standby ......");
	}

	// start
	public void startSch() throws Exception {
		setSchList();
		scheduler.start();
		log.debug("scheduler is in state of start ......");
	}

	// shutdown
	public void shutdown() throws Exception {
		scheduler.shutdown();
	}

	private void setSchList() throws Exception {
		if (schDTO != null)
			return;

		// 스케줄정보 가져오기
		List<SchDTO> schList = new ArrayList<SchDTO>();

		try {
			SchService schService = getContext().getBean(SchService.class);
			schList = schService.selectList();
		} catch (SQLNonTransientConnectionException e) {
			// 쿼리파이프가 깨짐, 여러번 retry하면 접속됨 혹은 gc해서 restart하게함(?)
			System.gc();
		} catch (Exception e) {
			throw e;
		}

		schDTO = new SchDTO[schList.size()];

		for (int i = 0; i < schList.size(); i++) {
			schDTO[i] = schList.get(i);
			log.debug("schDTO[" + i + "] in SchedulerMainService================>" + schDTO[i].toString());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

}
