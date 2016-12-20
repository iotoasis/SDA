package com.pineone.icbms.sda.sf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.pineone.icbms.sda.comm.util.Utils;
/*
 * MongoDB에 접속하여 쿼리수행
 */
public class MongoDbQueryImpl extends QueryCommon implements QueryItf {

	private final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		log.info("runQuery of mongo start ======================>");

		log.debug("try (first) .................................. ");
		try {
			list = getResult(query, idxVals);
		} catch (Exception e) {
			int waitTime = 5*1000;
			log.debug("Exception message in runQuery() =====> "+e.getMessage());  
			
			try {
				// 일정시간 대기 했다가 다시 수행함
				log.debug("sleeping (first)................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				list = getResult(query, idxVals);
			} catch (Exception ee) {
				log.debug("Exception 1====>"+ee.getMessage());
				if(ee.getMessage().contains("Service Unavailable")|| ee.getMessage().contains("java.net.ConnectException")
						// || ee.getMessage().contains("500 - Server Error") || ee.getMessage().contains("HTTP 500 error")
						) {					
					try {
						// restart fuseki
						Utils.restartFuseki();
					
						// 일정시간을 대기 한다.
						log.debug("sleeping (final)................................. in "+waitTime);
						Thread.sleep(waitTime);
						
						// 마지막으로 다시한번 처리해줌
						log.debug("try (final).................................. ");
						list = getResult(query, idxVals);
					} catch (Exception eee) {
						log.debug("Exception 2====>"+eee.getMessage());
						throw eee;
					}
				}
				throw ee;
			}
		}

		log.info("runQuery of mongo end ======================>");
		return list;
	}
	
	private final List<Map<String, String>> getResult (String query, String[] idxVals) throws Exception {
		String db_server = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.server");
		String db_port = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.port");
		String db_name = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.name");
		String collection_name = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.collection.name"); // resource
		
/*		String db_user = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.user");
		String db_pass = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.pass");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
*/		
		DBCollection table=null;
		MongoClient mongoClient=null;
		DB db = null;

		// 변수치환
		query = makeFinal(query, idxVals);
	
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		// MongoDB연결
		try {
			mongoClient = new MongoClient(new ServerAddress(db_server, Integer.parseInt(db_port)));
			db = mongoClient.getDB(db_name);
			table = db.getCollection(collection_name);
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
		// con값에 대한 형변환(String -> Integer)
		// 형변환
	/*		db.resource.find (
				    {"_uri": /TicketCount\/status\/CONTENT_INST/, "ct": /20161213/}
				)
				    .forEach(function(x) {
				    x.con = new NumberInt(x.con);  
				      db.resource.save(x)  })
	*/		
	   DBObject searchCastQuery = new BasicDBObject();  //"$match", new BasicDBObject("ct", new BasicDBObject("$gte", "20161213T160000")));
	   searchCastQuery.put("_uri", new BasicDBObject("$regex", "TicketCount/status/CONTENT_INST"));
	   searchCastQuery.put("ct", new BasicDBObject("$regex", "20161213"));
	   //searchCastQuery.put("ct", new BasicDBObject("$regex", Utils.sysdateFormat.format(new Date())));
		
		DBCursor cursor = table.find(searchCastQuery);
		while (cursor.hasNext()) {
			DBObject oldObj = cursor.next();
			System.out.println("==>"+oldObj);
			
/*			@SuppressWarnings("unchecked")
			Map<String, String> map = makeStringMap(oldObj.toMap());
			//map.put("_id", new ObjectId(map.get("_id")));
			
			ObjectId id = new ObjectId(map.get("_id"));
			BasicDBObject newObj = new BasicDBObject(map);
			newObj.append("_id", id);
			newObj.append("con", Integer.parseInt(map.get("con")));
			table.update(oldObj, newObj);*/
		}
		
		
		// 집계 수행
		DBObject match = new BasicDBObject();  //"$match", new BasicDBObject("ct", new BasicDBObject("$gte", "20161213T160000")));
		match.put("ty",4);
		match.put("_uri", new BasicDBObject("$regex", "TicketCount/status/CONTENT_INST"));
		//match.put("ct", new BasicDBObject("$gte", "20161213T160000"));
		long nowDate = new Date().getTime();
		long newDate = nowDate-(5*60*1000);
		
		match.put("ct", new BasicDBObject("$gte", Utils.dateFormat.format((new Date(newDate)))));

		//Forming Group parts
		DBObject group = new BasicDBObject();
		group.put("_id", "$cr");
		group.put("sum_con", new BasicDBObject("$sum", "$con"));
		//group.put("sum_con", new BasicDBObject("$sum", 1));

		//Forming Project parts
		DBObject project = new BasicDBObject();
		project.put("cr","$_id");
		project.put("_id",0);
		project.put("sum_con", 1);

		try {
			AggregationOutput output = db.getCollection("resource").aggregate(
						new BasicDBObject("$match", match), 
						new BasicDBObject("$group", group),
						new BasicDBObject("$project", project)
						);

			//System.out.println("output : "+output.getCommandResult().getString("result"));
			Iterator<DBObject> itr = output.results().iterator();
			
			while(itr.hasNext()) {
				DBObject dbObject =itr.next();
				//JSONObject jsonObject = JSONObject.fromObject(dbObject.toString());
				//Map<String, String> newMap = castMap(dbObject.toMap(), String.class, String.class);
				@SuppressWarnings("unchecked")
				Map<String, String> newMap = makeStringMap(dbObject.toMap());
				list.add(newMap);
	        }	
			
			return list;
		} catch (Exception e) {
			log.debug("Exception : "+e.getMessage());
			throw e;
		} finally {
			if(db != null) {
				db.cleanCursors(true);
				table = null;
				db = null;				
			}
			if(mongoClient != null ) {
				mongoClient.close();
			}
		} 
	}
	
	public Map<String,String> makeStringMap(Map<String, String> map) {
		Map<String, String> newMap = new HashMap<String, String>();
		
    	Set<String> entry = map.keySet();
    	Iterator<String> itr = entry.iterator();
    	
    	while(itr.hasNext()) {
    		String key = String.valueOf(itr.next());
    		//System.out.println("key : "+key);
    		String value = String.valueOf(map.get(key));
    		//System.out.println("value : "+value);
    		
    		newMap.put(key, value);
    	}
    	
	    return newMap;
	}		
}
