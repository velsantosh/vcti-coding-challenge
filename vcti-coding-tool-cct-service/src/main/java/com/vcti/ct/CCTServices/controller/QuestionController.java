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
import com.vcti.ct.CCTServices.model.QuesResponse;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.Technology;
import com.vcti.ct.CCTServices.model.TechnologyMap;
import com.vcti.ct.CCTServices.model.ValidateObjQuestions;
import com.vcti.ct.CCTServices.model.ValidateSubjQuestions;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class QuestionController {

	@Autowired
	private QuestionDataService questionDataService;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck() {
		return "{ \"isWorking\" : true }";
	}
	
	@PostMapping("/question")
	public Question addQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addQuestion(newQ);
	}
	
	@PostMapping("/add/obj/question")
	public Question addObjQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addObjQuestion(newQ);
	}
	
	@PostMapping("/add/sub/question")
	public Question addSubQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addSubQuestion(newQ);
	}

	@PostMapping("/add/technology")
	public Technology addTechnology(@RequestBody Technology technology) {
		return questionDataService.addTechnology(technology);
	}
	
	@GetMapping("/technology/name/{tname}")
	public List<Technology> getTechnologyByTname(@PathVariable String tname) {
		return questionDataService.getTechnology(tname);
	}
	
	@GetMapping("/technology/key/{tname}")
	public List<TechnologyMap> getTechnologyByKey(@PathVariable String tname) {
		return questionDataService.getTechnologyByKey(tname);
	}
	
	@GetMapping("/technologies")
	public List<TechnologyMap> getAllTechnology() {
		return questionDataService.getAllTechnology();
	}
	
	@GetMapping("/questions")
	public List<QuestionBase> getQuestions() {
		return questionDataService.getQuestions();
	}
	
	@GetMapping("/questions/type/{type}/tech/{tname}")
	public List<QuestionBase> getAllQuestionsByTypeAndTname(@PathVariable String type, @PathVariable String tname) {
		return questionDataService.getAllQuestionsByTypeAndTname(type, tname);
	}
	
	@GetMapping("/questions/type/{type}")
	public List<QuestionBase> getAllQuestionsByType(@PathVariable String type) {
		return questionDataService.getAllQuestionsByType(type);
	}
	
	@GetMapping("/questions/tech/{tname}")
	public List<QuestionBase> getAllQuestionsByTname(@PathVariable String tname) {
		return questionDataService.getAllQuestionsByTname(tname);
	}

	@GetMapping("/question/{id}")
	public QuestionBase getQuestion(@PathVariable String id) {
		return questionDataService.getQuestion(id);
	}

	@GetMapping("/questionsByType/{type}")
	public List<QuestionBase> getQuestionsByType(@PathVariable String type) {
		return questionDataService.getQuestionsByType(type);
	}

	@DeleteMapping(value = "/question/{id}", produces = "application/json; charset=utf-8")
	public String deleteQuetion(@PathVariable String id) {
		return questionDataService.deleteQuestion(id);
	}

	@PutMapping("/update/sub/question/{id}")
	public Question updateSubQuestion(@RequestBody QuestionBase newQues, @PathVariable String id) {
		return questionDataService.updateSubQuestion(newQues, id);
	}
	
	@PutMapping("/update/obj/question/{id}")
	public Question updateObjQuestion(@RequestBody QuestionBase newQues, @PathVariable String id) {
		return questionDataService.updateObjQuestion(newQues, id);
	}
	
	@PutMapping("/question/{id}")
	public Question updateQuestion(@RequestBody QuestionBase newQues, @PathVariable String id) {
		return questionDataService.updateQuestions(newQues, id);
	}

	@PostMapping("/validateObjQues")
	public List<QuesResponse> validateObjQues(@RequestBody ValidateObjQuestions validateObjQ) {
		return questionDataService.validateObjQues(validateObjQ.getResponseList());
	}

	@PostMapping("/validateSubjQues")
	public QuesResponse validateSubjQues(@RequestBody ValidateSubjQuestions validateSubjQ) {
		return questionDataService.validateSubjQues(validateSubjQ);
	}
}