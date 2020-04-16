package com.vcti.ct.CCTServices.exceptions;

/**
 * @author sandeepkumar.yadav
 *
 */
public class QuestionAlreadyExistsException extends RuntimeException {


	private static final long serialVersionUID = 1L;
	
	private String errorMsg;

	public QuestionAlreadyExistsException(String errorMsg) {
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
