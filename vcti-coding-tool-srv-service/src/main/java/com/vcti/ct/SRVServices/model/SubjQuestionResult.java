package com.vcti.ct.SRVServices.model;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
@Table("subjresult")
public class SubjQuestionResult {
	@PrimaryKey
	private ResultKey key;
	private @NonNull ByteBuffer program;
	private @NonNull String consolidatedoutput;
	private String compilationStatus;
	private String className;

	public String getCompilationStatus() {
		return compilationStatus;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setCompilationStatus(String compilationStatus) {
		this.compilationStatus = compilationStatus;
	}
 public SubjQuestionResult() {
	 
 }
	public SubjQuestionResult(final ResultKey key, @NonNull ByteBuffer program, @NonNull String consolidatedoutput,String compilationStatus,String className) {
		super();
		this.key = key;
		this.program = program;
		this.consolidatedoutput = consolidatedoutput;
		this.compilationStatus=compilationStatus;
		this.className=className;
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