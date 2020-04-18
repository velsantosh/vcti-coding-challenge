package com.vcti.ct.CCTServices.dao;

import java.util.List;

import com.vcti.ct.CCTServices.model.QuesResponse;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.ValidateSubjQuestions;

public interface QuestionDataService {
	Question addQuestion(QuestionBase newQ);

	String deleteQuestion(String qId);

	void updateQuestion(QuestionBase newQues, String qId);

	QuestionBase getQuestion(String qId);

	List<QuestionBase> getQuestions();

	List<QuesResponse> validateObjQues(List<QuesResponse> list);

	List<QuestionBase> getQuestionsByType(String type);

	QuesResponse validateSubjQues(ValidateSubjQuestions validateSubjQ);

	default Boolean[] executeQuery(List<String> list) {
		return new Boolean[] { false };
	}

}
