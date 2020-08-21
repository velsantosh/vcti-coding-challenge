package com.vcti.ct.SRVServices.model;

import java.util.Date;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Table("schedule_challenge")
public class ScheduleChallenge {
	@PrimaryKey
	private @NonNull String challengeid;
	private @NonNull String assigneduid;
	private @NonNull String assigneruid;
	private @NonNull String status;
	private @NonNull Date scheduleTime;
	private Date startTime;
	private Date endTime;
	private String templateId;
	private String templateType;
	private String videoStream;
	
	public ScheduleChallenge() {
		super();
	}

	public ScheduleChallenge(@NonNull String assigneduid, String videoStream) {
		super();
		this.assigneduid = assigneduid;
		this.videoStream = videoStream;
	}

	public ScheduleChallenge(@NonNull String challengeid, @NonNull String assigneduid, @NonNull String assigneruid,
			@NonNull String status, @NonNull Date scheduleTime, Date startTime, Date endTime, String templateId, String templateType) {
		super();
		this.challengeid = challengeid;
		this.assigneduid = assigneduid;
		this.assigneruid = assigneruid;
		this.status = status;
		this.scheduleTime = scheduleTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.templateId = templateId;
		this.templateType = templateType;
	}

	public String getChallengeid() {
		return challengeid;
	}

	public void setChallengeid(String challengeid) {
		this.challengeid = challengeid;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getVideoStream() {
		return videoStream;
	}

	public void setVideoStream(String videoStream) {
		this.videoStream = videoStream;
	}
	
	
}
