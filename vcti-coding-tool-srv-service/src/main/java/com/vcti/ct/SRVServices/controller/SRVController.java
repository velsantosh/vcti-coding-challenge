package com.vcti.ct.SRVServices.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.vcti.ct.SRVServices.dao.SRVDataService;
import com.vcti.ct.SRVServices.model.CandidateResult;
import com.vcti.ct.SRVServices.model.Interviewer;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionBase;
import com.vcti.ct.SRVServices.model.QuestionCustom;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
import com.vcti.ct.SRVServices.model.ScheduleChallenge;
import com.vcti.ct.SRVServices.model.ScheduledRequest;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;
import com.vcti.ct.SRVServices.model.SubjQuestionResultPojo;

@CrossOrigin(origins = { "*","https://vcct.blr.velankani.com:3000", "http://localhost:3000" })
@RestController
public class SRVController {

	@Autowired
	private SRVDataService srvDataService;

	@Autowired
	private RestTemplate restTemplate;

	// Scheduler URI

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck() {
		return "{ \"isWorking\" : true }";
	}

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
	@GetMapping("/schQuesByUid/")
	public List<QuestionCustom> getAllSchQuestionsByUserId(@RequestParam (value = "userId", required = true) String userId) {
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
			srvDataService.callCCTService(questionList, id);
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

	@GetMapping("/objResByUserId/{userId:.+}")
	public List<ObjQuestionResult> getObjQResultByUserId(@PathVariable String userId) {
		return srvDataService.getObjQResultByUserId(userId);
	}

	@GetMapping("/objResByQId/{qId}")
	public List<ObjQuestionResult> getObjQResultByQId(@PathVariable String qId) {
		return srvDataService.getObjQResultByQId(qId);
	}

	// Subjective Q Result URI
	@PostMapping(value = "/addSubjRes")
	public boolean addSubjResult(@RequestBody SubjQuestionResultPojo subjQRes) {
		return srvDataService.addSubjQResult(subjQRes);
	}

	@PostMapping(value = "/addSubQResList")
	public boolean addSubjResultList(@RequestBody List<SubjQuestionResultPojo> subjQResList) {
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

	@PostMapping("/schedule/request")
	public List<ScheduledRequest> scheduleRequest(@RequestBody List<ScheduledRequest> scheduleRequest) {
		return srvDataService.scheduleRequest(scheduleRequest);
	}

	@GetMapping("/schedule/request")
	public List<ScheduledRequest> getAllScheduledRequest() {
		return srvDataService.getAllScheduledRequest();
	}

	@PutMapping("/reschedule/request")
	public List<ScheduledRequest> updateScheduleRequest(@RequestBody List<ScheduledRequest> scheduleRequest) {
		return srvDataService.rescheduleRequest(scheduleRequest);
	}

	@PutMapping("/update/scheduleddata/{assigneduid}/{videoStreamFlag}")
	public boolean updateScheduleChallenge(@PathVariable String assigneduid, @PathVariable String videoStreamFlag) {
		return srvDataService.updateScheduleChallenge(assigneduid, videoStreamFlag);
		// ScheduledRequest scheduleRequest,
	}

	@GetMapping("/schedule/videostream/")
	public List<ScheduleChallenge> getAllVideoStreamingCandidateData(@RequestParam (value = "userId", required = true) String assigneruid) {
		return srvDataService.getAllVideoStreamingCandidateData(assigneruid);
	}

	@DeleteMapping("/cancel/schedule/request")
	public List<ScheduledRequest> deleteScheduleRequest(@RequestBody List<String> scheduleRequestIds) {
		return srvDataService.cancelScheduleRequest(scheduleRequestIds);
	}

	@GetMapping("/subjResReport/{id}/{challengeid}")
	public byte[] getSubjObjResultReport(@PathVariable String id, @PathVariable String challengeid) {
		return srvDataService.getSubjObjResultReport(id, challengeid);
	}

	@GetMapping("/candidateReport/")
	public List<CandidateResult> getCandidateReports(@RequestParam (value = "candidateId", required = true) String id) {
		return srvDataService.getCandidateReports(id);
	}

	@GetMapping("/send/testlink")
	public List<String> sendTestLinkToCandidates() {
		return srvDataService.sendTestLinkToCandidates();
	}

	@GetMapping("/schQuesByassignerId/{assignerId}")
	public List<QuestionCustom> getSchQuestionsByAssignerId(@PathVariable String assignerId) {
		List<QuestionSchedView> quesIdList = srvDataService.getQuestionsByAssignerId(assignerId);
		return getQuestionListFromCCTService(quesIdList);
	}

	@PostMapping("/send/candidate/report/{challengeid}")
	public List<String> sendCandidateReport(@PathVariable String challengeid, @RequestBody Interviewer interviewer) {
		return srvDataService.sendCandidateReport(interviewer, challengeid);
	}

	@GetMapping("/send/testlink/now")
	public List<String> sendEamilToCandidateForTestLink(@RequestBody List<String> candidateEmailList) {
		return srvDataService.sendEamilToCandidateForTestLink(candidateEmailList);
	}

	@GetMapping("/chlngRecByassignerId/")
	@ResponseBody
	public List<ScheduleChallenge> getChallengeRecByAssignerId(@RequestParam (value = "assignerId", required = true)  String assignerId) {
		List<ScheduleChallenge> quesIdList = srvDataService.getChallengeRecByAssignerId(assignerId);
		
		return quesIdList;
	}

	@DeleteMapping(value = "/challenge/{challengeId}", produces = "application/json; charset=utf-8")
	public String deleteChallenge(@PathVariable String challengeId) {
		return srvDataService.deleteChallenge(challengeId);
	}

	@PutMapping("/updateChallenge")
	public ScheduleChallenge updateUser(@RequestBody QuestionSchedulerCustom assignBulkQ) {
		return srvDataService.updateChallenge(assignBulkQ);
	}

	@GetMapping("/schQuesBychallengeId/{challengeId}")
	public List<QuestionCustom> getQuestionsByChallengeId(@PathVariable String challengeId) {
		List<QuestionScheduler> quesIdList = srvDataService.getQuestionsByChallengeId(challengeId);
		return srvDataService.getQuestionListFromCCT(quesIdList);
	}



	@GetMapping("/schQuesNotBychallengeId/{assigneduid}/{challengeId}")
	public List<QuestionCustom> getQuestionsNotByChallengeId(@PathVariable String assigneduid,
			@PathVariable String challengeId) {
		List<QuestionSchedView> quesIdList = srvDataService.getQuestionsNotByChallengeId(assigneduid, challengeId);
		return getQuestionListFromCCTService(quesIdList);
	}

	@GetMapping("/schQuesByCandidate/")
	public List<QuestionCustom> getAllSchQuestionsByCandidate(@RequestParam (value = "userId", required = true) String candidateId) {
		List<QuestionScheduler> quesIdList = srvDataService.getQuestionsByCandidateId(candidateId);
		return srvDataService.getQuestionListFromCCT(quesIdList);
	}

	@PutMapping("/updateChallengeStatus/")
	public boolean updateChallengeStatus(@RequestParam (value = "candidateId", required = true) String candidateId) {
		return srvDataService.updateChallengeStatus(candidateId);
	}

	@PostMapping("/assignTemplates")
	public Boolean assignTemplates(@RequestBody QuestionSchedulerCustom assignBulkQ) {
		return srvDataService.assignDynamicTemplate(assignBulkQ);
	}

	@PutMapping("/updateChallengeTemplate")
	public ScheduleChallenge updateChallengeWithTemplate(@RequestBody QuestionSchedulerCustom assignBulkQ) {
		return srvDataService.updateChallengeWithTemplate(assignBulkQ);
	}

	@PostMapping("/customTemplates")
	public Boolean customTemplates(@RequestBody QuestionSchedulerCustom assignBulkQ) {
		return srvDataService.createCustomTemplate(assignBulkQ);
	}
}