package com.pineone.icbms.sda.sch.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.pineone.icbms.sda.comm.kafka.onem2m.AvroRdbmsTimeTableWattagePublish; 
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.mapper.rdbms.TimeTableWattageMapper;
import com.pineone.icbms.sda.sch.comm.SchedulerJobComm;

@Service
public class CollectTimeTableWattageFromSSJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private static AtomicInteger ai = new AtomicInteger();
	
	public void collect(String db_server, int db_port, String db_name, String db_user, String db_pass, String save_path,
			JobExecutionContext jec) throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		// 중복방지
		start_time = start_time + "S"+String.format("%010d", ai.getAndIncrement());

		String end_time = Utils.dateFormat.format(new Date());
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		log.info("CollectTimeTableWattageFromSSJobService(id : " + jec.getJobDetail().getName() + ") start.......................");

		StringBuffer sb = new StringBuffer();
		
		String triple_path_file = "";
		String triple_check_result = "";
		String triple_check_result_file = "";

		// task_group_id, task_id에 대한 schDTO정보
		// schDTO = getSchDTO(jec);

		StringBuffer sql = new StringBuffer();
		
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:',lower(room_id), ' rdf:type ' , 'o:ClassRoom .') as col from  grib.tb_wate_limit where place_name = '강의실' union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:',lower(room_id), ' rdf:type ' , 'o:Domitory .') as col from  grib.tb_wate_limit where place_name = '기숙사' union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:',lower(room_id), ' rdf:type ' , 'o:Laboratory .') as col from  grib.tb_wate_limit where place_name = '실험실' union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:',lower(room_id), ' rdf:type ' , 'o:Warehouse .') as col from  grib.tb_wate_limit where place_name = '기자제' union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:',lower(room_id), ' o:name \"',room_name , '\"^^xsd:string .') as col from  grib.tb_wate_limit union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:',lower(room_id), ' o:hasElectricPowerLimit \"' , wate,'\"^^xsd:integer .') as col from  grib.tb_wate_limit   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' rdf:type o:Lecture .' ) as col from  grib.tb_lecture union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:name \"' , lecture_name_eng,'\"^^xsd:string .') as col from  grib.tb_lecture  union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' rdfs:label \"' , lecture_name,'\"^^xsd:string .') as col from  grib.tb_lecture  union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:takePlace ' , 'o:', lower(room_id) , ' .') as col from  grib.tb_lecture union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:monday .') as col from  grib.tb_lecture where lecture_week=1   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:tuesday .') as col from  grib.tb_lecture where lecture_week=2   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:wednesday .') as col from  grib.tb_lecture where lecture_week=3   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:thursday .') as col from  grib.tb_lecture where lecture_week=4   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:friday .') as col from  grib.tb_lecture where lecture_week=5   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:saturday .') as col from  grib.tb_lecture where lecture_week=6   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasLectureDaysOfWeek ' , 'o:sunday .') as col from  grib.tb_lecture where lecture_week=7   union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasStartTime \"' ,lecture_start_time ,'\"^^xsd:string .') as col from  grib.tb_lecture union all    ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('o:Lecture_',lecture_id,   ' o:hasEdTime \"' ,lecture_end_time ,'\"^^xsd:string .') as col from  grib.tb_lecture ");

		log.debug("sql ==>\n"+sql.toString());
		
		List<java.lang.CharSequence> list = new ArrayList<java.lang.CharSequence>();
		
		int cnt = 0;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mariadb://" + db_server + ":" + db_port + "/" + db_name, db_user,
					db_pass);

			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				String col = rs.getString("col");

				// stringbuffer에 저장, 문제있는 row는 skip하고 정상적인것은 triple로 변환함
				try {
					sb.append(new TimeTableWattageMapper(col).from());
					sb.append(Utils.NEW_LINE);
				} catch (Exception e) {
					e.printStackTrace();
				}

				cnt++;
			}

			// sch_hist테이블에 data insert
			//insertSchHist(jec, -1, start_time, endDate);
			insertSchHist(jec, cnt, start_time, Utils.dateFormat.format(new Date()));

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
				}
			}
		}
		
		// json으로 변환
		list.add(getJson(sb.toString()));
		
		// kafka 전송
		AvroRdbmsTimeTableWattagePublish avroRdbmsTimeTableWattagePublish = new AvroRdbmsTimeTableWattagePublish(Utils.BROKER_LIST);
		
		com.pineone.icbms.sda.comm.kafka.avro.COL_RDBMS rDBMS = new com.pineone.icbms.sda.comm.kafka.avro.COL_RDBMS();
		
		rDBMS.setColFrom(Utils.COL_SS_TIMETABLE_WATT_DATA);
		rDBMS.setReadFromTime(String.format("%s", new Date().getTime()));
		rDBMS.setWriteQueueTime(String.format("%s", new Date().getTime()));
		rDBMS.setTaskGroupId(jec.getJobDetail().getGroup());
		rDBMS.setTaskId(jec.getJobDetail().getName());
		rDBMS.setStartTime(start_time);
		rDBMS.setData(list);
		
		// 전송시작
		log.debug("Sending Rdbms(TimeTable and Wattage) start ......................");		
		avroRdbmsTimeTableWattagePublish.send(rDBMS);

		// 전송끝 
		avroRdbmsTimeTableWattagePublish.close();
		log.debug("Sending Rdbms(TimeTable and Wattage) end ......................");		

		if(cnt > 0 ) {
			triple_check_result_file = Utils.DataSaved;
			triple_path_file = Utils.DataSaved;
			triple_check_result = Utils.DataSaved;
		} else {
			triple_check_result_file = Utils.None;
			triple_path_file = Utils.None;
			triple_check_result = Utils.None;
		}

		// sch_hist테이블의 finish_time에 날짜및 정보 설정
		String finish_time = Utils.dateFormat.format(new Date());
		updateFinishTime(jec, start_time, finish_time, triple_check_result_file, triple_path_file, triple_check_result);
		
		// endDate값을 sch테이블의 last_work_time에 update
		updateLastWorkTime(jec, end_time);
		
		log.info("CollectTimeTableWattageFromSSJobService(id : " + jec.getJobDetail().getName() + ") end.......................");
	}

	public void execute(JobExecutionContext arg0)  throws JobExecutionException{
		String db_server, db_name, db_user, db_pass, save_path;
		int db_port;

		db_server = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.server");
		db_port = Integer.parseInt(Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.port"));
		db_name = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.name.timetable");
		db_user = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.user");
		db_pass = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.pass");
		save_path = Utils.getSdaProperty("com.pineone.icbms.sda.triple.save_path");

		// 폴더가 없으면 생성(년월일을 폴더에 포함함)
		save_path = Utils.makeSavePath(save_path);

		try {
			collect(db_server, db_port, db_name, db_user, db_pass, save_path, arg0);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception ("+this.getClass()+")  ....................................> "+e.toString());			
			throw new JobExecutionException(e);
		}
	}
	
	private String getJson(Object doc) throws Exception {
		String rtn = "";
		if (doc instanceof DBObject) {
			DBObject docT = (DBObject) doc;
			//ty = (Integer) docT.get("ty");
			rtn =  new Gson().toJson(docT);
		} else if (doc instanceof String) {
			rtn =  new Gson().toJson(doc);
		}
		
		//log.debug("rtn string ======>: " + rtn);
		return rtn;
	}


}
