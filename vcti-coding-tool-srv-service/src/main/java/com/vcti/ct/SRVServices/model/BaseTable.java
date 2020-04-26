package com.vcti.ct.SRVServices.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author sandeepkumar.yadav
 *
 */
public class BaseTable {

	private String createdDate;
	private String createdBy;
	private String updatedDate;
	private String updatedBy;
	
	public BaseTable() {
	}
	public BaseTable(String createdDate, String createdBy, String updatedDate, String updatedBy) {
		super();
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}
	public String getCreatedDate() {
		if(createdDate == null) {
			createdDate = getCurrentDate();
		}
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		if(createdBy == null) {
			createdBy = getName();
		}
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedDate() {
		if(updatedDate == null) {
			updatedDate = getCurrentDate();
		}
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedBy() {
		if(updatedBy == null) {
			updatedBy = getName();
		}
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	private String getCurrentDate() {
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - hh:mm:ss a");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		return d1.toString();
	}
	
	private String getName() {
		return "test.user@vspl.com";
	}
}
