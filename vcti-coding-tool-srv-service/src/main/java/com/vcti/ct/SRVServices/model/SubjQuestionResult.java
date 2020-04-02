package com.vcti.ct.SRVServices.model;

import java.nio.ByteBuffer;

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
	private @NonNull ByteBuffer program;
	private @NonNull String consolidatedoutput;

	public SubjQuestionResult(final ResultKey key, @NonNull ByteBuffer program, @NonNull String consolidatedoutput) {
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

	public @NonNull ByteBuffer getProgram() {
		return program;
	}

	public void setProgram(@NonNull ByteBuffer program) {
		this.program = program;
	}

	public String getConsolidatedoutput() {
		return consolidatedoutput;
	}

	public void setConsolidatedoutput(String consolidatedoutput) {
		this.consolidatedoutput = consolidatedoutput;
	}

}