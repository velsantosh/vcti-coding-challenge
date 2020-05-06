package com.vcti.ct.SRVServices.dao;

import java.io.FileNotFoundException;
import java.util.List;

import com.vcti.ct.SRVServices.model.CandidateResult;
import com.vcti.ct.SRVServices.model.Interviewer;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
import com.vcti.ct.SRVServices.model.ScheduledRequest;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;
import com.vcti.ct.SRVServices.model.User;

import net.sf.jasperreports.engine.JRException;

public interface SRVDataService {

	boolean assignUser(QuestionScheduler assignQ);

	boolean unAssignUser(QuestionScheduler unAssignQ);

	Boolean bulkAssignUser(QuestionSchedulerCustom assignBulkQ);

	Boolean bulkUnAssignUser(QuestionSchedulerCustom unAssignBulkQ);

	List<QuestionScheduler> getQuestions();

	List<QuestionScheduler> getQuestions(String qId);

	List<QuestionScheduler> getQuestions(QuestionScheduler assignQ);

	// Objective Q Result
	boolean addObjQResult(ObjQuestionResult objQRes);

	boolean removeObjQResult(ObjQuestionResult objQRes);

	List<ObjQuestionResult> getObjQResult();

	List<ObjQuestionResult> getObjQResult(ObjQuestionResult objQResult);

	List<ObjQuestionResult> getObjQResultByUserId(String userId);

	List<ObjQuestionResult> getObjQResultByQId(String qId);

	// Subjective Q Result
	boolean addSubjQResult(SubjQuestionResult subjQRes);

	boolean removeSubjQResult(SubjQuestionResult subjQRes);

	List<SubjQuestionResult> getSubjQResult();

	List<SubjQuestionResult> getSubjQResult(SubjQuestionResult subjQResult);

	List<SubjQuestionResult> getSubjQResultByUserId(String userId);

	List<SubjQuestionResult> getSubjQResultByQId(String qId);

	List<QuestionSchedView> getQuestionsByUserId(String userId);

	boolean addObjQResultList(List<ObjQuestionResult> objQResList);

	boolean addSubjQResultList(List<SubjQuestionResult> subjQResList);

	List<ScheduledRequest> scheduleRequest(List<ScheduledRequest> scheduleRequest);

	List<ScheduledRequest> getAllScheduledRequest();

	List<ScheduledRequest> rescheduleRequest(List<ScheduledRequest> scheduleRequest);

	List<ScheduledRequest> cancelScheduleRequest(List<String> id);
	byte[] getSubjObjResultReport(String format);

	List<CandidateResult> getCandidateReports();

	List<String> candidateSendEmail();
	
	List<QuestionSchedView> getQuestionsByAssignerId(String assignerId);

	List<String> sendCandidateReport(Interviewer interviewer);

}
