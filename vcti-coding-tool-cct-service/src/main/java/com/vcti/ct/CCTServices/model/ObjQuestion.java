package com.vcti.ct.CCTServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Table("objectiveq")
public class ObjQuestion {
	@PrimaryKey
	private @NonNull String qId;
	private @NonNull String statement;
	private @NonNull String options;
	private @NonNull String correct_option;

	public ObjQuestion(@NonNull String qId, @NonNull String statement, @NonNull String options,
			@NonNull String correct_option) {
		super();
		this.qId = qId;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
	}

	public String getqId() {
		return qId;
	}

	public void setqId(String qId) {
		this.qId = qId;
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

}