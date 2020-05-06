package com.vcti.ct.SRVServices.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author sandeepkumar.yadav
 *
 */

@XmlRootElement
public class Interviewer {

	private String toEmailIds;
	private String candidateId;
	private String subject;
	private String body;
	
	public Interviewer() {
	}

	public Interviewer(String toEmailIds, String candidateId, String subject, String body) {
		super();
		this.toEmailIds = toEmailIds;
		this.candidateId = candidateId;
		this.subject = subject;
		this.body = body;
	}

	public String getToEmailIds() {
		return toEmailIds;
	}

	public void setToEmailIds(String toEmailIds) {
		this.toEmailIds = toEmailIds;
	}

	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
