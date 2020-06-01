package com.vcti.ct.SRVServices.model;

import java.util.Date;

import lombok.NonNull;

public class CandidateResult {

	 private String candidateName;
	 private String id;
   	 private String testcaseReport;
	 private String testScheduler;
	 private String status;
	 private int testCasePercentage;
	 private Date scheduleDate;
	 private String challengeid;
	 
	 public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	 
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
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getChallengeid() {
		return challengeid;
	}
	public void setChallengeid(String challengeid) {
		this.challengeid = challengeid;
	}
	 
}
