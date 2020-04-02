package com.vcti.ct.CCTServices.dao;

import java.util.List;
import java.util.Map;

import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;

public interface QuestionDataService {
	Question addQuestion(QuestionBase newQ);

	String deleteQuestion(String qId);

	void updateQuestion(QuestionBase newQues, String qId);

	QuestionBase getQuestion(String qId);

	List<QuestionBase> getQuestions();

	Map<String, Boolean> validateObjQues(Map<String, String> testObj);

	List<QuestionBase> getQuestionsByType(String type);

}
