package com.vcti.ct.SRVServices.model;

public class CandidateResult {

	 private String candidateName;
	 private String id;
	 public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String testcaseReport;
	 private String testScheduler;
	 private String status;
	 private int testCasePercentage;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTestCasePercentage() {
		return testCasePercentage;
	}
	public void setTestCasePercentage(int testCasePercentage) {
		this.testCasePercentage = testCasePercentage;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getTestcaseReport() {
		return testcaseReport;
	}
	public void setTestcaseReport(String testcaseReport) {
		this.testcaseReport = testcaseReport;
	}
	public String getTestScheduler() {
		return testScheduler;
	}
	public void setTestScheduler(String testScheduler) {
		this.testScheduler = testScheduler;
	}
	 
}
