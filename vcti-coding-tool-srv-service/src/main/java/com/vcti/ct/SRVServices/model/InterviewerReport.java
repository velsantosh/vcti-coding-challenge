package com.vcti.ct.SRVServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Table("interviewer_report")
public class InterviewerReport {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String interviewerid;
	private @NonNull String challengeid;
	
	public InterviewerReport() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterviewerid() {
		return interviewerid;
	}

	public void setInterviewerid(String interviewerid) {
		this.interviewerid = interviewerid;
	}

	public String getChallengeid() {
		return challengeid;
	}

	public void setChallengeid(String challengeid) {
		this.challengeid = challengeid;
	}

	
	
}
