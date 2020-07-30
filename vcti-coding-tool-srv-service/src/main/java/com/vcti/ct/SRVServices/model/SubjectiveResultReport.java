package com.vcti.ct.SRVServices.model;

public class SubjectiveResultReport {

	private String program;
	private String ansSubmitted;
	private String consolidatedOutput;
	private String timeTook;
	private String clicksonRunTest;

	
	public String getClicksonRunTest() {
		return clicksonRunTest;
	}
	public void setClicksonRunTest(String clicksonRunTest) {
		this.clicksonRunTest = clicksonRunTest;
	}
	public String getTimeTook() {
		return timeTook;
	}
	public void setTimeTook(String timeTook) {
		this.timeTook = timeTook;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getAnsSubmitted() {
		return ansSubmitted;
	}
	public void setAnsSubmitted(String ansSubmitted) {
		this.ansSubmitted = ansSubmitted;
	}
	public String getConsolidatedOutput() {
		return consolidatedOutput;
	}
	public void setConsolidatedOutput(String consolidatedOutput) {
		this.consolidatedOutput = consolidatedOutput;
	}
	
}
