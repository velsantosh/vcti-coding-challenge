package com.vcti.ct.CCTServices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vcti.ct.CCTServices.dao.QuestionDataService;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;

@RestController
public class QuestionController {

	@Autowired
	private QuestionDataService questionDataService;

	@PostMapping("/question")
	public Question addQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addQuestion(newQ);
	}

	@GetMapping("/questions")
	public List<QuestionBase> getQuestions() {
		return questionDataService.getQuestions();
	}

	@GetMapping("/question/{id}")
	public QuestionBase getQuestion(@PathVariable String id) {
		return questionDataService.getQuestion(id);
	}

	@DeleteMapping(value = "/question/{id}", produces = "application/json; charset=utf-8")
	public String deleteQuetion(@PathVariable String id) {
		return questionDataService.deleteQuestion(id);
	}

	@PutMapping("/question/{id}")
	public void updateUser(@RequestBody QuestionBase newQues, @PathVariable String id) {
		questionDataService.updateQuestion(newQues, id);
	}
}