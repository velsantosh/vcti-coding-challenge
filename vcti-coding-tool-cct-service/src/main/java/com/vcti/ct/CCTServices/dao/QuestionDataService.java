package com.vcti.ct.CCTServices.dao;

import java.util.List;
import java.util.Optional;

import com.vcti.ct.CCTServices.model.QuesResponse;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.QuestionTemplate;
import com.vcti.ct.CCTServices.model.Technology;
import com.vcti.ct.CCTServices.model.TechnologyMap;
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

	public Question addObjQuestion(QuestionBase newQ);

	public Question addSubQuestion(QuestionBase newQ);

	default Boolean[] executeQuery(List<String> list) {
		return new Boolean[] { false };
	}

	Technology addTechnology(Technology technology);

	Technology updateTechnology(QuestionBase newQ);

	List<Technology> getTechnology(String tname);

	List<TechnologyMap> getAllTechnology();

	List<TechnologyMap> getTechnologyByKey(String tname);

	List<QuestionBase> getAllQuestionsByTypeAndTname(String type, String tname);

	List<QuestionBase> getAllQuestionsByTechDifficultyAndExp(String tech, String difficulty, String exp);

	List<QuestionBase> getAllQuestionsByType(String type);

	List<QuestionBase> getAllQuestionsByTname(String tname);

	Question updateSubQuestion(QuestionBase newQues, String id);

	Question updateObjQuestion(QuestionBase newQues, String id);

	Question updateQuestions(QuestionBase newQues, String id);

	QuesResponse runSubjQuesTestCode(ValidateSubjQuestions validateSubjQ);

	List<QuestionTemplate> getAllQuestionTemplate();

	List<QuestionTemplate> getAllQuestionTemplateByTech(String technology);

	List<QuestionTemplate> getAllQuestionTemplateByExp(String experience);

	QuestionTemplate getQuestionTemplateByName(String templateName);

	QuestionTemplate addQuestionTemplate(QuestionTemplate questionTemplate);

	QuestionTemplate updateQuestions(QuestionTemplate questionTemplate, String id);

	String deleteQuestionTemplate(String questionTemplateId);

	List<QuestionTemplate> getFilteredTemplates(String tech, String difficulty, String experience);

	Optional<QuestionTemplate> getTemplate(String templateId);

	List<QuestionBase> getAllQuestsByTemplateId(String templateId);

	QuestionTemplate updateQuestionTemplate(QuestionTemplate questTemplateData, String id);

}
