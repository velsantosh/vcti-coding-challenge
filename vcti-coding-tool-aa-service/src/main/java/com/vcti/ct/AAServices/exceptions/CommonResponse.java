package com.vcti.ct.AAServices.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author sandeepkumar.yadav
 *
 */
public class CommonResponse {

	private HttpStatus status;
	private String message;
	private String error;
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
	
}
