package com.vcti.ct.SRVServices.model;

import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class QuestionBase {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String type;
	private @NonNull String experience;
	private @NonNull String createdUserid;
	private @NonNull String statement;
	private @NonNull List<String> options;
	private @NonNull String correct_option;
	private @NonNull String methodName;
	private @NonNull ByteBuffer junitObj;
	private @NonNull String title;
	private @NonNull String difficulty;
	private @NonNull String expectedTime;
	private @NonNull String technologyId;
	private @NonNull String technology;
	private @NonNull String topic;
	private String junitText;

	public QuestionBase() {
		super();
	}

	public QuestionBase(@NonNull String id, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String statement, @NonNull List<String> options,
			@NonNull String correct_option, @NonNull String methodName, @NonNull ByteBuffer junitObj) {
		super();
		this.id = id;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
		this.methodName = methodName;
		this.junitObj = junitObj;
	}

	public QuestionBase(@NonNull String id, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String statement, @NonNull List<String> options,
			@NonNull String correct_option, @NonNull String methodName, @NonNull ByteBuffer junitObj,
			@NonNull String title, @NonNull String difficulty, @NonNull String expectedTime,
			@NonNull String technologyId, @NonNull String technology, @NonNull String topic, String junitText) {
		super();
		this.id = id;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
		this.methodName = methodName;
		this.junitObj = junitObj;
		this.title = title;
		this.difficulty = difficulty;
		this.expectedTime = expectedTime;
		this.technologyId = technologyId;
		this.technology = technology;
		this.topic = topic;
		this.junitText = junitText;
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

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public @NonNull List<String> getOptions() {
		return options;
	}

	public void setOptions(@NonNull List<String> options) {
		this.options = options;
	}

	public String getCorrect_option() {
		return correct_option;
	}

	public void setCorrect_option(String correct_option) {
		this.correct_option = correct_option;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public ByteBuffer getJunitObj() {
		return junitObj;
	}

	public void setJunitObj(@NonNull ByteBuffer junitObj) {
		this.junitObj = junitObj;
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

	public String getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(String expectedTime) {
		this.expectedTime = expectedTime;
	}

	public String getTechnologyId() {
		return technologyId;
	}

	public void setTechnologyId(String technologyId) {
		this.technologyId = technologyId;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getJunitText() {
		return junitText;
	}

	public void setJunitText(String junitText) {
		this.junitText = junitText;
	}
}