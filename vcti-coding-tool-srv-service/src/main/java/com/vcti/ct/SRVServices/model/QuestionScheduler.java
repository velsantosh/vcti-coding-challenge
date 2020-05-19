package com.vcti.ct.SRVServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
@Table("scheduled")
public class QuestionScheduler {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String qid;
	private @NonNull String challengeid;
	private @NonNull String assigneduid;
	private @NonNull String assigneruid;

	public QuestionScheduler() {
		super();
	}

	public QuestionScheduler(@NonNull String id, @NonNull String qid, @NonNull String assigneduid, @NonNull String assigneruid, @NonNull String challengeid) {
		super();
		this.id = id;
		this.qid = qid;
		this.assigneduid = assigneduid;
		this.assigneruid = assigneruid;
		this.challengeid = challengeid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getAssigneduid() {
		return assigneduid;
	}

	public void setAssigneduid(String assigneduid) {
		this.assigneduid = assigneduid;
	}

	public String getAssigneruid() {
		return assigneruid;
	}

	public void setAssigneruid(String assigneruid) {
		this.assigneruid = assigneruid;
	}

	public String getChallengeid() {
		return challengeid;
	}

	public void setChallengeid(String challengeid) {
		this.challengeid = challengeid;
	}

	
}