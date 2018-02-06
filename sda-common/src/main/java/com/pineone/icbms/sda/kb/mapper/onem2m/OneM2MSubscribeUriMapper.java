package com.pineone.icbms.sda.kb.mapper.onem2m;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pineone.icbms.sda.comm.util.Utils;
import com.pineone.icbms.sda.kb.dto.OneM2MContainerType;
import com.pineone.icbms.sda.kb.model.ICBMSContextConditionModel;

/**
 *   Subscribe할 URI의 Mapper클래스
 */
public class OneM2MSubscribeUriMapper {

	private String domain;
	private String condition;
	private List<ICBMSContextConditionModel> clist;

	public OneM2MSubscribeUriMapper(String condition) {
		this.condition = condition;
		this.setConditionModel();
	}

	public OneM2MSubscribeUriMapper(String domain, String condition) {
		this.domain = domain;
		this.condition = condition;
		this.setConditionModel();
	}

	/**
	 * condition model 설정
	 * @return void
	 */
	private void setConditionModel() {
		Gson gson = new Gson();
		Type type = new TypeToken<List<ICBMSContextConditionModel>>() {
		}.getType();
		clist = gson.fromJson(this.condition, type);
	}

	/**
	 * 타입형 uri 가져오기
	 * @return List<String>
	 */
	private List<String> getTypedUri() {
		Iterator<ICBMSContextConditionModel> it = this.clist.iterator();
		List<String> typeList = new ArrayList<String>();
		while (it.hasNext()) {
			ICBMSContextConditionModel model = it.next();
			if (model.getPredicate().contains("type")) {
				typeList.add(model.getObject());
			}
		}
		return typeList;
	}

	/**
	 * 타입 필드 생성
	 * @param type
	 * @return String
	 */
	private String makeTypeField(List<String> type) {
		String result = "";
		String typequery = "\t{ ?uri o:hasDeviceType @type@ .}";
		for (int i = 0; i < type.size(); i++) {
			if(i != 0 && type.size() >= 2) {
				result = result +"\n\t\t union";
			}
			result = result + new String(typequery).replaceAll("@type@", type.get(i));
		}
		return result;
	}

	/**
	 * 도메인 필드 생성
	 * @return String
	 */
	private String makeDomainField() {
		if (this.domain == null)
			return "";
		else
			return "\n\t?uri dul:hasLocation "+ domain + ".";
	}

	/**
	 * subscribe container 가져오기
	 * @param type
	 * @return String
	 */
	private String getSubscribeDataContainer(String type) {
		String result = "";
		if (type.equals(OneM2MContainerType.status.toString())) {
			return OneM2MContainerType.status + result;
		} else if (type.equals(OneM2MContainerType.reserved.toString())) {
			return OneM2MContainerType.reserved + result;
		} else if (type.equals(OneM2MContainerType.zone.toString())) {
			return OneM2MContainerType.zone + result;
		} else if (type.equals(OneM2MContainerType.batt.toString())) {
			return OneM2MContainerType.batt + result;
		} else if (type.equals(OneM2MContainerType.stanbypower.toString())) {
			return OneM2MContainerType.stanbypower + result;
		} else if (type.equals(OneM2MContainerType.status_batt.toString())) {
			return OneM2MContainerType.status_batt + result;
		}
		return result;
	}
	
	/**
	 * 쿼리 스트링 생성
	 * @return String
	 */
	public String makeQueryString() {
		String query = Utils.getSparQlHeader();
		query = query + "select  distinct  ?res  where {	\n\t?uri rdf:type b:Device.\n	";
		query = query + this.makeTypeField(getTypedUri());
		query = query + this.makeDomainField() + " ?uri o:hasResource ?res } ";

		return query;
	}

	/**
	 * container 타입 가져오기
	 * @param uri
	 * @return String
	 */
	public String getProperContainerType(String uri) {
		return uri + "/" + getSubscribeDataContainer("status");
	}

	/**
	 * subscribe uri 가져오기
	 * @return List<String>
	 */
	public List<String> getSubscribeUri() {
		List<String> result = new ArrayList<String>();
		String query = this.makeQueryString();
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.dw.sparql.endpoint");
		String baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, query);
		ResultSet rs = queryExec.execSelect();
		for (; rs.hasNext();) {
			QuerySolution qs = rs.nextSolution();
			result.add(getProperContainerType(
					new String(qs.get("res").toString().replaceAll(baseuri, ""))));
		}
		return result;
	}
}