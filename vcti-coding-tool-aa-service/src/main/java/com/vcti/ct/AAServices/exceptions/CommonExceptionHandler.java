package com.vcti.ct.AAServices.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice
public class CommonExceptionHandler {
	
//	@ExceptionHandler(Exception.class)
	public String genericExceptionHandler(Exception exe) {
		return exe.getMessage();
	}
}
