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
public class Param {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String q_id;
	private @NonNull String name;
	private @NonNull String type;
	private @NonNull String testcaseid;

	public Param() {
		super();
	}

}