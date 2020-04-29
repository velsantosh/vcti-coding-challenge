package com.vcti.ct.SRVServices.exceptions;

/**
 * @author sandeepkumar.yadav
 *
 */
public class InvalidScheduleRequestIdException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String errorMsg;

	public InvalidScheduleRequestIdException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
