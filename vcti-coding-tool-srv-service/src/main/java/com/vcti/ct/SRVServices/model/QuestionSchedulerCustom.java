package com.vcti.ct.SRVServices.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class QuestionSchedulerCustom {
	private String id;
	private @NonNull List<String> qidList;
	private @NonNull List<String> assigneduidList;
	private @NonNull String assigneruid;
	private @NonNull String challengeid;
	private @NonNull Date scheduleTime;
	private @NonNull String status;

	public QuestionSchedulerCustom() {
		super();
	}

	public QuestionSchedulerCustom(String id, @NonNull List<String> qidList, @NonNull List<String> assigneduidList,
			@NonNull String assigneruidList, @NonNull String challengeid, @NonNull Date scheduleTime, @NonNull String status) {
		super();
		this.id = id;
		this.qidList = qidList;
		this.assigneduidList = assigneduidList;
		this.assigneruid = assigneruidList;
		this.challengeid = challengeid;
		this.scheduleTime = scheduleTime;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getQidList() {
		return qidList;
	}

	public void setQidList(List<String> qidList) {
		this.qidList = qidList;
	}

	public List<String> getAssigneduidList() {
		return assigneduidList;
	}

	public void setAssigneduidList(List<String> assigneduidList) {
		this.assigneduidList = assigneduidList;
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

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}