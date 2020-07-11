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
@AllArgsConstructor
@Getter
@Setter
@Table("questionTemplate")
public class QuestionTemplate {
	

	@PrimaryKey
	private @NonNull String id;
	private @NonNull String templateName;
	private  String technology;
	private @NonNull List<String> questionList;
	private @NonNull String experiance;

	
	public QuestionTemplate(@NonNull String id, @NonNull String templateName, String technology,
			@NonNull List<String> questionList, @NonNull String experiance) {
		super();
		this.id = id;
		this.templateName = templateName;
		this.technology = technology;
		this.questionList = questionList;
		this.experiance = experiance;
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
	public String getExperiance() {
		return experiance;
	}
	public void setExperiance(String experiance) {
		this.experiance = experiance;
	}

	

}
