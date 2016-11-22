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
import com.pineone.icbms.sda.comm.kafka.onem2m.AvroRdbmsDeviceInfoPublish;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.mapper.rdbms.DeviceInfoMapper;
import com.pineone.icbms.sda.sch.comm.SchedulerJobComm;

@Service
public class CollectDeviceInfoFromSSJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private static AtomicInteger ai = new AtomicInteger();

	public void collect(String db_server, int db_port, String db_name, String db_user, String db_pass, String save_path,	JobExecutionContext jec) throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		// 중복방지
		start_time = start_time + "S"+String.format("%010d", ai.getAndIncrement());
		//start_time = start_time + "S"+String.format("%010d", String.valueOf(ai.getAndIncrement()));
		

		String end_time = Utils.dateFormat.format(new Date());
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		log.info("CollectDeviceInfoFromSSJobService(id : " + jec.getJobDetail().getName() + ") start.......................");

		StringBuffer sb = new StringBuffer();
		
		String triple_path_file = "";
		String triple_check_result = "";
		String triple_check_result_file = "";

		// task_group_id, task_id에 대한 schDTO정보
		// schDTO = getSchDTO(jec);

		StringBuffer sql = new StringBuffer();
		
		sql.append(" select DISTINCT concat('<http://www.iotoasis.org',a.uri, '> rdf:type ', '<http://www.onem2m.org/ontology/Base_Ontology#Device> .' ) as col from device.Device a, device.DeviceFunction b ");
		sql.append(" where a.name = b.device_name  UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- device label ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select DISTINCT concat('<http://www.iotoasis.org',a.uri, '> rdfs:label \"', label, '\"^^xsd:string .' ) as col  from device.Device a, device.DeviceFunction b ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" where a.name = b.device_name UNION ALL ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" -- device name ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select DISTINCT concat('<http://www.iotoasis.org',a.uri, '> o:name \"', a.name, '\"^^xsd:string .' )  as col from device.Device a, device.DeviceFunction b ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" where a.name = b.device_name UNION ALL "); 
		sql.append(Utils.NEW_LINE);		
		sql.append(" -- device creator ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select DISTINCT concat('<http://www.iotoasis.org',a.uri, '> dc:creator ',  '<http://www.iotoasis.org/ontology/grib> .' )  as col from device.Device a, device.DeviceFunction b ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" where a.name = b.device_name and creator = '그립' UNION ALL ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" -- device - type ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select distinct concat('<http://www.iotoasis.org',a.uri,'> o:hasDeviceType <',c.uri,'> .') as col from device.Device a, device.DeviceFunction b, DeviceType c ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" where a.name = b.device_name and a.DeviceType_id = c.id UNION ALL ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" -- device type ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select concat('<',uri, '> rdf:type <http://www.iotoasis.org/ontology/DeviceType> .') as col from device.DeviceType UNION ALL ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" -- device type label ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select DISTINCT concat('<',uri, '> rdfs:label \"', name, '\"^^xsd:string .' )  from device.DeviceType  UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- device type comment ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select DISTINCT concat('<',uri, '> rdfs:comment \"', comment, '\"^^xsd:string .' ) as col  from device.DeviceType  where comment is not null  UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- device function uri ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',devicefunctionUri, '> rdf:type <', functionalityClassUri, '> .') as col from device.DeviceFunction UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- device aspect uri ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',devicefunctionUri, '> b:refersTo <', aspecturi, '> .')  as col from device.DeviceFunction UNION all ");		
		sql.append(Utils.NEW_LINE);
		sql.append(" -- device - deviceFuncionaltiy ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select distinct  concat('<http://www.iotoasis.org',b.uri,'> b:hasFunctionality <', a.devicefunctionUri,'> .') as col from device.DeviceFunction a, device.Device b where b.name = a.device_name UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- deviceFunctionality - command ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select concat('<', devicefunctionUri , '> b:hasCommand <' , functionResourceUri, '> .') as col from device.DeviceFunction UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- command uri ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select concat('<',functionResourceUri , '> rdf:type <http://www.onem2m.org/ontology/Base_Ontology#Command> .' ) as col from device.DeviceFunction union all ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',5),'> rdf:type <http://www.iotoasis.org/ontology/CSE> .' )  as col from device.DeviceFunction UNION ALL ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- onem2m cse ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',5),'> rdf:type <http://www.iotoasis.org/ontology/CSE> .' )  as col from device.DeviceFunction ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" union all ");
		sql.append(Utils.NEW_LINE);		
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',5),'> o:hasResource <' ,substring_index(functionResourceUri,'/',6),'> .' ) as col from device.DeviceFunction union all ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- onem2m ae ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',6), '> rdf:type <http://www.iotoasis.org/ontology/ApplicationEntity> .' )  as col from device.DeviceFunction union all ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',6),'> o:hasResource <' ,substring_index(functionResourceUri,'/',7),'> .' ) as col from device.DeviceFunction union all ");
		sql.append(Utils.NEW_LINE);
		sql.append(" -- onem2m container1 ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',7), '> rdf:type <http://www.iotoasis.org/ontology/Container> .' )  as col from device.DeviceFunction union all ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',7),'> o:hasResource <' ,substring_index(functionResourceUri,'/',8),'> .' ) as col from device.DeviceFunction union all ");
		sql.append(Utils.NEW_LINE);
		sql.append(" select distinct concat('<',substring_index(functionResourceUri, '/',8), '> rdf:type <http://www.iotoasis.org/ontology/Container> .' )  as col from device.DeviceFunction ");
		
		log.debug("sql ==>\n"+sql.toString());
		
		List<java.lang.CharSequence> list = new ArrayList<java.lang.CharSequence>();
		
		int cnt = 0;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mariadb://" + db_server + ":" + db_port + "/" + db_name, db_user,  db_pass);

			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				String col = rs.getString("col");

				// stringbuffer에 저장, 문제있는 row는 skip하고 정상적인것은 triple로 변환함
				try {
					sb.append(new DeviceInfoMapper(col).from());
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
		AvroRdbmsDeviceInfoPublish avroRdbmsDeviceInfoPublish = new AvroRdbmsDeviceInfoPublish(Utils.BROKER_LIST);
		
		com.pineone.icbms.sda.comm.kafka.avro.COL_RDBMS rDBMS = new com.pineone.icbms.sda.comm.kafka.avro.COL_RDBMS();
		
		rDBMS.setColFrom(Utils.COL_SS_DEVICEINFO_DATA);
		rDBMS.setReadFromTime(String.format("%s", new Date().getTime()));
		rDBMS.setWriteQueueTime(String.format("%s", new Date().getTime()));
		rDBMS.setTaskGroupId(jec.getJobDetail().getGroup());
		rDBMS.setTaskId(jec.getJobDetail().getName());
		rDBMS.setStartTime(start_time);
		rDBMS.setData(list);
		
		// 전송시작
		log.debug("Sending Rdbms(DeviceInfo) start ......................");		
		avroRdbmsDeviceInfoPublish.send(rDBMS);

		// 전송끝 
		avroRdbmsDeviceInfoPublish.close();
		log.debug("Sending Rdbms(DeviceInfo) end ......................");		

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

		log.info("CollectDeviceInfoFromSSJobService(id : " + jec.getJobDetail().getName() + ") end.......................");

	}

	public void execute(JobExecutionContext arg0)  throws JobExecutionException{
		String db_server, db_name, db_user, db_pass, save_path;
		int db_port;

		db_server = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.server");
		db_port = Integer.parseInt(Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.port"));
		db_name = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.name.device");
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
