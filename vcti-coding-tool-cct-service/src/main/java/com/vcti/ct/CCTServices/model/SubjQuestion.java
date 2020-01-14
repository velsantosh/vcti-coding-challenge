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
@Table
public class SubjQuestion {
	@PrimaryKey
	private @NonNull String qId;
	private @NonNull String statement;
	private @NonNull String methodName;
	private @NonNull String paramIdList;

	public SubjQuestion(@NonNull String qId, @NonNull String statement, @NonNull String methodName,
			@NonNull String paramIdList) {
		super();
		this.qId = qId;
		this.statement = statement;
		this.methodName = methodName;
		this.paramIdList = paramIdList;
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

	public String getParamIdList() {
		return paramIdList;
	}

	public void setParamIdList(String paramIdList) {
		this.paramIdList = paramIdList;
	}

}