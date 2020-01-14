package com.hackathon.SRVServices.model;

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
public class Output {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String output;

	public Output() {
		super();
	}

}