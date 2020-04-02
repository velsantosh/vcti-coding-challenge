package com.vcti.ct.SRVServices.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.vcti.ct.SRVServices.dao.SRVDataService;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionBase;
import com.vcti.ct.SRVServices.model.QuestionCustom;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
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

	@PostMapping("/bulkAssignUser")
	public Boolean bulkAssignUser(@RequestBody QuestionSchedulerCustom assignBulkQ) {
		return srvDataService.bulkAssignUser(assignBulkQ);
	}

	@PostMapping("/unAssignUser")
	public Boolean unAssignUser(@RequestBody QuestionScheduler unAssignQ) {
		return srvDataService.unAssignUser(unAssignQ);
	}

	@PostMapping("/bulkUnAssignUser")
	public Boolean bulkUnAssignUser(@RequestBody QuestionSchedulerCustom unAssignBulkQ) {
		return srvDataService.bulkUnAssignUser(unAssignBulkQ);
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

	// Get Q List on User
	@GetMapping("/schQuesByUid/{userId}")
	public List<QuestionCustom> getAllSchQuestionsByUserId(@PathVariable String userId) {
		List<QuestionSchedView> quesIdList = srvDataService.getQuestionsByUserId(userId);
		return getQuestionListFromCCTService(quesIdList);
	}

	@GetMapping("/schObjQuesByUid/{userId}")
	public List<QuestionCustom> getSchObjQuestionsByUserId(@PathVariable String userId) {
		List<QuestionSchedView> quesIdList = srvDataService.getQuestionsByUserId(userId);
		return getQuestionListFromCCTService(quesIdList);
	}

	private List<QuestionCustom> getQuestionListFromCCTService(List<QuestionSchedView> quesIdList) {
		List<QuestionCustom> questionList = new ArrayList<QuestionCustom>();
		for (QuestionSchedView qId : quesIdList) {
			String id = qId.getQid();
			// TODO move this to application.properties as connection point to CCT Service
			final String uri = "http://localhost:8082/question/" + id;

			RestTemplate restTemplate = new RestTemplate();
			QuestionBase result = restTemplate.getForObject(uri, QuestionBase.class);
			QuestionCustom customObj = new QuestionCustom(result.getId(), result.getType(), result.getStatement(),
					result.getOptions(), result.getCorrect_option(), result.getMethodName());

			questionList.add(customObj);
		}

		return questionList;
	}

	// Objective Q Result URI
	@PostMapping(value = "/addObjRes")
	public boolean addObjResult(@RequestBody ObjQuestionResult objQRes) {
		return srvDataService.addObjQResult(objQRes);
	}

	@PostMapping(value = "/addObjResList")
	public boolean addObjResultList(@RequestBody List<ObjQuestionResult> objQResList) {
		return srvDataService.addObjQResultList(objQResList);
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

	@PostMapping(value = "/addSubQResList")
	public boolean addSubjResultList(@RequestBody List<SubjQuestionResult> subjQResList) {
		return srvDataService.addSubjQResultList(subjQResList);
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