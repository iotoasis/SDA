package com.pineone.icbms.sda.sch.service;

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
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.pineone.icbms.sda.comm.kafka.onem2m.AvroOneM2MZoneDataPublish;
import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.comm.SchedulerJobComm;

@Service
public class CollectZoneDataFromSIJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private MongoClient mongoClient;
	private DB db = null; 
	private SchDTO schDTO;
	private static AtomicInteger ai = new AtomicInteger();	
	private final String m = "zone/Data";
	private final int maxLimit = Integer.parseInt(Utils.getSdaProperty("com.pineone.icbms.sda.mongodb.read_limit"));
	
	public void collect(String ip, int port, String dbname, String save_path, JobExecutionContext jec)
			throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		// 중복방지
		start_time = start_time + "S"+String.format("%010d", ai.getAndIncrement());
		
		log.info("CollectZoneDataFromSIJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
		mongoClient = new MongoClient(new ServerAddress(ip, port));
		db = mongoClient.getDB(dbname);
		
		String startDate = "";
		String endDate = "";
		String triple_path_file = "";
		String triple_check_result = "";
		String triple_check_result_file = "";
		int cnt = 0;

		DBCollection table = db.getCollection("resource");
	
		// task_group_id, task_id에 대한 schDTO정보
		schDTO = getSchDTO(jec);

		// startdate구하기
		if (schDTO.getLast_work_time() == null || schDTO.getLast_work_time().equals("")) {
			startDate = "";
		} else {
			startDate = schDTO.getLast_work_time();
		}

		// enddate구하기
		BasicDBObject searchQuery = new BasicDBObject("ct", new BasicDBObject("$gte", startDate));

		DBCursor cursor = null;
		try {
			cursor = table.find(searchQuery).limit(maxLimit);
			/*
			cursor.sort(new BasicDBObject("ct", -1)); // 1: 정순, -1 : 역순
			while (cursor.hasNext()) {
				DBObject doc = cursor.next();
				endDate = (String) doc.get("ct");
				break;
			}
			*/
			
			log.debug("size of cursor ==> "+cursor.size());
			log.debug("maxLimit  ==> "+maxLimit);
			
			cursor.sort(new BasicDBObject("ct", 1)); // 1: 정순, -1 : 역순
			if(cursor.size() > 0) {
				if(cursor.size() > maxLimit) {
					cursor.skip(maxLimit);
				} else {
					cursor.skip(cursor.size()-1);
				}
				while (cursor.hasNext()) {
					DBObject doc = cursor.next();
					endDate = (String) doc.get("ct");
				}
			} else {
				endDate = startDate;
			}
		} catch (MongoException e) {
			e.printStackTrace();
			if(db != null) {
				db.cleanCursors(true);
				table = null;
				db = null;				
			}
			if(mongoClient != null ) {
				mongoClient.close();
			}
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if(db != null) {
				db.cleanCursors(true);
				table = null;
				db = null;				
			}
			if(mongoClient != null ) {
				mongoClient.close();
			}
			throw e;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		log.debug("startDate : " + startDate);
		log.debug("endDate : " + endDate);

		// triple생성 대상 범위의 데이타 가져오기
		BasicDBObject searchQuery2 = new BasicDBObject("ct",
				new BasicDBObject("$gte", startDate).append("$lt", endDate));
		searchQuery2.put("_uri",  java.util.regex.Pattern.compile(m));

		DBCursor cursor2 = null;
		int err_cnt = 0;
		
		List<java.lang.CharSequence> list = new ArrayList<java.lang.CharSequence>();		
		try {
			cursor2 = table.find(searchQuery2);
			cursor2.sort(new BasicDBObject("ct", 1)); // 1: 정순, -1 : 역순

			// sch_hist테이블에 data insert
			cnt = cursor2.count();
			insertSchHist(jec, cursor2.count(), start_time, Utils.dateFormat.format(new Date()));

			while (cursor2.hasNext()) {
				DBObject doc = cursor2.next();

				// stringbuffer에 저장, 문제있는 row는 skip하고 정상적인것은 triple로 변환함
				try {
					list.add(getJson(doc));
				} catch (Exception e) {
					log.debug("Skipped error data["+err_cnt+"] ==> "+doc.toString());
					e.printStackTrace();
				}

				err_cnt++;
			}
		} catch (MongoException e) {
			e.printStackTrace();
			if(db != null) {
				db.cleanCursors(true);
				table = null;
				db = null;				
			}
			if(mongoClient != null ) {
				mongoClient.close();
			}
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			if(db != null) {
				db.cleanCursors(true);
				table = null;
				db = null;				
			}
			if(mongoClient != null ) {
				mongoClient.close();
			}
			throw e;
		} finally {
			if (cursor2 != null) {
				cursor2.close();
			}
		}

		// kafka 전송
		AvroOneM2MZoneDataPublish avroOneM2MZoneDataPublish = new AvroOneM2MZoneDataPublish(Utils.BROKER_LIST);
		
		com.pineone.icbms.sda.comm.kafka.avro.COL_ONEM2M oneM2M = new com.pineone.icbms.sda.comm.kafka.avro.COL_ONEM2M();
		
		oneM2M.setColFrom(Utils.COL_SI_ZONE_DATA);
		oneM2M.setReadFromTime(String.format("%s", new Date().getTime()));
		oneM2M.setWriteQueueTime(String.format("%s", new Date().getTime()));
		oneM2M.setTaskGroupId(jec.getJobDetail().getGroup());
		oneM2M.setTaskId(jec.getJobDetail().getName());
		oneM2M.setStartTime(start_time);
		oneM2M.setData(list);
		
		// 전송시작
		log.debug("Sending OneM2MZoneData start ......................");		
		avroOneM2MZoneDataPublish.send(oneM2M);

		// 전송끝 
		avroOneM2MZoneDataPublish.close();
		log.debug("Sending OneM2MZoneData end ......................");		

		// mongodb관련 커넥션 닫기
		if(db != null) {
			db.cleanCursors(true);
			table = null;
			db = null;				
		}
		if(mongoClient != null ) {
			mongoClient.close();
		}
		
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
		updateLastWorkTime(jec, endDate);
		log.info("CollectZoneDataFromSIJobService(id : "+jec.getJobDetail().getName()+") end.......................");		
	}

	public void execute(JobExecutionContext arg0)  throws JobExecutionException{
		String mongodb_server;
		int mongodb_port;
		String mongodb_db;
		String save_path;

		mongodb_server = Utils.getSdaProperty("com.pineone.icbms.sda.mongodb.server");
		mongodb_port = Integer.parseInt(Utils.getSdaProperty("com.pineone.icbms.sda.mongodb.port"));
		mongodb_db = Utils.getSdaProperty("com.pineone.icbms.sda.mongodb.db");
		save_path = Utils.getSdaProperty("com.pineone.icbms.sda.triple.save_path");

		// 폴더가 없으면 생성
		save_path = Utils.makeSavePath(save_path);

		try {
			collect(mongodb_server, mongodb_port, mongodb_db, save_path, arg0);
			//collect("120.0.0.1", mongodb_port, mongodb_db, save_path, arg0);
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
