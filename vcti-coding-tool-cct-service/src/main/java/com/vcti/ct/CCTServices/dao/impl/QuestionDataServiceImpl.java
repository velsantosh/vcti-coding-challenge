package com.vcti.ct.CCTServices.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vcti.ct.CCTServices.config.CCTConstants;
import com.vcti.ct.CCTServices.dao.QuestionDataService;
import com.vcti.ct.CCTServices.model.ObjQuestion;
import com.vcti.ct.CCTServices.model.Param;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.SubjQuestion;
import com.vcti.ct.CCTServices.repository.ObjQuestionRepository;
import com.vcti.ct.CCTServices.repository.ParamRepository;
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
	ParamRepository paramRepository;

	@Override
	public Question addQuestion(QuestionBase newQ) {

		if (newQ.getType().equals(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
			System.out.println("Adding Objective Question...");
			ObjQuestion objQ = new ObjQuestion(newQ.getId(), newQ.getStatement(), newQ.getOptions(),
					newQ.getCorrect_option());
			objQRepository.save(objQ);
		} else {
			System.out.println("Adding Subjective Question...");
			SubjQuestion subQ = new SubjQuestion(newQ.getId(), newQ.getStatement(), newQ.getMethodName());
			subjQRepository.save(subQ);
			// Adding in Parameter Table
			addInParamTable(newQ);
		}
		Question question = new Question(newQ.getId(), newQ.getLanguage(), newQ.getType(), newQ.getExperience(),
				newQ.getCreatedUserid());
		questionRepository.save(question);
		return null;
	}

	/**
	 * Adding question request to param table
	 * 
	 * @param newQ : Question Base Object
	 */
	private void addInParamTable(QuestionBase newQ) {
		Map<String, Map<String, Param>> tcMap = newQ.getTestCaseMap();
		if (tcMap != null && !tcMap.isEmpty()) {
			System.out.println("Test Cases are :" + tcMap.size());
			for (Entry<String, Map<String, Param>> entry : tcMap.entrySet()) {
				Map<String, Param> paramMap = entry.getValue();

				for (Entry<String, Param> paramEntry : paramMap.entrySet()) {
					// @NonNull String id, @NonNull String q_id, @NonNull String name, @NonNull
					// String type,@NonNull String testcaseid
					Param paramObj = new Param(UUID.randomUUID().toString(), newQ.getId(),
							paramEntry.getValue().getName(), paramEntry.getValue().getType(), entry.getKey());
					paramRepository.save(paramObj);
				}
			}
		}
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
			} else {
				subjQRepository.deleteById(qId);
				//Fetch param ID
				List<Param> paramList = paramRepository.findByqId(qId);
				for (Param param:paramList) {
					paramRepository.deleteById(param.getId());
				}
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
			// Param Table
			populateParamTable(qBase);
		}

	}

	private void populateParamTable(QuestionBase qBase) {
		List<Param> paramList = paramRepository.findByqId(qBase.getId());
		Map<String, Map<String, Param>> tcMap = new HashMap<String, Map<String, Param>>();
		for (Param parm : paramList) {
			Map<String, Param> paramMap = new HashMap<String, Param>();
			int count = 1;

			// entry doen't exist then add
			if (tcMap.get(parm.getTestcaseid()) == null) {
				paramMap.put(count + "", parm);
				tcMap.put(parm.getTestcaseid(), paramMap);
			} else {
				Map<String, Param> tcExist = tcMap.get(parm.getTestcaseid());
				Set<String> tcKeySet = tcExist.keySet();
				int maxKey = findMaxKey(tcKeySet);
				tcExist.put(maxKey + 1 + "", parm);
			}

		}
		qBase.setTestCaseMap(tcMap);
	}

	private int findMaxKey(Set<String> tcKeySet) {
		List<Integer> list = new ArrayList<Integer>();
		for (String key : tcKeySet) {
			list.add(Integer.parseInt(key));
		}
		Collections.sort(list);
		int size = list.size();
		return list.get(size - 1);
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
	}

	private void populateObjQTable(QuestionBase qBase, ObjQuestion objQ) {
		qBase.setStatement(objQ.getStatement());
		qBase.setOptions(objQ.getOptions());
		qBase.setCorrect_option(objQ.getCorrect_option());

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
					objQ.setOptions(newQues.getOptions());
					objQ.setCorrect_option(newQues.getCorrect_option());
					objQRepository.save(objQ);
				}

			} else {// Subjective Question
				Optional<SubjQuestion> subjQues = subjQRepository.findById(qId);
				if (subjQues.isPresent()) {
					SubjQuestion subjQ = subjQues.get();
					subjQ.setStatement(newQues.getStatement());
					subjQ.setMethodName(newQues.getMethodName());
					subjQRepository.save(subjQ);
				}
				// Param Table

				List<Param> paramList = paramRepository.findByqId(qId);

				for (Param param:paramList) {
					paramRepository.deleteById(param.getId());
				}

				if (paramList.size() > 0) {
					addInParamTable(newQues);

				}
			}
		}
	}
}
