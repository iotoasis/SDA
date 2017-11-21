package com.pineone.icbms.sda.comm.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.sf.MariaDbOfGribQueryImpl;
import com.pineone.icbms.sda.sf.QueryService;
import com.pineone.icbms.sda.sf.QueryServiceFactory;
import com.pineone.icbms.sda.sf.SparqlFusekiQueryImpl;

public class Test {
	private static final Log log = LogFactory.getLog(Test.class);
	
	public void updateTest() {
		// Add a new book to the collection
//		String service = "http://219.248.137.7:13030/icbms/update";
//		UpdateRequest ur = UpdateFactory
//				.create(""
//						+ "delete  { <http://www.iotoasis.org/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue>  ?value . } "
//						+ "insert  { <http://www.iotoasis.org/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue>  \"19\" . } " 
//						+ "WHERE   { <http://www.iotoasis.org/TemperatureObservationValue_LB0001> <http://data.nasa.gov/qudt/owl/qudt#hasNumericValue> ?value . }");
//		UpdateProcessor up = UpdateExecutionFactory.createRemote(ur, service);
//		up.execute();

		// Query the collection, dump output
//		String service1 = "http://219.248.137.7:13030/icbms/";
//		QueryExecution qe = QueryExecutionFactory.sparqlService(service1,
//				"SELECT * WHERE {<http://www.iotoasis.org/Student_S00001> ?r ?y} limit 20 ");
//
//		ResultSet results = qe.execSelect();
//		ResultSetFormatter.out(System.out, results); 
//		qe.close();
		Model model = ModelFactory.createDefaultModel();
		Statement s = model.createStatement(model.createResource("ex:e1"),model.createProperty("ex:p1"), ResourceFactory.createTypedLiteral(new String("0.6")));
		model.add(s);
		String query = "select * {?s ?p ?o }";
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		ResultSetFormatter.out(System.out, results);
		qe.close();
		
		// gooper
		if(! model.isClosed()) {
			model.close();
		}
		if(model != null) {
			model = null;
		}

	}
	
	public static class Item {
		private String name;
		private int qty;
		private BigDecimal price;
		public Item(String name, int qty, BigDecimal price) {
			super();
			this.name = name;
			this.qty = qty;
			this.price = price;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getQty() {
			return qty;
		}
		public void setQty(int qty) {
			this.qty = qty;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
	}
	
	
	public static void main(String[] args) {
		
		Test t = new Test();
//		t.insertTest();
		t.updateTest();
		
		List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7,8);
		List<Integer> towEvenSquares = numbers.parallelStream()
				.filter(n -> {System.out.println("filtering "+n);  return n % 2  == 0;})
				.map(n -> {System.out.println("mappng " + n); return n*n;})
				.limit(20)
				.collect(Collectors.toList());

		//------------------------------------------
		List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
		
		List<Item> items2 = Arrays.asList(
				new Item("apple", 10, new BigDecimal("9.99")),
				new Item("banana", 20, new BigDecimal("19.99")),
				new Item("orange", 10, new BigDecimal("29.99")),
				new Item("watermelon", 10, new BigDecimal("29.99")),
				new Item("papaya", 20, new BigDecimal("9.99")),
				new Item("apple", 10, new BigDecimal("9.99")),
				new Item("banana", 10, new BigDecimal("19.99")),
				new Item("apple", 20, new BigDecimal("9.99"))
				);

		
		Map<String, Long> result = items.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		System.out.println(result);
			
		Map<String, Long> counting = items2.stream().filter(item -> item.getName().equals("apple")).collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
		//Map<String, Long> counting = items2.stream().collect(Collectors.groupingBy( (Item i) ->i.getName(), Collectors.counting()));
		
		System.out.println(counting);
		
		Map<String, Integer> sum = items2.stream().collect(Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));
		
		
		
	
		
		
		
		String[] myArray = {"this", "is", "a", "sentence"};
		Arrays.stream(myArray).forEach(  (x) -> System.out.println(x) );
		
		//String result2 = Arrays.stream(myArray).forEach( (i) -> System.out.println(i); ).reduce("=", (a,b) -> a + b);
		//System.out.println("result2 : "+result2);
		
		
		
//		for(int i = 0; i < 1000000; i ++) {
//			if((i % 5000) == 0) {
//				System.out.println("com.pineone.icbms.sda.triple.regist.bin["+i+"] =>"+Utils.getSdaProperty("com.pineone.icbms.sda.triple.regist.bin"));
//			}
//		}
		
		UUID uid = UUID.randomUUID();
		System.out.println(uid.toString());
		
		System.out.println("case1=>"+String.format("%01x", "PARK".hashCode()));
		System.out.println("case2=>"+String.format("%01x", "PARKSANG".hashCode()));
		System.out.println("case3=>"+String.format("%01x", "PARK".hashCode()));
		System.out.println("case4=>"+String.format("%01x", "PARKSANGPARKSANGPARKSANGPARKSANGPARKSANGPARKSANGPARKSANGPARKSANGPARKSANGPARKSANGPARKSANG".hashCode()));
		System.out.println("case1=>"+String.format("%s", StringUtils.leftPad("PARK",10,'X')));
		
		//SparqlService sfService = new SparqlService();
		//QueryService sfService= new QueryService(new SparqlFusekiQueryImpl());
		QueryService sfService;
		try {
			sfService = QueryServiceFactory.create(Utils.QUERY_GUBUN.FUSEKISPARQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<String> queryList = new ArrayList<String>();

		// sparql
		//queryList.add(" select * where {<http://www.iotoasis.org/herit-in/herit-cse/ONSB_Impact01_001> ?p ?o} " +Utils.SPLIT_STR+Utils.QUERY_GUBUN.SPARQL.toString());
		queryList.add(" select * where {<http://www.iotoasis.org/herit-in/herit-cse/ONSB_Impact01_001> ?p ?o} ");

		
		// mariadb
/*		queryList.add(" select a.cnt, b.menu_id, b.menu_name from " 
				+ " (select menu_id, sum(menu_count) cnt " 
				+ " from tb_cafeteria_vote_history "
				+ " where vote_date < date_sub(sysdate(), INTERVAL 7 DAY  ) "
			    + " group by menu_id )  a, tb_cafeteria_menu_main b "
			    + " where a.menu_id = b.menu_id "
			    + " order by cnt desc "
			    + " limit 10 " +Utils.SPLIT_STR+Utils.QUERY_GUBUN.MARIADB.toString());
*/		
		//queryList.add("select \"p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" as p, \"o=http://www.loa-cnr.it/ontologies/DUL.owl#TypeCollection\" as o" +Utils.SPLIT_STR+Utils.QUERY_GUBUN.MARIADBOFSDA.toString());
		queryList.add("select \"p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" as p, \"o=http://www.loa-cnr.it/ontologies/DUL.owl#TypeCollection\" as o");
		String[] idxVals = new String[]{""};
		
		List<Map<String, String>> query_result;
		
		
	
		//public List<Map<String, String>> runSparqlUniqueResultByVariableColumn(List<String> sparQlList, String[] idxVals) throws Exception {
		
		Test test = new Test();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
		
		try {
			System.out.println("start...........");
			list = test.getResult("", new String[]{""});
			
			//list2 = getResult2("com.pineone.icbms.sda.sf.mongo.PopularMenu", new String[]{""});
			
			System.out.println("end...........");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ex:"+e.getMessage());
		}
		
		System.out.println("list start...........");
		for(int m = 0; m < list.size(); m++) {
			System.out.println(list.get(m));
		}
		System.out.println("list end...........");
		
		System.out.println("list2 start...........");
		for(int m = 0; m < list2.size(); m++) {
			System.out.println(list2.get(m));
		}
		System.out.println("list2 end...........");

	}
	
	public final List<Map<String, String>> getResult (String query, String[] idxVals) throws Exception {
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
		
		DBCursor cursor = null;
		String startDate = "20161206T195419";
		String endDate = "20161206T195519";
		
		String qry_str = "{ty:4, _uri:/PressureCount01/}";
		
		Map<String, String> qry = new HashMap<String, String>();
		qry.put("ty",  "4");
		qry.put("_uri",  "/PressureCount01/");
		
		
		String test_query = "{ "+
									 " '$match' : { "+
									 "	'$and' : [ {'_uri': {'$regex' : 'TicketCount/status/CONTENT_INST'}, {'ct' : { $gte : '20161213T160000' } } ] "+
                					 " } "+ 
    								 " } "+
									 "  , "+
									 "  { "+   
									 "  ' $group' : {'_id' :  '$cr', sum: { $sum: 'con'} } "+
									 "} "; 
	    
	    
		//BasicDBObject searchQuery = new BasicDBObject("ct", new BasicDBObject("$gte", startDate).append("$lt", endDate));
		//BasicDBObject searchQuery =BasicDBObject.parse(qry_str);

		
		//DBObject searchQuery = (DBObject)JSON.parse("{\"ty\":\"4\",\"_uri\":\"PressureCount01\"}");
		//DBObject searchQuery = (DBObject)JSON.parse("{'ri':'ONTENT_INST_417839', 'ty':4}");
		//DBObject searchQuery = (DBObject)JSON.parse("{'ty':4, '_uri': {'$regex': 'PressureCount01'}}");
 
	  String aa =  "{ 'aggregate': 'articles', "+
	   " pipeline: [ "+
	   "   { $project: { tags: 1 } }, "+
	   "   { $unwind: '$tags' }, "+
	   "   { $group: { _id: '$tags', count: { $sum : 1 } } }"+
	   " ],"+
	   " cursor: { }" +
	   "} ";

		//DBObject searchQuery = (DBObject)JSON.parse(aa);
		
		
		
		// 형변환
/*		db.resource.find (
			    {"_uri": /TicketCount\/status\/CONTENT_INST/, "ct": /20161213/}
			)
			    .forEach(function(x) {
			    x.con = new NumberInt(x.con);  
			      db.resource.save(x)  })
*/		
	   String working_uri = "TicketCount/status/CONTENT_INST";
	   int working_ty = 4;
	   
	   DBObject searchQuery = new BasicDBObject();  //"$match", new BasicDBObject("ct", new BasicDBObject("$gte", "20161213T160000")));
	   searchQuery.put("ty",working_ty);
	   searchQuery.put("_uri", new BasicDBObject("$regex", working_uri));
	   //searchQuery.put("ct", new BasicDBObject("$regex", "20161213"));
	   //searchQuery.put("ct", new BasicDBObject("$regex", "20161221"));
	   searchQuery.put("ct", new BasicDBObject("$regex", Utils.sysdateFormat.format(new Date())));
	   System.out.println("-->"+Utils.sysdateFormat.format(new Date()));
		
		DBCursor cursor2 = table.find(searchQuery);
		while (cursor2.hasNext()) {
			//System.out.println("oldObj.toMap()==>"+cursor2.next());
			
			DBObject oldObj = cursor2.next();
			
			@SuppressWarnings("unchecked")
			Map<String, String> map = makeStringMap(oldObj.toMap());
			//map.put("_id", new ObjectId(map.get("_id")));
			
			System.out.println("oldObj.toMap()==>"+oldObj.toMap());
			
			ObjectId id = new ObjectId(map.get("_id"));
			BasicDBObject newObj = new BasicDBObject(map);
			newObj.append("_id", id);
			
			//map.put("con", map.get("con")+1);
			newObj.append("con", Integer.parseInt(map.get("con")));
			newObj.append("ty", Integer.parseInt(map.get("ty")));
			newObj.append("st", Integer.parseInt(map.get("st")));
			newObj.append("cs", Integer.parseInt(map.get("cs")));
			table.update(oldObj, newObj);
		}
		
		// update결과 확인
		DBCursor cursor3 = table.find(searchQuery);
		while (cursor3.hasNext()) {
			System.out.println("after casting ==>"+cursor3.next());
		}
		
		//Forming Unwind parts
		//DBObject unwind = new BasicDBObject("$unwind","$dp.fin.record");

		//Forming Match parts
		DBObject match = new BasicDBObject();  //"$match", new BasicDBObject("ct", new BasicDBObject("$gte", "20161213T160000")));
		match.put("ty",working_ty);
		match.put("_uri", new BasicDBObject("$regex", working_uri));
		//match.put("ct", new BasicDBObject("$gte", "20161221T160000"));
		long nowDate = new Date().getTime();
		long newDate = nowDate-(5*60*1000);
		//long newDate = nowDate-(10*24*60*60*1000);
		
		System.out.println("Utils.dateFormat.format((new Date(newDate)))) ==>" + Utils.dateFormat.format((new Date(newDate))));
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
		      
		// 참조 : http://stackoverflow.com/questions/31066829/mongodb-converting-string-field-into-int-using-java


		/**
		 * Executing aggregation 
		 */
		AggregationOutput output = db.getCollection("resource").aggregate(
						new BasicDBObject("$match", match), 
						new BasicDBObject("$group", group),
						new BasicDBObject("$project", project)
						);

		System.out.println("output : "+output.getCommandResult().getString("result"));
		Iterator<DBObject> itr = output.results().iterator();
		
		while(itr.hasNext()) {
			DBObject dbObject =itr.next();
			@SuppressWarnings("unchecked")
			Map<String, String> newMap = makeStringMap(dbObject.toMap());
			list.add(newMap);
        }	
		
		///////////////////
		

		try {

			return list;
		} catch (Exception e) {
			log.debug("Exception : "+e.getMessage());
			throw e;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
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
	
	
	public static List<Map<String, String>> getResult2 (String query, String[] idxVals) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			log.debug("class ===>"+query);
			
			Class<?> workClass = Class.forName(query);
			Object  newObj = workClass.newInstance();
			Method m = workClass.getDeclaredMethod("runMongoQueryByClass");
			list = (List<Map<String, String>>) m.invoke(newObj);
		
			log.debug("workClass==>"+workClass.getName());
		} catch (ClassNotFoundException e) {
			log.debug(e.getMessage());
		} catch (InstantiationException e) {
			log.debug(e.getMessage());
		} catch (IllegalAccessException e) {
			log.debug(e.getMessage());
		}		
		//return null;		
		return list;
}

}