package com.pineone.icbms.sda.kb.context.routine;

import java.util.HashMap;
import java.util.UUID;

/**
 *   Context클래스
 */
public class ICBMSContext {

	private String observationId;
	private HashMap<String, String> observationValue;

	/**
	 *   Id생성
	 * @return
	 */
	private String createObservationId() {
		return UUID.randomUUID().toString();
	}

	public String getObservationId() {
		if (observationId == null)
			return this.createObservationId();
		return this.observationId;
	}

	public void setObservationId(String observationId) {
		this.observationId = observationId;
	}

	public HashMap<String, String> getObservationValue() {
		return observationValue;
	}

	public void setObservationValue(HashMap<String, String> observationValue) {
		this.observationValue = observationValue;
	}

	public static void main(String[] args) {
		ICBMSContext a = new ICBMSContext();
		System.out.println(a.getObservationId());
	}
}
