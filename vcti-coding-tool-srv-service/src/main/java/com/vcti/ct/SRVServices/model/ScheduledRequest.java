package com.vcti.ct.SRVServices.model;

import java.util.Date;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sandeepkumar.yadav
 *
 */
@AllArgsConstructor
@Getter
@Setter
@Table("scheduled_request")
public class ScheduledRequest {
	@PrimaryKey
	private String id;
	private String hiringManagerName;
	private String hiringManagerId;
	private String recruiterName;
	private String recruiterId;
	private String candidateName;
	private String candidateEmailId;
	private String candidateMobileNo;
	private String candidateExperience;
	private String technology;
	private Date interviewDate;
	private String requestedDate;
	private String requirementId;
	public ScheduledRequest() {
	}

	public ScheduledRequest(String id, String hiringManagerName, String hiringManagerId, String recruiterName,
			String recruiterId, String candidateName, String candidateEmailId, String candidateMobileNo,
			String candidateExperience, String technology, Date interviewDate, String requestedDate,
			String requirementId) {
		super();
		this.id = id;
		this.hiringManagerName = hiringManagerName;
		this.hiringManagerId = hiringManagerId;
		this.recruiterName = recruiterName;
		this.recruiterId = recruiterId;
		this.candidateName = candidateName;
		this.candidateEmailId = candidateEmailId;
		this.candidateMobileNo = candidateMobileNo;
		this.candidateExperience = candidateExperience;
		this.technology = technology;
		this.interviewDate = interviewDate;
		this.requestedDate = requestedDate;
		this.requirementId = requirementId;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHiringManagerName() {
		return hiringManagerName;
	}

	public void setHiringManagerName(String hiringManagerName) {
		this.hiringManagerName = hiringManagerName;
	}

	public String getHiringManagerId() {
		return hiringManagerId;
	}

	public void setHiringManagerId(String hiringManagerId) {
		this.hiringManagerId = hiringManagerId;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(String recruiterId) {
		this.recruiterId = recruiterId;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getCandidateEmailId() {
		return candidateEmailId;
	}

	public void setCandidateEmailId(String candidateEmailId) {
		this.candidateEmailId = candidateEmailId;
	}

	public String getCandidateMobileNo() {
		return candidateMobileNo;
	}

	public void setCandidateMobileNo(String candidateMobileNo) {
		this.candidateMobileNo = candidateMobileNo;
	}

	public String getCandidateExperience() {
		return candidateExperience;
	}

	public void setCandidateExperience(String candidateExperience) {
		this.candidateExperience = candidateExperience;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}

	@Override
	public String toString() {
		return "ScheduleRequest [id=" + id + ", hiringManagerName=" + hiringManagerName + ", hiringManagerId="
				+ hiringManagerId + ", recruiterName=" + recruiterName + ", recruiterId=" + recruiterId
				+ ", candidateName=" + candidateName + ", candidateEmailId=" + candidateEmailId + ", candidateMobileNo="
				+ candidateMobileNo + ", candidateExperience=" + candidateExperience + ", technology=" + technology
				+ ", interviewDate=" + interviewDate + ", requestedDate=" + requestedDate + ", requirementId="
				+ requirementId + "]";
	}

}