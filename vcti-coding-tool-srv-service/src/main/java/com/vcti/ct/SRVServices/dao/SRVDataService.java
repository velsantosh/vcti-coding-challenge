package com.vcti.ct.SRVServices.dao;

import java.util.List;

import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;

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

}
