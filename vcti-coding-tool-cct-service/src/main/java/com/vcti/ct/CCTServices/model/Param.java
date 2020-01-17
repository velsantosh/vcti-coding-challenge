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
@Table("param")
public class Param {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String qId;
	private @NonNull String name;
	private @NonNull String type;
	private @NonNull String testcaseid;

	public Param() {
		super();
	}

	public Param(@NonNull String id, @NonNull String qId, @NonNull String name, @NonNull String type,
			@NonNull String testcaseid) {
		super();
		this.id = id;
		this.qId = qId;
		this.name = name;
		this.type = type;
		this.testcaseid = testcaseid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getqId() {
		return qId;
	}

	public void setqId(String qId) {
		this.qId = qId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTestcaseid() {
		return testcaseid;
	}

	public void setTestcaseid(String testcaseid) {
		this.testcaseid = testcaseid;
	}

}