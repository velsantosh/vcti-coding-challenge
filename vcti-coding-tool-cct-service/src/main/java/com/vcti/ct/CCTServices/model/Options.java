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
@Table("options")
public class Options {
	@PrimaryKey
	private @NonNull String id;

	private @NonNull String qId;

	private @NonNull String options;

	public Options(@NonNull String id, String qId, String options) {
		super();
		this.id = id;
		this.qId = qId;
		this.options = options;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQId() {
		return qId;
	}

	public void setQId(String qId) {
		this.qId = qId;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

}