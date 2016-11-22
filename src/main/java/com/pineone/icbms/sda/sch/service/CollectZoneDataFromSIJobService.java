package com.pineone.icbms.sda.sch.service;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.pineone.icbms.sda.comm.service.SchedulerJobComm;
import com.pineone.icbms.sda.sf.service.TripleService;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sch.dto.SchDTO;

@Service
public class CollectZoneDataFromSIJobService extends SchedulerJobComm implements Job {
	private final Log log = LogFactory.getLog(this.getClass());
	private MongoClient mongoClient;
	private DB db = null; 
	private SchDTO schDTO;
	private final String m = "zone/Data";
	private final int maxLimit = Integer.parseInt(Utils.getSdaProperty("com.pineone.icbms.sda.mongodb.read_limit"));
	
	public void collect(String ip, int port, String dbname, String save_path, JobExecutionContext jec)
			throws Exception {
		String start_time = Utils.dateFormat.format(new Date());
		
		log.info("CollectZoneDataFromSIJobService(id : "+jec.getJobDetail().getName()+") start.......................");
		
		mongoClient = new MongoClient(new ServerAddress(ip, port));
		db = mongoClient.getDB(dbname);
		
		String riot_mode = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");
		StringBuffer sb = new StringBuffer();
		String startDate = "";
		String endDate = "";
		String triple_path_file = "";
		String triple_check_result = "";
		TripleService tripleService = new TripleService();

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
		// endDate값을 sch테이블의 last_work_time에 update
		updateLastWorkTime(jec, endDate);

		log.debug("startDate : " + startDate);
		log.debug("endDate : " + endDate);

		// triple생성 대상 범위의 데이타 가져오기
		BasicDBObject searchQuery2 = new BasicDBObject("ct",
				new BasicDBObject("$gte", startDate).append("$lt", endDate));
		searchQuery2.put("_uri",  java.util.regex.Pattern.compile(m));

		DBCursor cursor2 = null;
		int cnt = 0;
		try {
			cursor2 = table.find(searchQuery2);
			cursor2.sort(new BasicDBObject("ct", 1)); // 1: 정순, -1 : 역순

			// sch_hist테이블에 data insert
			insertSchHist(jec, cursor2.count(), start_time, endDate);

			while (cursor2.hasNext()) {
				DBObject doc = cursor2.next();

				// stringbuffer에 저장, 문제있는 row는 skip하고 정상적인것은 triple로 변환함
				try {
					//test
					//log.debug("value of ["+cnt+"]...."+doc.toString());

					sb.append(tripleService.getTriple(doc));
				} catch (Exception e) {
					log.debug("error data["+cnt+"] ==> "+doc.toString());
					e.printStackTrace();
				}
				cnt++;
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

		// 스트링 버퍼에 있는 값을 파일에 기록한다.
		String file_name = jec.getJobDetail().getName()+"_WRK" + endDate + "_TT" + start_time;
		String triple_check_result_file = "";
		
		// 결과값이 있을때만 triple파일을 만듬
		if( sb.length() > 0) {
			triple_path_file = save_path + "/"+ file_name + ".nt";			
			tripleService.makeTripleFile(triple_path_file, sb);
			
			// triple파일 체크
			if( ! riot_mode.equals("--skip")) {
				String[] check_result = tripleService.checkTripleFile(triple_path_file, file_name);
				
				// 점검결과를 파일로 저장한다.(체크결과 오류가 있는 경우만 파일로 만듬)
				if( ! check_result[1].trim().equals("")) {
					triple_check_result_file = file_name+".bad";
					tripleService.makeResultFile(triple_check_result_file, check_result);
					
					// triple파일 체크결과값
					if(check_result[1].length() > 0) {
						triple_check_result = check_result[1];
					} else {
						triple_check_result = Utils.Valid;
					}
				}
			}
			// 파일 전송
			tripleService.sendTripleFile(triple_path_file);
			log.info("CollectZoneDataFromSIJobService(id : "+jec.getJobDetail().getName()+") end.......................");			
		}

		// sch_hist테이블의 finish_time에 날짜 설정
		String finish_time = Utils.dateFormat.format(new Date());
		if(triple_check_result_file.equals("")) triple_check_result_file = Utils.None;
		updateFinishTime(jec, start_time, finish_time, "triple_check_result_file : " + triple_check_result_file, triple_path_file, triple_check_result);
		
		// mongodb관련 커넥션 닫기
		if(db != null) {
			db.cleanCursors(true);
			table = null;
			db = null;				
		}
		if(mongoClient != null ) {
			mongoClient.close();
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) {
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
		}
	}

}
