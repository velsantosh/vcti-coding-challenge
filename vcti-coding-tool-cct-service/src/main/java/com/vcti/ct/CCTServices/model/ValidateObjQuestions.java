package com.vcti.ct.CCTServices.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidateObjQuestions {
	private @NonNull String userId;
	private Map<String, String> questionOptionMap;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getQuestionOptionMap() {
		return questionOptionMap;
	}

	public void setQuestionOptionMap(Map<String, String> questionOptionMap) {
		this.questionOptionMap = questionOptionMap;
	}
}