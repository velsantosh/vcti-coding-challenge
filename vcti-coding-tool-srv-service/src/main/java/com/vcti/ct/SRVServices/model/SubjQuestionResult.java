package com.vcti.ct.SRVServices.model;

import java.sql.Blob;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Table("subjresult")
public class SubjQuestionResult {
	@PrimaryKey
	private ResultKey key;
	private @NonNull Blob program;
	private @NonNull String consolidatedoutput;

	public SubjQuestionResult(final ResultKey key, @NonNull Blob program, @NonNull String consolidatedoutput) {
		super();
		this.key = key;
		this.program = program;
		this.consolidatedoutput = consolidatedoutput;
	}

	public ResultKey getKey() {
		return key;
	}

	public void setKey(ResultKey key) {
		this.key = key;
	}

	public Blob getProgram() {
		return program;
	}

	public void setProgram(Blob program) {
		this.program = program;
	}

	public String getConsolidatedoutput() {
		return consolidatedoutput;
	}

	public void setConsolidatedoutput(String consolidatedoutput) {
		this.consolidatedoutput = consolidatedoutput;
	}

}