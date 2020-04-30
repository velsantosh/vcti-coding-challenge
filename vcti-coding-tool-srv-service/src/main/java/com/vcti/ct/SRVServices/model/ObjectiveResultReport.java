package com.vcti.ct.SRVServices.model;

public class ObjectiveResultReport {

	private String problem;
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
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
	private String option;
	private String slectedAnswer;
	private String correctAnswer;
	
	
}
