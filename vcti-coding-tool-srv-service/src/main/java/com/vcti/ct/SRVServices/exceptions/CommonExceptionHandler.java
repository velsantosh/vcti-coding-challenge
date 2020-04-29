package com.vcti.ct.SRVServices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sandeepkumar.yadav
 *
 */
@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(InvalidScheduleRequestIdException.class)
	public final ResponseEntity<CommonResponse> invalidQuestionIdExcetpion(InvalidScheduleRequestIdException ex) {
		CommonResponse response = new CommonResponse();
		response.setStatus(HttpStatus.NOT_FOUND);
		response.setMessage(ex.getMessage());
		response.setError(ex.toString());
		return new ResponseEntity<CommonResponse>(response, HttpStatus.NOT_FOUND);
	}
}
