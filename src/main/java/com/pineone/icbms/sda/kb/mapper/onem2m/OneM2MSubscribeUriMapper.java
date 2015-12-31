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
 * 
 * @author rosenc
 * 
 *         retrieve make subscription Uri
 */
public class OneM2MSubscribeUriMapper {

	private String domain;
	private String condition;
	// private ICBMSContextConditionModel jsonModel;
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

	private void setConditionModel() {
		Gson gson = new Gson();
		Type type = new TypeToken<List<ICBMSContextConditionModel>>() {
		}.getType();
		clist = gson.fromJson(this.condition, type);
	}

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

	private String makeTypeField(List<String> type) {
		String result = "";
		String typequery = " ?uri rdf:type @type@  . \n";
		for (int i = 0; i < type.size(); i++) {
			result = result + new String(typequery).replaceAll("@type@", type.get(i));
		}
		return result;
	}

	private String makeDomainField() {
		if (this.domain == null)
			return "";
		else
			return "	?uri  DUL:hasLocation ?domain.		\n	" + "	?domain rdf:type " + domain + " .		\n	";
	}

	private String getSubscribeDataContainer(String type) {
		String result = "/Data";
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
	
	

	public String makeQueryString() {
		String query = Utils.getSparQlHeader();
		query = query + " SELECT   distinct  ?uri  where {		\n	";
		query = query + this.makeTypeField(getTypedUri());
		query = query + this.makeDomainField() + "} ";

		return query;
	}

	public String getProperContainerType(String uri) {
		// 1차년도는 status 정보 subscribe
		return uri + "/" + getSubscribeDataContainer("status");
		// 향후 subscribe 범위가늘어날 경우 서비스 추가 
	}

	// subscribe AE/action/data
	// public url이 정해지면 수정 필
	public List<String> getSubscribeUri() {
		List<String> result = new ArrayList<String>();
		String query = this.makeQueryString();
		String serviceURI = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.sparql.endpoint");
		String baseuri = Utils.getSdaProperty("com.pineone.icbms.sda.knowledgebase.uri");
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(serviceURI, query);
		ResultSet rs = queryExec.execSelect();
		for (; rs.hasNext();) {
			QuerySolution qs = rs.nextSolution();
			result.add(getProperContainerType(
					new String(qs.get("uri").toString().replaceAll(baseuri, ""))));
		}
		return result;
	}
	
	public static void main(String[] args) {
		OneM2MSubscribeUriMapper mapper = new OneM2MSubscribeUriMapper("icbms:ClassRoom",
				"[{\"subject\":\"?arg1\", \"predicate\":\"rdf:type\", \"object\":\"m2m:TemperatureSensor\"}]");
		System.out.println(mapper.getSubscribeUri());
	}

}
