package com.vcti.ct.CCTServices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vcti.ct.CCTServices.dao.QuestionDataService;
import com.vcti.ct.CCTServices.model.DBQuery;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * @author sandeepkumar.yadav
 *
 */
@CrossOrigin(origins = { "http://cct:8089","http://localhost:3000" })
@RestController
public class DBController {

	@Autowired
	private QuestionDataService questionDataService;

	@PostMapping("/execute/query")
	public Boolean[] executeQuery(@RequestBody DBQuery queries) {
		return questionDataService.executeQuery(queries.getQueries());
	}

}
