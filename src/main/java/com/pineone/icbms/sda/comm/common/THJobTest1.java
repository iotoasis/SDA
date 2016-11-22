package com.pineone.icbms.sda.comm.common;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

import com.pineone.icbms.sda.comm.service.SchedulerJobComm;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.dto.SchDTO;

@Service
public class THJobTest1 extends SchedulerJobComm implements Job {
	SchDTO schDTO;
	private Log log = LogFactory.getLog(this.getClass());

	public void execute(JobExecutionContext jec) {
		String start_time;
		String finish_time;

		try {
			start_time = Utils.dateFormat.format(new Date());

			// task_group_id, task_id에 대한 schDTO정보
			schDTO = getSchDTO(jec);

			// sch_hist테이블에 data insert
			insertSchHist(jec, 0, start_time, start_time);

			// 메인작업 시작
			Thread.sleep(1000 * 6);
			log.debug("Test Job1 = " + new Date());
			// 메인작업 끝

			// sch_hist테이블의 finish_time에 날짜 설정(start_time은 key3에 해당되므로 key값인
			// strt_time을 보내준다.)
			finish_time = Utils.dateFormat.format(new Date());
			updateFinishTime(jec, start_time, finish_time, "OK");

			// finish_time값을 sch테이블의 last_work_time에 update
			updateLastWorkTime(jec, finish_time);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}