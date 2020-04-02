package com.vcti.ct.CCTServices.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vcti.ct.CCTServices.config.CCTConstants;
import com.vcti.ct.CCTServices.dao.QuestionDataService;
import com.vcti.ct.CCTServices.model.ObjQuestion;
import com.vcti.ct.CCTServices.model.Options;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.SubjQuestion;
import com.vcti.ct.CCTServices.repository.ObjQuestionRepository;
import com.vcti.ct.CCTServices.repository.OptionsRepository;
import com.vcti.ct.CCTServices.repository.QuestionRepository;
import com.vcti.ct.CCTServices.repository.SubjQuestionRepository;

@Component
@Service
public class QuestionDataServiceImpl implements QuestionDataService, CCTConstants {
	@Autowired
	QuestionRepository questionRepository;
	@Autowired
	ObjQuestionRepository objQRepository;
	@Autowired
	SubjQuestionRepository subjQRepository;
	@Autowired
	OptionsRepository optionsRepository;

	@Override
	public Question addQuestion(QuestionBase newQ) {

		if (newQ.getType().equals(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
			System.out.println("Adding Objective Question...");
			// Update Options Table
			updateOptionsTable(newQ);
			// Update ObjectiveQ Table
			updateObjQuestionTable(newQ);

		} else {
			System.out.println("Adding Subjective Question...");
			// Update SubjectiveQ Table
			updateSubjQuestionTable(newQ);
		}
		// Update Questions Table
		updateQuestionBaseTable(newQ);
		return null;
	}

	private void updateQuestionBaseTable(QuestionBase newQ) {
		Question question = new Question(newQ.getId(), newQ.getLanguage(), newQ.getType(), newQ.getExperience(),
				newQ.getCreatedUserid());
		questionRepository.save(question);
	}

	private void updateSubjQuestionTable(QuestionBase newQ) {
		SubjQuestion subQ = new SubjQuestion(newQ.getId(), newQ.getStatement(), newQ.getMethodName(),
				newQ.getJunitObj());
		subjQRepository.save(subQ);
	}

	private void updateObjQuestionTable(QuestionBase newQ) {
		ObjQuestion objQ = new ObjQuestion(newQ.getId(), newQ.getStatement(), null, newQ.getCorrectOption());
		objQRepository.save(objQ);
	}

	private void updateOptionsTable(QuestionBase newQ) {
		List<String> optionsList = newQ.getOptions();
		List<Options> optionObjList = new ArrayList<Options>();
		Options optionObj = null;
		for (String options : optionsList) {
			optionObj = new Options(UUID.randomUUID().toString(), newQ.getId(), options);
			optionObjList.add(optionObj);
		}
		optionsRepository.saveAll(optionObjList);
	}

	@Override
	public String deleteQuestion(String qId) {
		Optional<Question> question = questionRepository.findById(qId);
		Boolean result = question.isPresent();
		if (result) {
			Question ques = question.get();
			questionRepository.deleteById(qId);
			if (ques.getType().equals(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
				objQRepository.deleteById(qId);
				List<Options> optionList = optionsRepository.findByqId(qId);
				optionList.forEach(option -> optionsRepository.deleteById(option.getId()));
			} else {
				subjQRepository.deleteById(qId);
			}
		}
		return "{ \"success\" : " + (result ? "true" : "false") + " }";
	}

	@Override
	public QuestionBase getQuestion(String qId) {
		QuestionBase qBase = new QuestionBase();
		Optional<Question> question = questionRepository.findById(qId);
		if (question.isPresent()) {
			Question ques = question.get();
			// Question Base Table
			populateQBaseTable(qBase, ques);
			updateObjOrSubQtable(qBase, ques);
		} else {
			System.out.println("No Question found");
		}
		return qBase;
	}

	@Override
	public List<QuestionBase> getQuestions() {
		Iterable<Question> questionParentList = questionRepository.findAll();
		return getQuestionList(questionParentList);
	}

	@Override
	public List<QuestionBase> getQuestionsByType(String type) {
		Iterable<Question> questionParentList = questionRepository.findByType(type);
		return getQuestionList(questionParentList);
	}

	private List<QuestionBase> getQuestionList(Iterable<Question> questionParentList) {
		List<QuestionBase> questionList = new ArrayList<QuestionBase>();

		for (Question question : questionParentList) {
			QuestionBase qBase = new QuestionBase();
			// Question Base Table
			populateQBaseTable(qBase, question);

			// Update Questions Obj or Subj
			updateObjOrSubQtable(qBase, question);

			questionList.add(qBase);
		}
		return questionList;
	}

	private void updateObjOrSubQtable(QuestionBase qBase, Question question) {
		if (question.getType().equals(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
			fetchObjQRepoParameters(question.getId(), qBase);
		} else {
			fetchSubQRepoParameters(question.getId(), qBase);
		}
	}

	private void fetchSubQRepoParameters(String id, QuestionBase qBase) {
		Optional<SubjQuestion> subjQues = subjQRepository.findById(id);
		if (subjQues.isPresent()) {
			SubjQuestion subjQ = subjQues.get();
			// Subjective Question Table
			populateSubjQTable(qBase, subjQ);

		}
	}

	private void fetchObjQRepoParameters(String id, QuestionBase qBase) {
		Optional<ObjQuestion> objQues = objQRepository.findById(id);
		if (objQues.isPresent()) {
			ObjQuestion objQ = objQues.get();
			// Objective Question Table
			populateObjQTable(qBase, objQ);
		}
	}

	private void populateSubjQTable(QuestionBase qBase, SubjQuestion subjQ) {
		qBase.setStatement(subjQ.getStatement());
		qBase.setMethodName(subjQ.getMethodName());
		qBase.setJunitObj(subjQ.getJunit());
	}

	private void populateObjQTable(QuestionBase qBase, ObjQuestion objQ) {
		qBase.setStatement(objQ.getStatement());
		qBase.setOptions(getOptionsList(objQ.getqId()));
		qBase.setCorrect_option(objQ.getCorrect_option());

	}

	private List<String> getOptionsList(String qId) {
		List<Options> optionsObjList = optionsRepository.findByqId(qId);
		List<String> optionList = new ArrayList<String>();
		for (Options optionsObj : optionsObjList) {
			optionList.add(optionsObj.getOptions());
		}
		return optionList;
	}

	private void populateQBaseTable(QuestionBase qBase, Question question) {
		qBase.setId(question.getId());
		qBase.setLanguage(question.getLanguage());
		qBase.setType(question.getType());
		qBase.setCreatedUserid(question.getCreatedUserid());

	}

	@Override
	public void updateQuestion(QuestionBase newQues, String qId) {
		Optional<Question> optionalQuestion = questionRepository.findById(qId);
		if (optionalQuestion.isPresent()) {
			Question modifQ = optionalQuestion.get();
			// Q Base Table
			modifQ.setLanguage(newQues.getLanguage());
			modifQ.setType(newQues.getType());
			modifQ.setCreatedUserid(newQues.getCreatedUserid());
			questionRepository.save(modifQ);

			if (modifQ.getType().equals(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
				Optional<ObjQuestion> objQues = objQRepository.findById(qId);
				if (objQues.isPresent()) {
					ObjQuestion objQ = objQues.get();
					objQ.setStatement(newQues.getStatement());
					objQ.setCorrect_option(newQues.getCorrectOption());
					objQRepository.save(objQ);
					// Delete rows from Options table
					List<Options> optionList = optionsRepository.findByqId(qId);
					optionList.forEach(option -> optionsRepository.deleteById(option.getId()));
					updateOptionsTable(newQues);
				}

			} else {// Subjective Question
				Optional<SubjQuestion> subjQues = subjQRepository.findById(qId);
				if (subjQues.isPresent()) {
					SubjQuestion subjQ = subjQues.get();
					subjQ.setStatement(newQues.getStatement());
					subjQ.setMethodName(newQues.getMethodName());
					subjQ.setJunit(newQues.getJunitObj());
					subjQRepository.save(subjQ);
				}
			}
		}
	}

	@Override
	public Map<String, Boolean> validateObjQues(Map<String, String> testObj) {
		// Result Map will store Qid as key and value as Status of selected option
		// whether correct or not
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		Iterable<ObjQuestion> objQList = objQRepository.findAllById(testObj.keySet());
		for (ObjQuestion objQFromDb : objQList) {
			if (testObj.containsKey(objQFromDb.getqId())) {
				resultMap.put(objQFromDb.getqId(),
						objQFromDb.getCorrect_option().equals(testObj.get(objQFromDb.getqId())));
			}
		}
		return resultMap;
	}
}
