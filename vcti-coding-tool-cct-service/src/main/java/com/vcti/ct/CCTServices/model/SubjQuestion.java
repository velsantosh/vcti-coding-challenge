package com.vcti.ct.CCTServices.model;

import java.util.Map;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Table("subjectiveq")
public class SubjQuestion {
	@PrimaryKey
	private @NonNull String qId;
	private @NonNull String statement;
	private @NonNull String methodName;

	public SubjQuestion(@NonNull String qId, @NonNull String statement, @NonNull String methodName) {
		super();
		this.qId = qId;
		this.statement = statement;
		this.methodName = methodName;
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

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

}