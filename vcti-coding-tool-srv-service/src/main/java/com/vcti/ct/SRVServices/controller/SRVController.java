package com.vcti.ct.SRVServices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vcti.ct.SRVServices.dao.SRVDataService;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;

@RestController
public class SRVController {

	@Autowired
	private SRVDataService srvDataService;

	// Scheduler URI

	@PostMapping("/assignUser")
	public Boolean assignUser(@RequestBody QuestionScheduler assignQ) {
		return srvDataService.assignUser(assignQ);
	}

	@PostMapping("/unAssignUser")
	public Boolean unAssignUser(@RequestBody QuestionScheduler unAssignQ) {
		return srvDataService.unAssignUser(unAssignQ);
	}

	@GetMapping("/schQues")
	public List<QuestionScheduler> getQuestions() {
		return srvDataService.getQuestions();
	}

	@GetMapping("/schQues/{qId}")
	public List<QuestionScheduler> getQuestion(@PathVariable String qId) {
		return srvDataService.getQuestions(qId);
	}

	@PostMapping(value = "/schQues")
	public List<QuestionScheduler> getQuestions(@RequestBody QuestionScheduler schQues) {
		return srvDataService.getQuestions(schQues);
	}

	// Objective Q Result URI
	@PostMapping(value = "/addObjRes")
	public boolean addObjResult(@RequestBody ObjQuestionResult objQRes) {
		return srvDataService.addObjQResult(objQRes);
	}

	@PostMapping(value = "/removeObjRes")
	public boolean removeObjResult(@RequestBody ObjQuestionResult objQRes) {
		return srvDataService.removeObjQResult(objQRes);
	}

	@GetMapping("/objRes")
	public List<ObjQuestionResult> getObjQResult() {
		return srvDataService.getObjQResult();
	}

	@PostMapping("/objRes")
	public List<ObjQuestionResult> getObjQResult(@RequestBody ObjQuestionResult objQResult) {
		return srvDataService.getObjQResult(objQResult);
	}

	@GetMapping("/objResByUserId/{userId}")
	public List<ObjQuestionResult> getObjQResultByUserId(@PathVariable String userId) {
		return srvDataService.getObjQResultByUserId(userId);
	}

	@GetMapping("/objResByQId/{qId}")
	public List<ObjQuestionResult> getObjQResultByQId(@PathVariable String qId) {
		return srvDataService.getObjQResultByQId(qId);
	}

	// Subjective Q Result URI
	@PostMapping(value = "/addSubjRes")
	public boolean addSubjResult(@RequestBody SubjQuestionResult subjQRes) {
		return srvDataService.addSubjQResult(subjQRes);
	}

	@PostMapping(value = "/removeSubjRes")
	public boolean removeSubjResult(@RequestBody SubjQuestionResult subjQRes) {
		return srvDataService.removeSubjQResult(subjQRes);
	}

	@GetMapping("/subjRes")
	public List<SubjQuestionResult> getSubjQResult() {
		return srvDataService.getSubjQResult();
	}

	@PostMapping("/subjRes")
	public List<SubjQuestionResult> getSubjQResult(@RequestBody SubjQuestionResult subjQResult) {
		return srvDataService.getSubjQResult(subjQResult);
	}

	@GetMapping("/subjResByUserId/{userId}")
	public List<SubjQuestionResult> getSubjQResultByUserId(@PathVariable String userId) {
		return srvDataService.getSubjQResultByUserId(userId);
	}

	@GetMapping("/subjResByQId/{qId}")
	public List<SubjQuestionResult> getSubjQResultByQId(@PathVariable String qId) {
		return srvDataService.getSubjQResultByQId(qId);
	}
}