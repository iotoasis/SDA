package com.pineone.icbms.sda.sf.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.pineone.icbms.sda.comm.util.Utils;

/**
 *   인기메뉴 구하는 클래스
 */
public class PopularMenuImpl implements MongoQueryItf {
	private final Log log = LogFactory.getLog(this.getClass());

    public List<Map<String, String>> runMongoQueryByClass () throws Exception {
	    final String working_uri = "TicketCount/status/CONTENT_INST";
	    final int working_ty = 4;

		final String db_server = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.server");
		final String db_port = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.port");
		final String db_name = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.name");
		final String collection_name = Utils.getSdaProperty("com.pineone.icbms.sda.mongo.db.collection.name"); // resource
		
		DBCollection table=null;
		MongoClient mongoClient=null;
		DB db = null;
	
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
	   DBObject searchCastQuery = new BasicDBObject();
	   searchCastQuery.put("ty",working_ty);
	   searchCastQuery.put("_uri", new BasicDBObject("$regex", working_uri));
	   searchCastQuery.put("ct", new BasicDBObject("$regex", Utils.sysdateFormat.format(new Date())));
		
		try {	   
			DBCursor cursor = table.find(searchCastQuery);
			while (cursor.hasNext()) {
				DBObject oldObj = cursor.next();
				
				@SuppressWarnings("unchecked")
				Map<String, String> map = makeStringMap(oldObj.toMap());
				
				ObjectId id = new ObjectId(map.get("_id"));
				BasicDBObject newObj = new BasicDBObject(map);
				newObj.append("_id", id);
				
				newObj.append("con", Integer.parseInt(map.get("con")));
				newObj.append("ty", Integer.parseInt(map.get("ty")));
				newObj.append("st", Integer.parseInt(map.get("st")));
				newObj.append("cs", Integer.parseInt(map.get("cs")));
				
				String lbl_tmp = map.get("lbl");
				Gson gson = new Gson();
				String[] lbl_json = gson.fromJson(lbl_tmp ,String[].class);
				
				newObj.append("lbl", lbl_json);
				table.update(oldObj, newObj);
			}
			
			// update결과 확인
			DBCursor cursor2 = table.find(searchCastQuery);
			while (cursor2.hasNext()) {
				log.debug("after casting ==>"+cursor2.next());
			}
			if(cursor2 != null) cursor2.close();
			
			// 집계 수행
			DBObject match = new BasicDBObject();  
			match.put("ty",working_ty);
			match.put("_uri", new BasicDBObject("$regex", working_uri));
			long nowDate = new Date().getTime();
			long newDate = nowDate-(5*60*1000);
			
			match.put("ct", new BasicDBObject("$gte", Utils.dateFormat.format((new Date(newDate)))));
	
			//Forming Group parts
			DBObject group = new BasicDBObject();
			group.put("_id", "$cr");
			group.put("sum_con", new BasicDBObject("$sum", "$con"));
	
			//Forming Project parts
			DBObject project = new BasicDBObject();
			project.put("cr","$_id");
			project.put("_id",0);
			project.put("sum_con", 1);

			AggregationOutput output = db.getCollection("resource").aggregate(
						new BasicDBObject("$match", match), 
						new BasicDBObject("$group", group),
						new BasicDBObject("$project", project)
						);

			log.debug("output : "+output.getCommandResult().getString("result"));
			Iterator<DBObject> itr = output.results().iterator();
			
			while(itr.hasNext()) {
				DBObject dbObject =itr.next();
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
			if(table != null) {table = null;}
			if(mongoClient != null ) {
				mongoClient.close();
			}
		} 
	}
	
	private Map<String,String> makeStringMap(Map<String, String> map) {
		Map<String, String> newMap = new HashMap<String, String>();
		
    	Set<String> entry = map.keySet();
    	Iterator<String> itr = entry.iterator();
    	
    	while(itr.hasNext()) {
    		String key = String.valueOf(itr.next());
    		String value = String.valueOf(map.get(key));
    		newMap.put(key, value);
    	}
    	
	    return newMap;
	}
}