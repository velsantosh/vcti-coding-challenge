package com.vcti.ct.SRVServices.model;

import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class QuestionCustom {
	@PrimaryKey
	private String id;
	private @NonNull String type;
	private @NonNull String statement;
	private List<String> options;
	private String correct_option;
	private String methodName;
	private String experience;
	private @NonNull String createdUserid;
	private ByteBuffer junitObj;
	private @NonNull String title;
	private String difficulty;
	private String expectedTime;
	private @NonNull String technologyId;
	private @NonNull String technology;
	private @NonNull String topic;
	private String junitText;

	public QuestionCustom() {
		super();
	}

	public QuestionCustom(String id, String type, String statement, List<String> options, String correct_option,
			String methodName) {
		super();
		this.id = id;
		this.type = type;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
		this.methodName = methodName;
	}

	public QuestionCustom(@NonNull String id, @NonNull String type, @NonNull String statement, List<String> options,
			String correct_option, String methodName, String experience, @NonNull String createdUserid,
			ByteBuffer junitObj, @NonNull String title, String difficulty,
			String expectedTime, @NonNull String technologyId, @NonNull String technology,
			@NonNull String topic, String junitText) {
		super();
		this.id = id;
		this.type = type;
		this.statement = statement;
		this.options = options;
		this.correct_option = correct_option;
		this.methodName = methodName;
		this.experience = experience;
		this.createdUserid = createdUserid;
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

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
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

	public ByteBuffer getJunitObj() {
		return junitObj;
	}

	public void setJunitObj(ByteBuffer junitObj) {
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