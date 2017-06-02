package com.pineone.icbms.sda.kafka.onem2m;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.pineone.icbms.sda.comm.SchComm;
import com.pineone.icbms.sda.comm.kafka.avro.COL_ONEM2M;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.sf.TripleService;


public class AvroOneM2MDataSparkSubscribe implements Serializable {
	private static final long serialVersionUID = 1333478786266564011L;
	private final String TOPIC = Utils.KafkaTopics.COL_ONEM2M.toString();
	private static final Log log = LogFactory.getLog(AvroOneM2MDataSparkSubscribe.class);
	
	private final TripleService tripleService = new TripleService();	
	private final int NUM_THREADS = Integer.parseInt(Utils.getSdaProperty ("com.pineone.icbms.sda.kafka.thread.count"));
		
	private final String user_id =this.getClass().getName();
	private final String group_id = this.getClass().getSimpleName();
	
	public static void main(String[] args) {
		AvroOneM2MDataSparkSubscribe avroOneM2MDataSparkSubscribe = new AvroOneM2MDataSparkSubscribe();
		try {
			avroOneM2MDataSparkSubscribe.collect();
		} catch (Exception ex) {
			log.debug("exception in main() :"+ex.getStackTrace());
		}
	}

	public void collect() throws Exception{
		SparkConf sc=new SparkConf().setAppName("AvroOneM2MDataSparkSubscribe")
				 .set("spark.ui.port", "4042")
				 .set("spark.blockManager.port", "38020")
				 .set("spark.broadcast.port", "38021")
				 .set("spark.driver.port", "38022")
				 .set("spark.executor.port", "38023")
				 .set("spark.fileserver.port", "38024")
				 .set("spark.replClassServer.port", "38025")
				 .set("spark.driver.memory", "4g")
				 .set("spark.executor.memory", "4g")
				 ;
		JavaStreamingContext jssc = new JavaStreamingContext(sc, Durations.seconds(10));

		Map<String, String> conf = new HashMap<String, String>();
				//class name을 user_id, grup_id로 사용함
				conf.put("zookeeper.connect",Utils.ZOOKEEPER_LIST);
				conf.put("group.id",group_id);
				conf.put("zookeeper.session.timeout.ms", "6000");
				conf.put("zookeeper.sync.time.ms", "2000");
				conf.put("auto.commit.enable", "true");
				conf.put("auto.commit.interval.ms", "5000");
				conf.put("fetch.message.max.bytes", "31457280");		// 30MB		
				conf.put("auto.offset.reset", "smallest");
		
		jssc.checkpoint("/tmp");
		Map<String, Integer> topic = new HashMap<String, Integer>();
		topic.put(TOPIC, NUM_THREADS);
		
		log.debug("NUM_THREADS : "+NUM_THREADS);

		try {
		    JavaPairReceiverInputDStream<byte[], byte[]> kafkaStream = KafkaUtils.createStream(jssc,byte[].class, byte[].class, kafka.serializer.DefaultDecoder.class, kafka.serializer.DefaultDecoder.class, conf, topic, StorageLevel.MEMORY_ONLY());
		    JavaDStream<byte[]> lines = kafkaStream.map(tuple2 -> tuple2._2());
		    
			  Function <byte[], String> wrkF =
					  new Function<byte[], String> (){
						private static final long serialVersionUID = 4509609657912968079L;

						public String call(byte[] x) {
							BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(x, null);
							SpecificDatumReader<COL_ONEM2M> specificDatumReader = new SpecificDatumReader<COL_ONEM2M>(COL_ONEM2M.class);
							try {
								COL_ONEM2M read = specificDatumReader.read(null, binaryDecoder);								
								new ConsumerT(read).go();
							} catch (Exception e) {
								log.debug("xxx=>"+e.getMessage());
							}
							return "";							
					  	}
					  };
					  
			JavaDStream<String> rst = lines.map(wrkF);
			
			// action을 위해서...
			rst.print(1);
			
			jssc.start();
			jssc.awaitTermination();
		} catch (Exception e) {
			e.printStackTrace();
		  log.debug("exception : "+e.getMessage());
		}
	}
	
	//public class ConsumerT implements Runnable {
	public class ConsumerT implements Serializable {
		private static final long serialVersionUID = 7697840079748720000L;
		private COL_ONEM2M read;
		
		public ConsumerT(COL_ONEM2M read) {
			super();
			this.read = read;
		}
		
		public void go(){
			StringBuffer sb = new StringBuffer();
				
			String task_group_id = "";
			String task_id =  "";
			String start_time =  "";
			String colFrom =  "";
			String calcuate_latest_yn =  "";
			
			try {
				 List<java.lang.CharSequence> data= read.getData();
				 log.debug("count data from kafka broker stream in AvroOneM2MDataSparkSubscribe: "+data.size());
				 
				 task_group_id = read.getTaskGroupId().toString();
				 task_id = read.getTaskId().toString();
				 start_time = read.getStartTime().toString();
				 colFrom = read.getColFrom().toString();
				 calcuate_latest_yn = read.getCalcuateLatestYn().toString();
					
				 String eachTriple = "";
				 int error_count = 0;
				 
				 // 작업 진행여부 판단
				 boolean processing_ok = false;
				 if(colFrom.equals(Utils.COL_SI_DATA)) {
					 processing_ok = true;
				 } 
	
				 if(processing_ok) {
					 for(int i = 0; i < data.size(); i++) {
						 
						try { 
							//log.debug("raw data : "+data.get(i).toString());
							
							eachTriple = tripleService.getTriple(data.get(i).toString());
						} catch (Exception e) {
							error_count++;
							log.debug("malformed data exception : "+e.getMessage());
							log.debug("malformed data : "+data.get(i).toString());
						}
						
						if(calcuate_latest_yn.equals("Y")) {
							//log.debug("calcuate_latest_yn is Y =gooper==>"+eachTriple);
							try {
								tripleService.addLatestContentInstance();
							} catch (Exception e) { 
								log.debug("tripleService.addLatestContentInstance() exception : "+e.getMessage());
							}
						} else {
							//log.debug("calcuate_latest_yn is N =gooper==>"+eachTriple);
						}
						sb.append(eachTriple);
					 }
				 }
				 
				 // task_group_id, task_id, start_time을 key로 work_result값을 update해줌
				 String finish_time = Utils.dateFormat.format(new Date());
				 String work_result  ="";
				 String triple_check_result_file = "";
				 String triple_path_file = "";
				 String triple_check_result = "";
				 String riot_mode = "";
				 
				 // triple data 체크및 전송정보
				 Map<String, String> hm = null;
				 if(processing_ok && data.size() > 0) {
					 try {
						 hm = sendTriples(sb, start_time);
					 } catch (Exception  e) {
						 log.debug("sendTriples(sb, start_time) exception : "+e.getMessage());
					 }
					 triple_check_result_file = hm.get("triple_check_result_file").equals("") ? Utils.None : hm.get("triple_check_result_file");
					 triple_path_file = hm.get("triple_path_file").equals("") ? Utils.None : hm.get("triple_path_file");
					 triple_check_result = hm.get("triple_check_result");
					 riot_mode = hm.get("riot_mode");
					 
					 if(riot_mode.equals("--skip")) {
						 triple_check_result = Utils.None;
					 } else {
						 if(triple_check_result.length() == 0) {
							 triple_check_result = Utils.Valid;
						 }
					 }
				 } else {
					 triple_path_file = Utils.None;
					 triple_check_result = Utils.None;
					 log.debug("data.size() is 0, does not send triple ......");
				 }
	
				 work_result =  "message " + data.size() + " counts from kafka broker have done with error data of "+error_count+" ! ";
				 
				 SchComm schComm = new SchComm();					 
				 schComm.updateFinishTime(task_group_id, task_id, start_time, finish_time, work_result, triple_path_file, triple_check_result);
				 
				 // clear
				 schComm = null;
				 sb = null;
				 data = null;
			} catch (Exception e) {
				log.debug("consumer("+this.getClass().getName()+") exception :"+e.getMessage());
				e.printStackTrace();
				try {
					SchComm schComm2 = new SchComm();
					schComm2.updateFinishTime(task_group_id, task_id, start_time, Utils.dateFormat.format(new Date()), "#2"+e.getMessage());
				} catch (Exception ex) {
					log.debug("ex ==>"+ex.getMessage());
				}
			} // try
		} // go method
	} // ConsumerT class

	private Map<String, String> sendTriples(StringBuffer sb, String start_time) throws Exception {
		String riot_mode = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");
		String triple_path_file = "";
		String triple_check_result = "";
		String work_time = Utils.dateFormat.format(new Date());
		String save_path  = Utils.getSdaProperty("com.pineone.icbms.sda.triple.save_path");
		
		// 폴더가 없으면 생성
		save_path = Utils.makeSavePath(save_path);

		// 스트링 버퍼에 있는 값을 파일에 기록한다.
		String file_name = group_id + "_TT" + start_time+"_WRK" + work_time;		
		String triple_check_result_file = "";
		
		// 결과값이 있을때만 triple파일을 만듬
		if( sb.length() > 0) {
			triple_path_file = save_path + "/"+ file_name + ".nt";			
			tripleService.makeTripleFile(triple_path_file, sb);
			
			// triple파일 체크
			if( ! riot_mode.equals("--skip")) {
				String[] check_result = tripleService.checkTripleFile(triple_path_file, file_name);
				
				// 점검결과를 파일로 저장한다.(체크결과 오류가 있는 경우만 파일로 만듬)
				if(! check_result[1].trim().equals("")) {
					triple_check_result_file = file_name+".bad";
					tripleService.makeResultFile(triple_check_result_file, check_result);
					
					// triple파일 체크결과값
					if(check_result[1].length() > 0) {
						triple_check_result = check_result[1]+ Utils.NEW_LINE+" with file '"+triple_check_result_file+"'";
					} else {
						triple_check_result = Utils.Valid;
					}
				}
			}
			// 파일 전송
			log.info("Sending triples in "+user_id+" start.......................");
			tripleService.sendTripleFileToDW(triple_path_file);
			log.info("Sending triples in "+user_id+" end.......................");
		}

		Map<String, String> hm = new HashMap<String, String>();
		hm.put("triple_check_result_file", triple_check_result_file);
		hm.put("triple_path_file", triple_path_file);
		hm.put("triple_check_result", triple_check_result);
		hm.put("riot_mode", riot_mode);
		
		return hm;
	}
}
