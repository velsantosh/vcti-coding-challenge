package com.vcti.ct.SRVServices.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class QuestionSchedulerCustom {
	private String id;
	private @NonNull List<String> qidList;
	private @NonNull List<String> assigneduidList;
	private @NonNull String assigneruid;

	public QuestionSchedulerCustom() {
		super();
	}

	public QuestionSchedulerCustom(String id, @NonNull List<String> qidList, @NonNull List<String> assigneduidList,
			@NonNull String assigneruidList) {
		super();
		this.id = id;
		this.qidList = qidList;
		this.assigneduidList = assigneduidList;
		this.assigneruid = assigneruidList;
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

}