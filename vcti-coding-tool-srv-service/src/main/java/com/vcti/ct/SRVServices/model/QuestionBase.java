package com.vcti.ct.SRVServices.model;

import java.nio.ByteBuffer;
import java.util.List;

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
	private @NonNull List<String> options;
	private @NonNull String correct_option;
	private @NonNull String methodName;
	private @NonNull ByteBuffer junitObj;

	public QuestionBase() {
		super();
	}

	public QuestionBase(@NonNull String id, @NonNull String language, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String statement, @NonNull List<String> options,
			@NonNull String correct_option, @NonNull String methodName, @NonNull ByteBuffer junitObj) {
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
		this.junitObj = junitObj;
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

	public @NonNull List<String> getOptions() {
		return options;
	}

	public void setOptions(@NonNull List<String> options) {
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

	public ByteBuffer getJunitObj() {
		return junitObj;
	}

	public void setJunitObj(@NonNull ByteBuffer junitObj) {
		this.junitObj = junitObj;
	}

}