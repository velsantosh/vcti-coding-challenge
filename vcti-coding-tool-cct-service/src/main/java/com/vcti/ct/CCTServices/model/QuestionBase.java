package com.vcti.ct.CCTServices.model;

import java.nio.ByteBuffer;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
	private @NonNull String correctOption;
	private @NonNull String methodName;
	private ByteBuffer junitObj;
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

	public QuestionBase(@NonNull String type, @NonNull String statement, @NonNull List<String> options,
			@NonNull String correctOption, @NonNull String technology, @NonNull String title,
			@NonNull String difficulty, @NonNull String experience, @NonNull String topic,
			@NonNull String expectedTime) {
		super();
		this.type = type;
		this.statement = statement;
		this.options = options;
		this.correctOption = correctOption;
		this.technology = technology;
		this.title = title;
		this.difficulty = difficulty;
		this.experience = experience;
		this.topic = topic;
		this.expectedTime = expectedTime;
	}

	public QuestionBase(@NonNull String type, @NonNull String statement, @NonNull String technology,
			@NonNull String title, @NonNull String difficulty, @NonNull String experience, @NonNull String topic,
			@NonNull String expectedTime, String junitText, @NonNull String methodName) {
		super();
		this.type = type;
		this.statement = statement;
		this.technology = technology;
		this.title = title;
		this.difficulty = difficulty;
		this.experience = experience;
		this.topic = topic;
		this.expectedTime = expectedTime;
		this.junitText = junitText;
		this.methodName = methodName;
	}

	public QuestionBase(@NonNull String id, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String statement, @NonNull List<String> options,
			@NonNull String correctOption, @NonNull String methodName, @NonNull ByteBuffer junitObj) {
		super();
		this.id = id;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
		this.statement = statement;
		this.options = options;
		this.correctOption = correctOption;
		this.methodName = methodName;
		this.junitObj = junitObj;
	}

	public QuestionBase(@NonNull String id, @NonNull String type, @NonNull String experience,
			@NonNull String createdUserid, @NonNull String statement, @NonNull List<String> options,
			@NonNull String correctOption, @NonNull String methodName, ByteBuffer junitObj, @NonNull String title,
			@NonNull String difficulty, @NonNull String expectedTime, @NonNull String technologyId,
			@NonNull String technology, @NonNull String topic, String junitText) {
		super();
		this.id = id;
		this.type = type;
		this.experience = experience;
		this.createdUserid = createdUserid;
		this.statement = statement;
		this.options = options;
		this.correctOption = correctOption;
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

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getCorrectOption() {
		return correctOption;
	}

	public void setCorrect_option(String correctOption) {
		this.correctOption = correctOption;
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

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTechnologyId() {
		return technologyId;
	}

	public void setTechnologyId(String technologyId) {
		this.technologyId = technologyId;
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