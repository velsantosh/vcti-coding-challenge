package com.vcti.ct.CCTServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Table("questions")

public class Question {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String type;
	private @NonNull String experience;
	private @NonNull String createdUserid;
	private @NonNull String title;
	private @NonNull String difficulty;
	private @NonNull String technologyId;

	public Question() {
		
	}
	public Question(@NonNull String id, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid) {
		super();
		this.id = id;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
	}
	
	public Question(@NonNull String id, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String title, @NonNull String difficulty, @NonNull String technologyId) {
		super();
		this.id = id;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
		this.title = title;
		this.difficulty = difficulty;
		this.technologyId = technologyId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	public String getTechnologyId() {
		return technologyId;
	}
	public void setTechnologyId(String technologyId) {
		this.technologyId = technologyId;
	}

}