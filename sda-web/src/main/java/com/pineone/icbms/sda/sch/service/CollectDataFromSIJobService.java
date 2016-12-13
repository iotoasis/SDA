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
import com.pineone.icbms.sda.comm.kafka.onem2m.AvroOneM2MDataPublish;
import com.pineone.icbms.sda.comm.sch.dto.SchDTO;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.comm.SchedulerJobComm;

@Service
public class CollectDataFromSIJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private MongoClient mongoClient;
	private DB db = null;
	private SchDTO schDTO;
	private static AtomicInteger ai = new AtomicInteger();
	
	// latestContentInstance값을 구해야 할지 여부
	private static boolean haveToMakeLatestContentInstance = false;
	
	// latestContentInstance 산출하기 변경 기준 날짜(이 시간 전까지는 산출하지 않음)
	private static String splitDate;
	
	// 전부 수집하도록 수정
	//private final String m = "status/Data";
	
	// _uri에 /status/만 수집하도록 다시 수정
	private final String m = "/status/";
	private final int maxLimit = Integer.parseInt(Utils.getSdaProperty("com.pineone.icbms.sda.mongodb.read_limit"));
	
	public void collect(String ip, int port, String dbname, String save_path, JobExecutionContext jec) throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		// 중복방지
		start_time = start_time + "S"+String.format("%010d", ai.getAndIncrement());
		//start_time = start_time + "S"+String.format("%010d", String.valueOf(ai.getAndIncrement()));
		
		log.info("CollectDataFromSIJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
		String startDate = "";
		String endDate = "";
		String triple_path_file = "";
		String triple_check_result = "";
		String triple_check_result_file = "";
		int cnt = 0;
		DBCollection table=null;
		
		// MongoDB연결
		try {
			mongoClient = new MongoClient(new ServerAddress(ip, port));
			db = mongoClient.getDB(dbname);
			table = db.getCollection("resource");
		} catch (Exception ex) {
			log.debug("MongoDB connection error : "+ex.getMessage());
			if(db != null) {
				db.cleanCursors(true);
				db = null;				
			}
			if(table != null) {table = null;}
			if(mongoClient != null ) {
				mongoClient.close();
			}
			throw ex;
		} 
		
		// task_group_id, task_id에 대한 schDTO정보
		schDTO = getSchDTO(jec);

		// startdate구하기
		if (schDTO.getLast_work_time() == null || schDTO.getLast_work_time().equals("")) {
			startDate = "";
			haveToMakeLatestContentInstance = false;
			// latestContentInstance를 구하지 않는 기준 일자를 구함(예, 현재시간 - adjust.ms초 전까지는 구하지 않음)
			long adjustMs = Long.parseLong(Utils.getSdaProperty("com.pineone.icbms.sda.init.adjust.ms"));
			
			splitDate = Utils.dateFormat.format(new Date().getTime() - adjustMs);
			
			log.debug("schDTO.getLast_work_time() is null or \"\" ================================");
		} else {
			startDate = schDTO.getLast_work_time();
		}
		
		
		//splitDate가 없을때(war가 재기동 되는 경우) splitDate설정
		if(splitDate == null || splitDate.equals("")) {
			log.debug("splitDate is null or \"\" ================================");
			long adjustMs = Long.parseLong(Utils.getSdaProperty("com.pineone.icbms.sda.init.adjust.ms"));
			long lastWorkTime_tmp = Utils.dateFormat.parse(schDTO.getLast_work_time()).getTime();
			
			splitDate = Utils.dateFormat.format(lastWorkTime_tmp - adjustMs);
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
			log.debug("Size of cursor ==> "+cursor.size());
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

			while (cursor.hasNext()) {
				DBObject doc = cursor.next();
				endDate = (String) doc.get("ct");
			}
			
			// endDate값을 sch테이블의 last_work_time에 update
			updateLastWorkTime(jec, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			updateFinishTime(jec, start_time, Utils.dateFormat.format(new Date()), e.getMessage());			
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
		
		// 초기화하는 경우 최초에 startDate가 ""이다.
		if(! startDate.equals("")) {
			// latestContentInstance계산여부 판단
			long startDate_tmp = Utils.dateFormat.parse(startDate).getTime();
			long endDate_tmp = Utils.dateFormat.parse(endDate).getTime();
			long splitDate_tmp = Utils.dateFormat.parse(splitDate).getTime();
			
			// endDate가 splitDate를 넘어가는 경우는 최근인스턴스를 구함
			if(splitDate_tmp <= startDate_tmp) {
				haveToMakeLatestContentInstance = true;
			} else {
				if(endDate_tmp > splitDate_tmp) {
					haveToMakeLatestContentInstance = true;
				} else {
					haveToMakeLatestContentInstance = false;
				}
			}
		}
		
		log.debug("startDate : " + startDate);
		log.debug("endDate : " + endDate);
		log.debug("splitDate : " + splitDate);
		log.debug("haveToMakeLatestContentInstance : " + haveToMakeLatestContentInstance);

		// triple생성 대상 범위의 데이타 가져오기
		BasicDBObject searchQuery2 = new BasicDBObject("ct", new BasicDBObject("$gte", startDate).append("$lt", endDate));
		
		// 전부 수집하도록 수정
		// _uri에 /status/만 수집하도록 다시 수정
		searchQuery2.put("_uri",  java.util.regex.Pattern.compile(m));
		
		DBCursor cursor2 = null;
		int err_cnt = 0;

		List<java.lang.CharSequence> list = new ArrayList<java.lang.CharSequence>();
		try {
			cursor2 = table.find(searchQuery2);
			cursor2.sort(new BasicDBObject("ct", 1)); // 1: 정순, -1 : 역순

			// sch_hist테이블에 data insert
			cnt = cursor2.count();
			insertSchHist(jec, cnt, start_time, Utils.dateFormat.format(new Date()));

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
		} catch (Exception e) {
			e.printStackTrace();
			updateFinishTime(jec, start_time, Utils.dateFormat.format(new Date()), e.getMessage());
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
		AvroOneM2MDataPublish avroOneM2MDataPublish = new AvroOneM2MDataPublish(Utils.BROKER_LIST);
		
		com.pineone.icbms.sda.comm.kafka.avro.COL_ONEM2M oneM2M = new com.pineone.icbms.sda.comm.kafka.avro.COL_ONEM2M();
		
		oneM2M.setColFrom(Utils.COL_SI_DATA);
		oneM2M.setReadFromTime(String.format("%s", new Date().getTime()));
		oneM2M.setWriteQueueTime(String.format("%s", new Date().getTime()));
		oneM2M.setTaskGroupId(jec.getJobDetail().getGroup());
		oneM2M.setTaskId(jec.getJobDetail().getName());
		oneM2M.setStartTime(start_time);
		if(haveToMakeLatestContentInstance) {
			oneM2M.setCalcuateLatestYn("Y");
		} else if(haveToMakeLatestContentInstance  == false) {
			oneM2M.setCalcuateLatestYn("N");
		}
		oneM2M.setData(list);
		
		// 전송시작
		log.debug("Sending OneM2MData start ......................");		
		avroOneM2MDataPublish.send(oneM2M);
		avroOneM2MDataPublish.close();
		log.debug("Sending OneM2MData end ......................");
		// 전송끝
		
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
		
		log.info("CollectDataFromSIJobService(id : "+jec.getJobDetail().getName()+") end.......................");
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