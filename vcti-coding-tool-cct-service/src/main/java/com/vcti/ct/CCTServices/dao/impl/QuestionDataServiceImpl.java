package com.vcti.ct.CCTServices.dao.impl;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vcti.ct.CCTServices.config.CCTConstants;
import com.vcti.ct.CCTServices.config.CCTUtils;
import com.vcti.ct.CCTServices.dao.QuestionDataService;
import com.vcti.ct.CCTServices.exceptions.InvalidQuestionTypeExceptoin;
import com.vcti.ct.CCTServices.exceptions.InvalidQuestionIdException;
import com.vcti.ct.CCTServices.exceptions.QuestionAlreadyExistsException;
import com.vcti.ct.CCTServices.exceptions.QuestionNotFoundExcetion;
import com.vcti.ct.CCTServices.model.ObjQuestion;
import com.vcti.ct.CCTServices.model.Options;
import com.vcti.ct.CCTServices.model.QuesResponse;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.SubjQuestion;
import com.vcti.ct.CCTServices.model.Technology;
import com.vcti.ct.CCTServices.model.TechnologyMap;
import com.vcti.ct.CCTServices.model.ValidateSubjQuestions;
import com.vcti.ct.CCTServices.repository.ObjQuestionRepository;
import com.vcti.ct.CCTServices.repository.OptionsRepository;
import com.vcti.ct.CCTServices.repository.QuestionRepository;
import com.vcti.ct.CCTServices.repository.SubjQuestionRepository;
import com.vcti.ct.CCTServices.repository.TechnologyRepository;

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
	@Autowired
	TechnologyRepository technologyRepository;
	@Autowired
	CassandraOperations cassandraOperations;

	@Override
	public Question addQuestion(QuestionBase newQ) {

		if (newQ.getType().equalsIgnoreCase(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
			System.out.println("Adding Objective Question...");
			// Update Options Table
			newQ.setId(getId("Obj"));
			newQ.setTechnologyId(getId("Tech"));
			updateOptionsTable(newQ);
			// Update ObjectiveQ Table
			updateObjQuestionTable(newQ);

			// Update Technologies Table
			updateTechnology(newQ);

		} else if (newQ.getType().equalsIgnoreCase(CCTConstants.questionTypeEnum.SUBJECTIVE.name())) {
			System.out.println("Adding Subjective Question...");
			// Update SubjectiveQ Table
			newQ.setId(getId("Sub"));
			newQ.setTechnologyId(getId("Tech"));
			updateSubjQuestionTable(newQ);

			// Update Technologies Table
			updateTechnology(newQ);
		} else {
			throw new InvalidQuestionTypeExceptoin(
					"Invalid question type. Question type must be either SUBJECTIVE or OBJECTIVE.");
		}
		// Update Questions Table
		return updateQuestionBaseTable(newQ);
	}

	@Override
	public Question addObjQuestion(QuestionBase newQ) {

		System.out.println("Adding Objective Question...");
		// Update Options Table
		newQ.setId(getId("Obj"));
		newQ.setTechnologyId(getId("Tech"));
		newQ.setType(CCTConstants.questionTypeEnum.OBJECTIVE.name());

		// Update Options Table
		updateOptionsTable(newQ);

		// Update ObjectiveQ Table
		updateObjQuestionTable(newQ);

		// Update Technologies Table
		updateTechnology(newQ);

		// Update Questions Table
		return updateQuestionBaseTable(newQ);
	}

	@Override
	public Question addSubQuestion(QuestionBase newQ) {

		System.out.println("Adding Subjective Question...");
		// Update SubjectiveQ Table
		newQ.setId(getId("Sub"));
		newQ.setTechnologyId(getId("Tech"));
		newQ.setType(CCTConstants.questionTypeEnum.SUBJECTIVE.name());

		// Update Subjectiveq table
		updateSubjQuestionTable(newQ);

		// Update Technologies Table
		updateTechnology(newQ);

		// Update Questions Table
		return updateQuestionBaseTable(newQ);
	}

	private Question updateQuestionBaseTable(QuestionBase newQ) {
		Question question = new Question(newQ.getId(), newQ.getType(), newQ.getExperience(), newQ.getCreatedUserid(),
				newQ.getTitle(), newQ.getDifficulty(), newQ.getTechnologyId());
		questionRepository.save(question);
		return question;
	}

	private void updateSubjQuestionTable(QuestionBase newQ) throws QuestionAlreadyExistsException {
		SubjQuestion subQ = new SubjQuestion(newQ.getId(), newQ.getStatement(), newQ.getMethodName(),
				newQ.getJunitObj(), newQ.getExpectedTime(), newQ.getJunitText());
		subjQRepository.save(subQ);
	}

	private void updateObjQuestionTable(QuestionBase newQ) {
		ObjQuestion objQ = new ObjQuestion(newQ.getId(), newQ.getStatement(), newQ.getOptions().toString(),
				newQ.getCorrectOption());
		objQRepository.save(objQ);
	}

	private void updateOptionsTable(QuestionBase newQ) {
		List<Options> question = optionsRepository.findByqId(newQ.getId());
		if (null != question && !question.isEmpty()) {
			throw new QuestionAlreadyExistsException("Question Already Exists");
		}
		List<String> optionsList = newQ.getOptions();
		List<Options> optionObjList = new ArrayList<Options>();
		Options optionObj = null;
		for (String options : optionsList) {
			optionObj = new Options(getId("Opt"), newQ.getId(), options);
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
			updateTechnology(qBase, ques);
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

			updateTechnology(qBase, question);
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
		qBase.setExpectedTime(subjQ.getExpectedTime());
		qBase.setJunitText(subjQ.getJunitText());
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
		qBase.setType(question.getType());
		qBase.setCreatedUserid(question.getCreatedUserid());
		qBase.setTitle(question.getTitle());
		qBase.setDifficulty(question.getDifficulty());
		qBase.setTechnologyId(question.getTechnologyId());
		qBase.setExperience(question.getExperience());

	}

	@Override
	public void updateQuestion(QuestionBase newQues, String qId) {
		Optional<Question> optionalQuestion = questionRepository.findById(qId);
		if (optionalQuestion.isPresent()) {
			Question modifQ = optionalQuestion.get();
			// Q Base Table
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
	public List<QuesResponse> validateObjQues(List<QuesResponse> list) {

		if (null == list || list.size() == 0) {
			throw new QuestionNotFoundExcetion("Question Not Found");
		}
		// resultList will store ObjQResponse object with qId and Status of selected
		// option
		List<QuesResponse> resultList = new ArrayList<QuesResponse>();
		Map<String, String> qIdOptionMap = new HashMap<String, String>();
		for (QuesResponse responseObj : list) {
			qIdOptionMap.put(responseObj.getqId(), responseObj.getUserInput());
		}
		Iterable<ObjQuestion> objQList = objQRepository.findAllById(qIdOptionMap.keySet());
		int responseSize = 0;
		for (ObjQuestion objQFromDb : objQList) {
			responseSize++;
			if (qIdOptionMap.keySet().contains(objQFromDb.getqId())) {
				QuesResponse responseObj = new QuesResponse();
				responseObj.setqId(objQFromDb.getqId());
				responseObj.setUserInput(
						Boolean.toString(objQFromDb.getCorrect_option().equals(qIdOptionMap.get(objQFromDb.getqId()))));
				resultList.add(responseObj);
			}
		}
		if (list.size() != responseSize) {
			throw new InvalidQuestionIdException("Invalid Question Id");
		}
		return resultList;
	}

	@Override
	public QuesResponse validateSubjQues(ValidateSubjQuestions subjQuesObj) {
		QuesResponse responseObj = new QuesResponse();
		// Get complete Program
		String prog = subjQuesObj.getQuesResponseObj().getUserInput();
		if (subjQuesObj.getClassName() == null) {
			subjQuesObj.setClassName("ExampleClass");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String currentTime = sdf.format(timestamp);
		String userDir = subjQuesObj.getUserId() + currentTime;
		// Save program to a Java File
		String path = CCTUtils.writeProgInFile(prog, subjQuesObj.getClassName(), userDir);
		// compilationStatus can be compilation failure or Junit results
		Map<String, String> compilationStatus = CCTUtils.compileJavaProgram(path);

		if (compilationStatus.containsKey(CCTConstants.status.SUCCESS.name())) {
			// Call Junit Stub which will call runJavaProgram
			responseObj.setCompilationsStatus(CCTConstants.status.SUCCESS.name());
			String junitProg = fetchJunitFromSubjQTable(subjQuesObj.getQuesResponseObj().getqId());
//			String junitProg = new String(readJunitFile());
			if (junitProg == null) {
				System.out.println("No record found in Question table for id");
				responseObj.setqId(subjQuesObj.getQuesResponseObj().getqId());
				responseObj.setUserInput("No record found in Question table for id");
				return responseObj;
			} else {
				String junitName = subjQuesObj.getClassName() + "Test";
				// Write Junit Prog on File
				String testClassPath = CCTUtils.writeProgInFile(junitProg, junitName, userDir);
				// Compile Junit Program
				Map<String, String> testCompilationStatus = CCTUtils.compileJavaProgram(testClassPath, userDir + "\\");
				if (testCompilationStatus.containsKey(CCTConstants.status.SUCCESS.name())) {
					// Since Junit compilation is also successful, then run the Junit
					Map<String, String> runJunitStatus = CCTUtils.runJavaProgram(junitName, userDir);
					if (runJunitStatus.containsKey(CCTConstants.status.SUCCESS.name())) {
						// Junit Run program is successful
						responseObj.setqId(subjQuesObj.getQuesResponseObj().getqId());
						responseObj.setUserInput(runJunitStatus.get(CCTConstants.status.SUCCESS.name()));
					} else {
						responseObj.setqId(subjQuesObj.getQuesResponseObj().getqId());
						responseObj.setUserInput(runJunitStatus.get(CCTConstants.status.FAIL.name()));
					}

				} else {
					System.out.println("Junit compilation is failing " + testCompilationStatus);
					responseObj.setqId(subjQuesObj.getQuesResponseObj().getqId());
					responseObj.setUserInput(testCompilationStatus.get(CCTConstants.status.FAIL.name()));
				}
			}

		} else {
			// Compilation failed for Java Program
			responseObj.setCompilationsStatus(CCTConstants.status.FAIL.name());
			responseObj.setqId(subjQuesObj.getQuesResponseObj().getqId());
			responseObj.setUserInput(compilationStatus.get(CCTConstants.status.FAIL.name()));
		}
		return responseObj;
	}

//	private byte[] readJunitFile() {
//
//		String path = "C:\\JUnit\\ExampleClassTest.java";
//		StringBuilder builder = new StringBuilder();
//		try {
//			File file = new File(path);
//			FileReader reader = new FileReader(file);
//			BufferedReader br = new BufferedReader(reader);
//			String fileData = br.readLine();
//
//			while (fileData != null) {
//				builder.append(fileData);
//				fileData = br.readLine();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return builder.toString().getBytes();
//	}

	private String fetchJunitFromSubjQTable(String qId) {
		Optional<SubjQuestion> subjQues = subjQRepository.findById(qId);
		if (subjQues.isPresent()) {
			String converted = null;
			SubjQuestion subjQ = subjQues.get();
			ByteBuffer junitBuff = subjQ.getJunit();
			System.out.println("Buffer is : " + junitBuff);
			if (junitBuff != null) {
				converted = new String(junitBuff.array());
			} else if (null != subjQ.getJunitText()) {
				converted = new String(subjQ.getJunitText().getBytes());
			}
			return converted;
		}
		System.out.println("No record found in Question table for id:" + qId);
		return null;
	}

	@Override
	public Boolean[] executeQuery(List<String> queries) {
		Boolean result[] = new Boolean[queries.size()];
		int index = 0;
		for (String query : queries) {
			try {
				result[index] = cassandraOperations.getCqlOperations().execute(query.trim());
			} catch (Exception ex) {
				result[index] = false;
			}
			index++;
		}
		return result;
	}

	private String getId(String questionType) {
		String id = questionType + "X" + new Random().nextInt(100000) + "X" + System.currentTimeMillis();
		return id;
	}

	@Override
	public Technology updateTechnology(QuestionBase newQ) {
		Technology technology = new Technology();
		technology.setId(newQ.getTechnologyId());
		technology.setTechnology(newQ.getTechnology());
		technology.setTopic(newQ.getTopic());
		return technologyRepository.save(technology);
	}

	@Override
	public Technology addTechnology(Technology technology) {
		technology.setId(getId("Tech"));
		return technologyRepository.save(technology);
	}

	@Override
	public List<Technology> getTechnology(String tname) {
		return technologyRepository.findByTechnology(tname);
	}

	@Override
	public List<TechnologyMap> getTechnologyByKey(String tname) {
		List<Technology> techList = technologyRepository.findByTechnology(tname);
		return convert(techList);
	}

	@Override
	public List<TechnologyMap> getAllTechnology() {
		Iterable<Technology> techList = technologyRepository.findAll();
		return convert(techList);
	}

	private List<TechnologyMap> convert(Iterable<Technology> techList) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (Technology tech : techList) {
			List<String> topics = map.get(tech.getTechnology());
			if (topics == null) {
				topics = new ArrayList<String>();
			}
			topics.add(tech.getTopic());
			map.put(tech.getTechnology(), topics);
		}
		List<TechnologyMap> techLists = new ArrayList<TechnologyMap>();
		Iterator<String> itr = map.keySet().iterator();
		while (itr.hasNext()) {
			TechnologyMap list = new TechnologyMap();
			String key = itr.next();
			list.setTechnology(key);
			list.setTopics(map.get(key));
			techLists.add(list);
		}
		return techLists;
	}

	@Override
	public List<QuestionBase> getAllQuestionsByType(String type) {
		List<QuestionBase> baseQuesList = new ArrayList<QuestionBase>();
		if (null != type) {
			type = type.toUpperCase();
			List<Question> questions = findQuestionsByType(type);
			if (type.equalsIgnoreCase(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
				for (Question ques : questions) {
					ObjQuestion objQuestion = findObjQuestionsByqid(ques.getId());
					Technology technology = findTechnologyById(ques.getTechnologyId());
					QuestionBase baseQues = mergeObjectiveQuestion(ques, objQuestion, technology);
					baseQuesList.add(baseQues);
				}
			} else if (type.equalsIgnoreCase(CCTConstants.questionTypeEnum.SUBJECTIVE.name())) {
				for (Question ques : questions) {
					SubjQuestion subQuestion = findSubQuestionsByqid(ques.getId());
					Technology technology = findTechnologyById(ques.getTechnologyId());
					QuestionBase baseQues = mergeSubjectiveQuestion(ques, subQuestion, technology);
					baseQuesList.add(baseQues);
				}
			} else {
				throw new InvalidQuestionTypeExceptoin(
						"Invalid question type. Question type must be either SUBJECTIVE or OBJECTIVE.");
			}
		}
		return baseQuesList;
	}

	@Override
	public List<QuestionBase> getAllQuestionsByTname(String tname) {
		List<QuestionBase> baseQuesList = new ArrayList<QuestionBase>();
		Iterable<Question> questions = questionRepository.findAll();
		Iterator<Question> iterator = questions.iterator();
		while (iterator.hasNext()) {
			Question ques = iterator.next();
			QuestionBase baseQues = null;
			Technology technology = findTechnologyById(ques.getTechnologyId());
			if (null != technology && null != technology.getTechnology()
					&& technology.getTechnology().equalsIgnoreCase(tname)) {
				if (ques.getType().equalsIgnoreCase(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
					ObjQuestion objQuestion = findObjQuestionsByqid(ques.getId());
					baseQues = mergeObjectiveQuestion(ques, objQuestion, technology);
				} else {
					SubjQuestion subQuestion = findSubQuestionsByqid(ques.getId());
					baseQues = mergeSubjectiveQuestion(ques, subQuestion, technology);
				}
				baseQuesList.add(baseQues);
			}
		}
		return baseQuesList;
	}

	@Override
	public List<QuestionBase> getAllQuestionsByTypeAndTname(String type, String tname) {
		List<QuestionBase> baseQuesList = new ArrayList<QuestionBase>();
		if (null != type && null != tname) {
			type = type.toUpperCase();
			List<Question> questions = findQuestionsByType(type);
			if (type.equalsIgnoreCase(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
				for (Question ques : questions) {
					ObjQuestion objQuestion = findObjQuestionsByqid(ques.getId());
					Technology technology = findTechnologyById(ques.getTechnologyId());
					if (null != technology.getTechnology() && technology.getTechnology().equalsIgnoreCase(tname)) {
						QuestionBase baseQues = mergeObjectiveQuestion(ques, objQuestion, technology);
						baseQuesList.add(baseQues);
					}
				}
			} else if (type.equalsIgnoreCase(CCTConstants.questionTypeEnum.SUBJECTIVE.name())) {
				for (Question ques : questions) {
					SubjQuestion subQuestion = findSubQuestionsByqid(ques.getId());
					Technology technology = findTechnologyById(ques.getTechnologyId());
					if (null != technology.getTechnology() && technology.getTechnology().equalsIgnoreCase(tname)) {
						QuestionBase baseQues = mergeSubjectiveQuestion(ques, subQuestion, technology);
						baseQuesList.add(baseQues);
					}
				}
			} else {
				throw new InvalidQuestionTypeExceptoin(
						"Invalid question type. Question type must be either SUBJECTIVE or OBJECTIVE.");
			}
		}
		return baseQuesList;
	}

	private List<Question> findQuestionsByType(String typeName) {
		List<Question> questions = questionRepository.findByType(typeName);
		return questions;
	}

	private SubjQuestion findSubQuestionsByqid(String qId) {
		Optional<SubjQuestion> subQuestions = null;
		if (null != qId) {
			subQuestions = subjQRepository.findById(qId);
			if (null != subQuestions && subQuestions.isPresent())
				return subQuestions.get();
		}
		return new SubjQuestion();
	}

	private ObjQuestion findObjQuestionsByqid(String qId) {
		Optional<ObjQuestion> subQuestions = null;
		if (null != qId) {
			subQuestions = objQRepository.findById(qId);
			if (null != subQuestions && subQuestions.isPresent())
				return subQuestions.get();
		}
		return new ObjQuestion();
	}

	private Technology findTechnologyById(String id) {
		Optional<Technology> technology = null;
		if (null != id) {
			technology = technologyRepository.findById(id);
			if (null != technology && technology.isPresent())
				return technology.get();
		}
		return new Technology();
	}

	private QuestionBase mergeSubjectiveQuestion(Question ques, SubjQuestion sub, Technology tech) {
		QuestionBase base = mergeBaseQuestionWithTechnology(ques, tech);
		base.setExpectedTime(sub.getExpectedTime());
		base.setJunitObj(sub.getJunit());
		base.setJunitText(sub.getJunitText());
		base.setMethodName(sub.getMethodName());
		base.setStatement(sub.getStatement());
		return base;
	}

	private QuestionBase mergeObjectiveQuestion(Question ques, ObjQuestion obj, Technology tech) {
		QuestionBase base = mergeBaseQuestionWithTechnology(ques, tech);
		base.setCorrect_option(obj.getCorrect_option());
		if (null != obj.getqId()) {
			List<Options> options = optionsRepository.findByqId(obj.getqId());
			List<String> objOptions = new ArrayList<String>();
			for (Options opt : options) {
				objOptions.add(opt.getOptions());
			}
			base.setOptions(objOptions);
			base.setStatement(obj.getStatement());
		}
		return base;
	}

	private QuestionBase mergeBaseQuestionWithTechnology(Question ques, Technology tech) {
		QuestionBase base = new QuestionBase();
		base.setId(ques.getId());
		base.setCreatedUserid(ques.getCreatedUserid());
		base.setDifficulty(ques.getDifficulty());
		base.setExperience(ques.getExperience());
		base.setTechnologyId(ques.getTechnologyId());
		base.setTitle(ques.getTitle());
		base.setType(ques.getType());
		base.setTechnology(tech.getTechnology());
		base.setTopic(tech.getTopic());
		return base;
	}

	@Override
	public Question updateSubQuestion(QuestionBase newQ, String id) {
		System.out.println("Updating Subjective Question...");
		// Update SubjectiveQ Table
		newQ.setId(id);
		newQ.setType(CCTConstants.questionTypeEnum.SUBJECTIVE.name());

		// Update Subjectiveq table
		updateSubjQuestionTable(newQ);

		// Update Technologies Table
		updateTechnology(newQ);

		// Update Questions Table
		return updateQuestionBaseTable(newQ);
	}

	@Override
	public Question updateObjQuestion(QuestionBase newQ, String id) {
		System.out.println("Updating Objective Question...");
		// Update Options Table
		newQ.setId(id);
		newQ.setType(CCTConstants.questionTypeEnum.OBJECTIVE.name());

		// Update/Edit Options Table
		editOptionsTable(newQ);

		// Update ObjectiveQ Table
		updateObjQuestionTable(newQ);

		// Update Technologies Table
		updateTechnology(newQ);

		// Update Questions Table
		return updateQuestionBaseTable(newQ);
	}

	private void editOptionsTable(QuestionBase newQ) {
		if (null != newQ.getId()) {
			List<Options> oldOptions = optionsRepository.findByqId(newQ.getId());
			for (Options old : oldOptions) {
				optionsRepository.deleteById(old.getId());
			}
		}
		List<String> optionsList = newQ.getOptions();
		List<Options> optionObjList = new ArrayList<Options>();
		Options optionObj = null;
		for (String options : optionsList) {
			optionObj = new Options(getId("Opt"), newQ.getId(), options);
			optionObjList.add(optionObj);
		}
		optionsRepository.saveAll(optionObjList);
	}

	@Override
	public Question updateQuestions(QuestionBase newQues, String id) {
		newQues.setId(id);
		return updateQuestion(newQues);

	}

	private Question updateQuestion(QuestionBase newQ) {

		if (newQ.getType().equalsIgnoreCase(CCTConstants.questionTypeEnum.OBJECTIVE.name())) {
			System.out.println("Updating Objective Question...");
			// Update Options Table
			editOptionsTable(newQ);
			// Update ObjectiveQ Table
			updateObjQuestionTable(newQ);

			// Update Technologies Table
			updateTechnology(newQ);

		} else if (newQ.getType().equalsIgnoreCase(CCTConstants.questionTypeEnum.SUBJECTIVE.name())) {
			System.out.println("Updating Subjective Question...");
			// Update SubjectiveQ Table
			updateSubjQuestionTable(newQ);

			// Update Technologies Table
			updateTechnology(newQ);

		} else {
			throw new InvalidQuestionTypeExceptoin(
					"Invalid question type. Question type must be either SUBJECTIVE or OBJECTIVE.");
		}
		// Update Questions Table
		return updateQuestionBaseTable(newQ);
	}

	private void updateTechnology(QuestionBase qBase, Question ques) {
		if (null != ques.getTechnologyId()) {
			Optional<Technology> tech = technologyRepository.findById(ques.getTechnologyId());
			if (tech.isPresent()) {
				Technology tch = tech.get();
				qBase.setTechnologyId(tch.getId());
				qBase.setTechnology(tch.getTechnology());
				qBase.setTopic(tch.getTopic());
			}
		}
	}
}
