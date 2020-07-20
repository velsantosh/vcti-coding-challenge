package com.vcti.ct.CCTServices.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
public class ValidateObjQuestions {
	private @NonNull String userId;

	/* List of ObjQResponse */
	List<QuesResponse> responseList;

	public String getUserId() {
		return userId;
	}

	public List<QuesResponse> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<QuesResponse> responseList) {
		this.responseList = responseList;
	}

}