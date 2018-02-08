package com.pineone.icbms.sda.sch.comm;

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

import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.service.SchService;

/**
 * 스케줄러 서비스의 메인 클래스
 */
@Service
public class SchedulerMainService implements ApplicationContextAware {
	private static ApplicationContext context;
	private SchedulerFactory schedulerFactory = null;
	private Scheduler scheduler = null;
	private JobDetail jobDetail = null;
	private CronTrigger trigger = null;
	private SchDTO[] schDTO;

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 스케줄러 초기화
	 * @throws Exception
	 * @return void
	 */
	public void JobInit() throws Exception {
		try {
			if (scheduler != null && scheduler.isStarted())
				return;

			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			scheduler.start();
			JobRegist();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 스케줄러 인스턴스 얻기
	 * @throws Exception
	 * @return Scheduler
	 */
	public Scheduler getScheduler() throws Exception {

		if (scheduler == null) {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
		}
		return scheduler;
	}

	/**
	 * 스케줄러 Job등록
	 * @throws Exception
	 * @return void
	 */
	public void JobRegist() throws Exception {
		Class<?> c = null;
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

	/**
	 * 스케줄러 목록 
	 * @throws Exception
	 * @return void
	 */
	public void statusSchList() throws Exception {
		setSchList();
		StringBuffer sb = new StringBuffer();
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
			sb.append(Utils.NEW_LINE);
		}
		
		log.debug(sb.toString());
	}

	/**
	 * 스케줄러 상태값 목록
	 * @throws Exception
	 * @return String
	 */
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

	/**
	 * 스케줄 pause
	 * @throws Exception
	 * @return void
	 */
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

	/**
	 * 스케줄러 standby상태로 전환
	 * @throws Exception
	 * @return void
	 */
	public void standbySch() throws Exception {
		setSchList();
		scheduler.standby();
		log.debug("scheduler is in state of standby ......");
	}

	/**
	 * 스케줄러 시작
	 * @throws Exception
	 * @return void
	 */
	public void startSch() throws Exception {
		setSchList();
		scheduler.start();
		log.debug("scheduler is in state of start ......");
	}

	/**
	 * 스케줄러 멈춤
	 * @throws Exception
	 * @return void
	 */
	public void shutdown() throws Exception {
		scheduler.shutdown();
	}

	/**
	 * 스케줄 목록 설정
	 * @throws Exception
	 * @return void
	 */
	private void setSchList() throws Exception {
		if (schDTO != null)
			return;

		// 스케줄정보 가져오기
		List<SchDTO> schList = new ArrayList<SchDTO>();

		try {
			SchService schService = getContext().getBean(SchService.class);
			schList = schService.selectList();
		} catch (SQLNonTransientConnectionException e) {
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

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

}
