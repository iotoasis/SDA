package com.pineone.icbms.sda.sf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.update.UpdateExecutionFactory;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MContentInstanceDTO;
import com.pineone.icbms.sda.kb.mapper.onem2m.OneM2MContentInstanceMapper;

public class TripleService {
	private final Log log = LogFactory.getLog(this.getClass());
	private String parentResourceUri;
	private String instanceUri;
	
	public String getParentResourceUri() {
		return parentResourceUri;
	}

	public void setParentResourceUri(String parentResourceUri) {
		this.parentResourceUri = parentResourceUri;
	}

	public String getInstanceUri() {
		return instanceUri;
	}

	public void setInstanceUri(String instanceUri) {
		this.instanceUri = instanceUri;
	}

	// DBObject나 String값을 triple로 변환
	public String getTriple(Object doc) throws Exception {
		Gson gson = new Gson();
		StringWriter sw = new StringWriter();
		String returnStr = "";
		String ri = Utils.NotSupportingType;
		OneM2MContentInstanceDTO contextInstanceDTO = new OneM2MContentInstanceDTO();

		int ty = -1;
	
		if (doc instanceof DBObject) {
			DBObject docT = (DBObject) doc;
			ty = (Integer) docT.get("ty");
		} else if (doc instanceof String) {
			// ty값을 확인하기 위해서 특정 객체에 매핑해봄(ty=4)
			
			contextInstanceDTO = gson.fromJson((String) doc, OneM2MContentInstanceDTO.class);
			ty = Integer.parseInt(contextInstanceDTO.getTy());
		}

		if (ty == 5) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");
			sw.flush();
		} else if (ty == 4) {
			if (doc instanceof DBObject) {
				contextInstanceDTO = gson.fromJson(gson.toJson(doc), OneM2MContentInstanceDTO.class);
				
			} else if (doc instanceof String) {
				contextInstanceDTO = gson.fromJson((String) doc, OneM2MContentInstanceDTO.class);
			}
			//log.debug("=== ri : "+contextInstanceDTO.getRi()+",  ty : "+ty+" ====>include");		
			

			OneM2MContentInstanceMapper mapper = new OneM2MContentInstanceMapper(contextInstanceDTO);
		
			Model model = ModelFactory.createDefaultModel();
			model.add(mapper.from());
			
			this.setParentResourceUri(mapper.getParentResourceUri());
			this.setInstanceUri(mapper.getInstanceUri());
			
			// container별로 최근의 contentinstance를 가지도록 함... 시작 
			//log.debug("delete->insert o:hasLatestContentInstance start....................");
			//addLatestContentInstance(mapper.getParentResourceUri(), mapper.getInstanceUri());
			//log.debug("delete->insert o:hasLatestContentInstance end....................");
			// container별로 최근의 contentinstance를 가지도록 함... 끝
			
			// 스트링 변환부분(test)
		    //RDFDataMgr.write(System.out, model, RDFFormat.NTRIPLES);

			// 스트링 변환부분
			RDFDataMgr.write(sw, model, RDFFormat.NTRIPLES);

			returnStr = sw.toString();
			sw.flush();
			
			// gooper
			if(! model.isClosed()) {
				model.close();
			}
			if(model != null) {
				model = null;
			}
			if(mapper != null) {
				mapper = null;
			}
			if(doc != null) {
				doc = null;
			}
		} else if (ty == 3) {
			log.debug("=== ri: "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();

			/*
			 * OneM2MContainerDTO cont = null; if (doc instanceof DBObject) {
			 * gson.toJson(doc)); cont = gson.fromJson(gson.toJson(doc),
			 * OneM2MContainerDTO.class); } else if (doc instanceof String) {
			 * (doc)); cont = gson.fromJson((String) doc,
			 * OneM2MContainerDTO.class); } OneM2MContainerMapper mapper = new
			 * OneM2MContainerMapper(cont); Model model =
			 * ModelFactory.createDefaultModel(); model.add(mapper.from());
			 * 
			 * // 스트링 변환부분 RDFDataMgr.write(sw, model, RDFFormat.NTRIPLES);
			 * 
			 * log.debug("value from mongo ====3(json)====>" +
			 * cont.toString()); log.debug(
			 * "value from mongo ====3(sw)====>" + sw.toString());
			 * 
			 * returnStr = sw.toString(); sw.flush();
			 * 
			 * 					
			// gooper
			if(! model.isClosed()) {
				model.close();
			}
			if(model != null) {
			    model = null;
		    }

			if(mapper != null) {
				mapper = null;
			}
			if(doc != null) {
				doc = null;
			}
			 */
		} else if (ty == 2) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();

			/*
			 * OneM2MAEDTO cont = null; if (doc instanceof DBObject) {
			 * gson.toJson(doc)); cont = gson.fromJson(gson.toJson(doc),
			 * OneM2MAEDTO.class); } else if (doc instanceof String) {
			 * (doc)); cont = gson.fromJson((String) (doc), OneM2MAEDTO.class);
			 * } OneM2MAEMapper mapper = new OneM2MAEMapper(cont);
			 * 
			 * Model model = ModelFactory.createDefaultModel();
			 * model.add(mapper.from());
			 * 
			 * // 스트링 변환부분 RDFDataMgr.write(sw, model, RDFFormat.NTRIPLES);
			 * 
			 * log.debug("value from mongo ====2(json)====>" +
			 * cont.toString()); log.debug(
			 * "value from mongo ====2(sw)====>" + sw.toString());
			 * 
			 * returnStr = sw.toString(); sw.flush();
			 * 
			// gooper
			if(! model.isClosed()) {
				model.close();
			}
			if(model != null) {
				model = null;
			}

			if(mapper != null) {
				mapper = null;
			}
			if(doc != null) {
				doc = null;
			}

			 */
		} else if (ty == 1) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();
		} else if (ty == 0) {
			log.debug("=== ri : "+ri+",  ty : "+ty+" ====>exclude");			
			sw.flush();
		} else {
			log.debug("unknown ty("+ty+") value ====>exclude ");
		}
		
		return returnStr;
	}
	
	//public void addLatestContentInstance(String parentResourceUri,  String instanceUri) throws Exception {
	public void addLatestContentInstance() throws Exception {
		// delete
		// <mapper.getParentResourceUri(), "o:hasLatestContentInstance",?>
		// insert 
		// <mapper.getParentResourceUri(), "o:hasLatestContentInstance",mapper.getInstanceUri()>
		
		String deleteql =  " prefix o: <http://www.iotoasis.org/ontology/> "
								+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
								+" delete  { <@{arg0}> o:hasLatestContentInstance ?o . } "
								+" WHERE   { <@{arg0}> o:hasLatestContentInstance  ?o  .} "   ;
		
		String insertql =   " prefix o: <http://www.iotoasis.org/ontology/> "
								+" PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
								+" insert data { <@{arg0}> o:hasLatestContentInstance <@{arg1}> . }" ;

		//SparqlService sparqlService = new SparqlService();
		QueryService sparqlService= new QueryService(new SparqlQueryImpl());
		
		//log.debug("this.getParentResourceUri() ====> "+this.getParentResourceUri());
		//log.debug("this.getInstanceUri() =====>"+this.getInstanceUri());
		sparqlService.updateSparql(deleteql, insertql, new String[]{this.getParentResourceUri(), this.getInstanceUri()});
	}

	// triple파일 생성(작업시간과 생성시간을 엮어서 만듬)
	public void makeTripleFile(String triple_path_file, StringBuffer sb) throws Exception {
		FileWriter fw = null;
		BufferedWriter bw = null;
		log.info("makeTripleFile start==========================>");
		log.debug("makeTripleFile ========triple_path_file=================>" + triple_path_file);
		try {
			fw = new FileWriter(triple_path_file);
			bw = new BufferedWriter(fw);
			bw.write(sb.toString());

			bw.flush();
			fw.flush();
			
			bw.close();
			fw.close();
			
			log.info("makeTripleFile end==========================>");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (bw != null) {
				try {
					IOUtils.closeQuietly(bw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (fw != null) {
				try {
					IOUtils.closeQuietly(fw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// triple파일 전송
	public String[] sendTripleFile(String triple_path_file) throws Exception {
		String[] result = new String[]{"",""};
		
		String[] args = { "/home/pineone/svc/apps/sda/bin/apache-jena-fuseki-2.3.0/bin/s-post",
						"http://192.168.1.1:3030/icbms", "default", "/tmp/test.nt" };

		// conf값을 확인해서 재설정함
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.triple.regist.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		args[2] = "default";
		args[3] = triple_path_file;

		log.info("sendTripleFile start==========================>");
		log.debug("sendTripleFile ==============triple_path_file============>" + triple_path_file);
		
		// 개수확인(before)
		//log.debug("before count ====>\n");
		//Utils.getTripleCount();

		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}

		log.debug("sendTripleFile ==============args============>" + sb.toString());
	
		// 실행(1차)
		log.debug("try (first).......................");
		result = Utils.runShell(sb);
		
		log.debug("resultStr in TripleService.sendTripleFile() == > "+ Arrays.toString(result));
		
		if(result[1] == null || ! result[1].trim().equals("")) {
			log.debug("result[1](error message) in TripleService.sendTripleFile() == > "+ result[1]);
			int waitTime = 15*1000;
			// fuseki재기동 (에러 메세지 : 500 Server Error http://166.104.112.43:23030/icbms?default)
			if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
				
				log.debug("sleep (first)...........................");
				// 일정시간을 대기 한다.(1차)
				Thread.sleep(waitTime);

				// restart fuseki
				Utils.restartFuseki();
				
				log.debug("sleep (final)...........................");
				// 일정시간을 대기 한다.(2차)
				waitTime = 30*1000;
				Thread.sleep(waitTime);
				
				// 실행(2차)
				log.debug("try (final).......................");
				result = Utils.runShell(sb);
				if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
					throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
				}
			} else {
				throw new UserDefinedException(HttpStatus.CONTINUE,  result[1].toString());
			}
		}
		
		log.info("sendTripleFile end==========================>");

		// 개수확인(after)
		//log.debug("after count ====>\n");
		//Utils.getTripleCount();

		return result;
	}
	

	
	// triple파일 체크
	public String[] checkTripleFile(String triple_path_file, String result_file_name) throws Exception {
		String[] result = new String[]{"",""};

		String[] args = { "/svc/apps/sda/bin/jena/apache-jena-3.0.0/bin/riot",
						"--check", "/tmp/test.nt" };

		// conf값을 확인해서 재설정함
		String result_path = Utils.getSdaProperty("com.pineone.icbms.sda.riot.result.save_path");
		args[0] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.bin");
		args[1] = Utils.getSdaProperty("com.pineone.icbms.sda.riot.mode");;
		args[2] = triple_path_file;

		log.info("checkTripleFile start==========================>");
		log.debug("checkTripleFile ==============triple_path_file============>" + triple_path_file);
		log.debug("result_path ==============result_path============>" + result_path);
		
		StringBuilder sb = new StringBuilder();

		for (String str : args) {
			sb.append(str);
			sb.append(" ");
		}

		log.debug("checkTripleFile ==============args============>" + sb.toString());
	
		// 실행
		/*
		result = Utils.runShell(sb);
		//log.debug("resultStr in TripleService.checkTripleFile() == > "+ Arrays.toString(result));
		if(result[1] == null || ! result[1].trim().equals("")) {
			// fuseki재기동 (에러 메세지 : 500 Server Error http://166.104.112.43:23030/icbms?default)
			if(result[1].contains("500 Server Error")) {
				// restart fuseki
				Utils.restartFuseki();
			}
		// check하는 경우는 별도의 exception을 발생시키지 않음
		//throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
		}
		*/

		//실행(1차)
		log.debug("try (first)...........................");
		result = Utils.runShell(sb);
		
		if(result[1] == null || ! result[1].trim().equals("")) {
			log.debug("result[1](error message) in TripleService.checkTripleFile() == > "+ result[1]);
			int waitTime = 15*1000;
			// fuseki재기동 (에러 메세지 : 500 Server Error http://166.104.112.43:23030/icbms?default)
			if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
				log.debug("sleep (first)...........................");
				// 일정시간을 대기 한다.(1차)
				Thread.sleep(waitTime);

				// restart fuseki
				Utils.restartFuseki();
				
				log.debug("sleep (final)...........................");
				// 일정시간을 대기 한다.(2차)
				waitTime = 30*1000;
				Thread.sleep(waitTime);
				
				// 실행(2차)
				log.debug("try (final)...........................");
				result = Utils.runShell(sb);
				if(result[1].contains("500 Server Error") || result[1].contains("java.net.ConnectException")  || result[1].contains("Service Unavailable") ) {
					throw new UserDefinedException(HttpStatus.GONE,  result[1].toString());
				}
			} else {
				throw new UserDefinedException(HttpStatus.CONTINUE,  result[1].toString());
			}
		}


		
		log.info("checkTripleFile end==========================>");

		return result;
	}
	
	
	// triple파일 체크결과 파일 생성(작업시간과 생성시간을 엮어서 만듬)
	public void makeResultFile(String file_name, String[] check_result) throws Exception {
		FileWriter fw = null;
		BufferedWriter bw = null;
		String result_path = Utils.getSdaProperty("com.pineone.icbms.sda.riot.result.save_path");
		String result_path_file = "";
		log.info("makeResultFile start==========================>");
		log.debug("makeResultFile ========result_path_file=================>" + result_path_file);
		
		// 폴더가 없으면 생성
		result_path = Utils.makeSavePath(result_path);
		
		try {
			result_path_file = result_path + "/" + file_name;
			fw = new FileWriter(result_path_file);
			bw = new BufferedWriter(fw);
			bw.write(check_result[1]+check_result[0]);

			bw.flush();
			fw.flush();
			
			bw.close();
			fw.close();
			
			log.info("makeResultFile end==========================>");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (bw != null) {
				try {
					IOUtils.closeQuietly(bw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (fw != null) {
				try {
					IOUtils.closeQuietly(fw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
