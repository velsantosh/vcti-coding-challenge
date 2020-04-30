package com.vcti.ct.CCTServices.model;

public class QuesResponse {
	private String qId;
	private String userInput;
	private String compilationsStatus;

	public String getCompilationsStatus() {
		return compilationsStatus;
	}

	public void setCompilationsStatus(String compilationsStatus) {
		this.compilationsStatus = compilationsStatus;
	}

	public String getqId() {
		return qId;
	}

	public void setqId(String qId) {
		this.qId = qId;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
}
