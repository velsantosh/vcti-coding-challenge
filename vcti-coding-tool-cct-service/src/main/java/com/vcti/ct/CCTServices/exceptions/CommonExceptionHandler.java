package com.vcti.ct.CCTServices.exceptions;

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

	@ExceptionHandler(InvalidQuestionIdException.class)
	public final ResponseEntity<CommonResponse> invalidQuestionIdExcetpion(InvalidQuestionIdException ex) {
		CommonResponse response = new CommonResponse();
		response.setStatus(HttpStatus.NOT_FOUND);
		response.setMessage(ex.getMessage());
		response.setError(ex.toString());
		return new ResponseEntity<CommonResponse>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(QuestionNotFoundExcetion.class)
	public final ResponseEntity<CommonResponse> questionNotFoundException(QuestionNotFoundExcetion ex) {
		CommonResponse response = new CommonResponse();
		response.setStatus(HttpStatus.NOT_FOUND);
		response.setMessage(ex.getMessage());
		response.setError(ex.toString());
		return new ResponseEntity<CommonResponse>(response, HttpStatus.NO_CONTENT);
	}
	
	
	@ExceptionHandler(QuestionAlreadyExistsException.class)
	public final ResponseEntity<CommonResponse> questionAlreadyExistException(QuestionAlreadyExistsException ex) {
		CommonResponse response = new CommonResponse();
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage(ex.getMessage());
		response.setError(ex.toString());
		return new ResponseEntity<CommonResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidQuestionTypeExceptoin.class)
	public final ResponseEntity<CommonResponse> invalidInputTypeException(InvalidQuestionTypeExceptoin ex) {
		CommonResponse response = new CommonResponse();
		response.setStatus(HttpStatus.NOT_FOUND);
		response.setMessage(ex.getMessage());
		response.setError(ex.toString());
		return new ResponseEntity<CommonResponse>(response, HttpStatus.NOT_FOUND);
	}
}
