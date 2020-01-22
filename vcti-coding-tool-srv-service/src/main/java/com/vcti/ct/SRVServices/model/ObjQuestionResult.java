package com.vcti.ct.SRVServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Table("objresult")
public class ObjQuestionResult {
	@PrimaryKey
	private ResultKey key;
	private @NonNull String selectedoption;

	public ObjQuestionResult(final ResultKey key, @NonNull String selectedoption) {
		super();
		this.key = key;
		this.selectedoption = selectedoption;
	}

	public ResultKey getKey() {
		return key;
	}

	public void setKey(ResultKey key) {
		this.key = key;
	}

	public String getSelectedoption() {
		return selectedoption;
	}

	public void setSelectedoption(String selectedoption) {
		this.selectedoption = selectedoption;
	}

}