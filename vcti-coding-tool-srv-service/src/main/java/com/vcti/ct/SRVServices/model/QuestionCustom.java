package com.vcti.ct.SRVServices.model;

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
public class QuestionCustom {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String type;
	private @NonNull String statement;
	private List<String> options;
	private String correct_option;
	private String methodName;

	public QuestionCustom() {
		super();
	}

	public QuestionCustom(String id, String type, String statement, List<String> options, String correct_option,
			String methodName) {
		super();
		this.id = id;
		this.type = type;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
		this.methodName = methodName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
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
}