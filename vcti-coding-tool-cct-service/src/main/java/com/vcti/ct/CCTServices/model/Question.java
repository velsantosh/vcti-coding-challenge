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
@Table("questions")

public class Question {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String language;
	private @NonNull String type;
	private @NonNull String experience;
	private @NonNull String createdUserid;

	public Question(@NonNull String id, @NonNull String language, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid) {
		super();
		this.id = id;
		this.language = language;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getCreatedUserid() {
		return createdUserid;
	}

	public void setCreatedUserid(String createdUserid) {
		this.createdUserid = createdUserid;
	}

}