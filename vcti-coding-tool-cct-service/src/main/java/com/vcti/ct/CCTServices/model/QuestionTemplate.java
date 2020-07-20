package com.vcti.ct.CCTServices.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author afser.basha
 *
 */
@Getter
@Setter
@Table("questionTemplate")
public class QuestionTemplate {
	

	@PrimaryKey
	private @NonNull String id;
	private @NonNull String templateName;
	private  String technology;
	private @NonNull List<String> questionList;
	private @NonNull String experience;
	private @NonNull String difficulty;
	
	public QuestionTemplate(@NonNull String id, @NonNull String templateName, String technology,
			@NonNull List<String> questionList, @NonNull String experience, @NonNull String difficulty) {
		super();
		this.id = id;
		this.templateName = templateName;
		this.technology = technology;
		this.questionList = questionList;
		this.experience = experience;
		this.difficulty = difficulty;
	}
	public QuestionTemplate() {
		
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public List<String> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<String> questionList) {
		this.questionList = questionList;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	

}
