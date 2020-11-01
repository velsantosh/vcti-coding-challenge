package com.vcti.ct.SRVServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
@Table("objresult")
public class ObjQuestionResult {
	
	public ObjQuestionResult() {
		super();
	}
	public ObjQuestionResult(ResultKey key, @NonNull String selectedoption, @NonNull String questionContent,
			@NonNull String selectedAnswer) {
		super();
		this.key = key;
		this.selectedoption = selectedoption;
		this.questionContent = questionContent;
		this.selectedAnswer = selectedAnswer;
	}

	public ResultKey getKey() {
		return key;
	}
	public void setKey(ResultKey key) {
		this.key = key;
	}
	public String getSelectedoption() {
		return selectedoption;
	}
	public void setSelectedoption(String selectedoption) {
		this.selectedoption = selectedoption;
	}
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	public String getSelectedAnswer() {
		return selectedAnswer;
	}
	public void setSelectedAnswer(String selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}
	@PrimaryKey
	private ResultKey key;
	private @NonNull String selectedoption;
	private @NonNull String questionContent;
	private @NonNull String selectedAnswer;
	


	

}