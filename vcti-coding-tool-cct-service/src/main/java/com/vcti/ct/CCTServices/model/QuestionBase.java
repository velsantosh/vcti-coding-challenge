package com.vcti.ct.CCTServices.model;

import java.util.Map;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class QuestionBase {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String language;
	private @NonNull String type;
	private @NonNull String experience;
	private @NonNull String createdUserid;
	private @NonNull String statement;
	private @NonNull String options;
	private @NonNull String correct_option;
	private @NonNull String methodName;
	private Map<String, Map<String, Param>> testCaseMap;

	public QuestionBase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionBase(@NonNull String id, @NonNull String language, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String statement, @NonNull String options,
			@NonNull String correct_option, @NonNull String methodName, Map<String, Map<String, Param>> testCaseMap) {
		super();
		this.id = id;
		this.language = language;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
		this.methodName = methodName;
		this.testCaseMap = testCaseMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCreatedUserid() {
		return createdUserid;
	}

	public void setCreatedUserid(String createdUserid) {
		this.createdUserid = createdUserid;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getCorrect_option() {
		return correct_option;
	}

	public void setCorrect_option(String correct_option) {
		this.correct_option = correct_option;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Map<String, Map<String, Param>> getTestCaseMap() {
		return testCaseMap;
	}

	public void setTestCaseMap(Map<String, Map<String, Param>> testCaseMap) {
		this.testCaseMap = testCaseMap;
	}

}