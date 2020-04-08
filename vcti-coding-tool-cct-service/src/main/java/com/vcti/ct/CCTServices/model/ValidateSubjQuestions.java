package com.vcti.ct.CCTServices.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidateSubjQuestions {
	private @NonNull String userId;
	/**
	 * questionOptionMap for storing key as qId and value as complete java program
	 */
	private Map<String, String> questionProgramMap;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getQuestionProgramMap() {
		return questionProgramMap;
	}

	public void setQuestionProgramMap(Map<String, String> questionProgramMap) {
		this.questionProgramMap = questionProgramMap;
	}

}