package com.vcti.ct.SRVServices.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidateSubjQuestions {
  
	private @NonNull String userId;
	private String className;
	/**
	 * quesResponseObj for storing qId and complete java program
	 */
	private QuesResponse quesResponseObj;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public QuesResponse getQuesResponseObj() {
		return quesResponseObj;
	}

	public void setQuesResponseObj(QuesResponse quesResponseObj) {
		this.quesResponseObj = quesResponseObj;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
