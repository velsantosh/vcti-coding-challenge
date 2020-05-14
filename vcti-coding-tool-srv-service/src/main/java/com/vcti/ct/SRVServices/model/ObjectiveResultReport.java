package com.vcti.ct.SRVServices.model;

import java.util.List;

public class ObjectiveResultReport {

	private String problem;
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	
	public String getSlectedAnswer() {
		return slectedAnswer;
	}
	public void setSlectedAnswer(String slectedAnswer) {
		this.slectedAnswer = slectedAnswer;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	private List<String> options;
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	private String slectedAnswer;
	private String correctAnswer;
	
	
}
