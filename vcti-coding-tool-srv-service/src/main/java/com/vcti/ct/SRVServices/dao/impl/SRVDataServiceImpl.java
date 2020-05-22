package com.vcti.ct.SRVServices.dao.impl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcti.ct.SRVServices.dao.SRVDataService;
import com.vcti.ct.SRVServices.exceptions.DuplicateScheduleRequestException;
import com.vcti.ct.SRVServices.exceptions.InvalidScheduleRequestIdException;
import com.vcti.ct.SRVServices.model.CandidateResult;
import com.vcti.ct.SRVServices.model.Interviewer;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.ObjectiveResultReport;
import com.vcti.ct.SRVServices.model.QuesResponse;
import com.vcti.ct.SRVServices.model.QuestionBase;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
import com.vcti.ct.SRVServices.model.ScheduleChallenge;
import com.vcti.ct.SRVServices.model.ScheduledRequest;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;
import com.vcti.ct.SRVServices.model.SubjQuestionResultPojo;
import com.vcti.ct.SRVServices.model.SubjectiveResultReport;
import com.vcti.ct.SRVServices.model.User;
import com.vcti.ct.SRVServices.model.ValidateSubjQuestions;
import com.vcti.ct.SRVServices.repository.ObjResultRepository;
import com.vcti.ct.SRVServices.repository.QuestionSchedulerRepository;
import com.vcti.ct.SRVServices.repository.ScheduleChallengeRepository;
import com.vcti.ct.SRVServices.repository.ScheduleRequestRepository;
import com.vcti.ct.SRVServices.repository.SubjResultRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Component
@Service
public class SRVDataServiceImpl implements SRVDataService {
	@Autowired
	QuestionSchedulerRepository questionScheduleRepository;
	@Autowired
	ObjResultRepository objResultRepository;
	@Autowired
	SubjResultRepository subjResultRepository;
	@Autowired
	ScheduleRequestRepository scheduleRequestRepository;
	@Autowired
	ScheduleChallengeRepository scheduleChallengeRepository;
	@Autowired
	@Value("${vcc.aa.service.host.port}")
	private String aaServiceHostPort;
	@Value("${vcc.cct.service.host.port}")
	private String cctServiceHostPort;
	@Value("${vcc.email.service.host.port}")
	private String emailServiceHostPort;

	@Value("${vcc.candidate.email.subject}")
	private String testLinkEmailSubject;

	@Value("${vcc.candidate.report.email.subject}")
	private String reportEmailSubject;

	@Value("${vcc.user.login.url}")
	private String userLoginUrl;

	private String testLinkEmailMsg;
	private String reportToInterviewerMsg;

	private final long millSecFor24Hours = 24 * 60 * 60 * 1000;
	
	@PostConstruct
	public void init() {
		try {
			testLinkEmailMsg = new String(
					Files.readAllBytes(Paths.get(ResourceUtils.getFile("classpath:testLinkEmailMsg.txt").toString())));
			reportToInterviewerMsg = new String(Files
					.readAllBytes(Paths.get(ResourceUtils.getFile("classpath:reportToInterverMsg.txt").toString())));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean assignUser(QuestionScheduler assignQ) {
		assignQ.setId(getId("SchQuest"));
		questionScheduleRepository.save(assignQ);
		return true;
	}

	@Override
	public boolean unAssignUser(QuestionScheduler unAssignQ) {
		deleteQEntry(getQList(unAssignQ));
		return true;
	}

	@Override
	public Boolean bulkAssignUser(QuestionSchedulerCustom assignBulkQ) {
		List<QuestionScheduler> assignQObjList = new ArrayList<QuestionScheduler>();
		List<ScheduleChallenge> scheduleChallengeList = new ArrayList<ScheduleChallenge>();
		List<String> challengeIdList = new ArrayList<String>();
		QuestionScheduler assignQ;
		ScheduleChallenge scheduleChallenge;

		List<String> qIdList = assignBulkQ.getQidList();
		if (qIdList == null) {
			System.out.println("No Question Id specified.");
			return false;
		}

		if (assignBulkQ.getStatus() != null && assignBulkQ.getStatus().equals("Completed")) {
			ScheduleChallenge challengeRecord = scheduleChallengeRepository
					.findByChallengeid(assignBulkQ.getChallengeid());
			if (challengeRecord != null) {
				challengeRecord.setStatus("Stale");
				scheduleChallengeRepository.save(challengeRecord);
			}
		}

		List<String> assignedUserIdList = assignBulkQ.getAssigneduidList();
		// Schedule Challenge

		for (String userId : assignedUserIdList) {
			List<ScheduleChallenge> scheduledChallenge = scheduleChallengeRepository.findByAssigneduid(userId);
			for (ScheduleChallenge challenge : scheduledChallenge) {
				challenge.setStatus("Stale");
				scheduleChallengeRepository.save(challenge);
			}
			String challengeId = "ChallengeX" + new Random().nextInt(100000) + "X" + System.currentTimeMillis();
			scheduleChallenge = new ScheduleChallenge(challengeId, userId, assignBulkQ.getAssigneruid(), "Scheduled",
					assignBulkQ.getScheduleTime(), null, null);
			scheduleChallengeList.add(scheduleChallenge);
			scheduleChallengeRepository.saveAll(scheduleChallengeList);
			challengeIdList.add(challengeId);

		}

		for (String qId : qIdList) {
			for (String challengeId : challengeIdList) {
				ScheduleChallenge challengeRecord = scheduleChallengeRepository.findByChallengeid(challengeId);
				assignQ = new QuestionScheduler(getId("SchQuest"), qId, challengeRecord.getAssigneduid(),
						assignBulkQ.getAssigneruid(), challengeId);
				assignQObjList.add(assignQ);
			}
		}

		questionScheduleRepository.saveAll(assignQObjList);
		return true;
	}

	@Override
	public Boolean bulkUnAssignUser(QuestionSchedulerCustom unAssignBulkQ) {
		List<QuestionScheduler> unAssignQObjList = new ArrayList<QuestionScheduler>();
		QuestionScheduler unAssignQ = new QuestionScheduler();
		List<String> qIdList = unAssignBulkQ.getQidList();
		List<String> assignedUserIdList = unAssignBulkQ.getAssigneduidList();
		for (String qId : qIdList) {
			for (String userId : assignedUserIdList) {
				unAssignQ = new QuestionScheduler("", qId, userId, unAssignBulkQ.getAssigneruid(), "");
				unAssignQObjList.add(unAssignQ);
			}
		}
		deleteQEntry(getQList(qIdList, unAssignQObjList));

		return true;
	}

	private List<QuestionScheduler> getQList(QuestionScheduler schQues) {
		List<QuestionScheduler> quesList = questionScheduleRepository.findByQid(schQues.getQid());

		return getMatchingCriteriaList(quesList, schQues);
	}

	private List<QuestionScheduler> getMatchingCriteriaList(List<QuestionScheduler> quesList,
			QuestionScheduler schQues) {
		List<QuestionScheduler> criteriaMatchedList = new ArrayList<QuestionScheduler>();
		for (QuestionScheduler ques : quesList) {
			// when no assigner user and assigned user is provided
			if (schQues.getAssigneruid() == null && schQues.getAssigneduid() == null) {
				criteriaMatchedList.add(ques);
				continue;
			}
			if (schQues.getAssigneruid() != null && schQues.getAssigneruid().equals(ques.getAssigneruid())) {
				if (schQues.getAssigneduid() != null && schQues.getAssigneduid().equals(ques.getAssigneduid()))
					criteriaMatchedList.add(ques);
			}

		}
		return criteriaMatchedList;
	}

	private List<QuestionScheduler> getQList(List<String> qIdList, List<QuestionScheduler> schQuesList) {
		// TODO - Check why below DB call doesn't work ?
		// List<QuestionScheduler> quesList =
		// questionScheduleRepository.findAllByQid(qIdList);
		List<QuestionScheduler> quesList = new ArrayList<QuestionScheduler>();
		for (String qId : qIdList) {
			quesList.addAll(questionScheduleRepository.findByQid(qId));
		}
		List<QuestionScheduler> matchingCritList = new ArrayList<QuestionScheduler>();
		for (QuestionScheduler schQues : schQuesList) {
			matchingCritList.addAll(getMatchingCriteriaList(quesList, schQues));
		}
		return matchingCritList;
	}

	private void deleteQEntry(List<QuestionScheduler> quesList) {
		questionScheduleRepository.deleteAll(quesList);
	}

	@Override
	public List<QuestionScheduler> getQuestions() {
		List<QuestionScheduler> quesList = new ArrayList<QuestionScheduler>();
		Iterable<QuestionScheduler> iter = questionScheduleRepository.findAll();
		iter.forEach(quesList::add);
		return quesList;
	}

	@Override
	public List<QuestionScheduler> getQuestions(String qId) {
		return questionScheduleRepository.findByQid(qId);

	}

	@Override
	public List<QuestionScheduler> getQuestions(QuestionScheduler schQues) {
		return getQList(schQues);
	}

	// ObjQ Result
	@Override
	public boolean addObjQResult(ObjQuestionResult objQRes) {
		objResultRepository.save(objQRes);
		return true;
	}

	@Override
	public boolean addObjQResultList(List<ObjQuestionResult> objQResList) {
		objResultRepository.saveAll(objQResList);
		return true;
	}

	@Override
	public boolean removeObjQResult(ObjQuestionResult objQRes) {
		getObjQResult(objQRes).forEach(objQ -> objResultRepository.deleteById(objQ.getKey().toString()));
		return true;
	}

	@Override
	public List<ObjQuestionResult> getObjQResult() {

		List<ObjQuestionResult> objQuesList = new ArrayList<ObjQuestionResult>();
		Iterable<ObjQuestionResult> iter = objResultRepository.findAll();
		iter.forEach(objQuesList::add);
		return objQuesList;
	}

	@Override
	public List<ObjQuestionResult> getObjQResult(ObjQuestionResult objQResult) {
		List<ObjQuestionResult> objQList = null;

		if (objQResult.getKey().getUserId() != null && objQResult.getKey().getQid() != null) {
			objQList = objResultRepository.findByKeyUserIdAndKeyQid(objQResult.getKey().getUserId(),
					objQResult.getKey().getQid());
		} else if (objQResult.getKey().getUserId() == null && objQResult.getKey().getQid() != null) {
			objQList = objResultRepository.findByKeyQid(objQResult.getKey().getQid());
		} else if (objQResult.getKey().getUserId() != null && objQResult.getKey().getQid() == null) {
			objQList = objResultRepository.findByKeyUserId(objQResult.getKey().getUserId());
		}
		return objQList;
	}

	@Override
	public List<ObjQuestionResult> getObjQResultByUserId(String userId) {
		return objResultRepository.findByKeyUserId(userId);
	}

	@Override
	public List<ObjQuestionResult> getObjQResultByQId(String qId) {
		return objResultRepository.findByKeyQid(qId);
	}

	// SubjQ result
	/*
	 * @Override public boolean addSubjQResult(SubjQuestionResult subjQRes) {
	 * subjResultRepository.save(subjQRes); return true; }
	 * 
	 * @Override public boolean addSubjQResultList(List<SubjQuestionResult>
	 * subjQResList) { subjResultRepository.saveAll(subjQResList); return true; }
	 */
	private ByteBuffer convertStringToByteBuffer(String pro) {

		Charset charset = Charset.forName("UTF-8");
		CharsetEncoder encoder = charset.newEncoder();
		ByteBuffer buff = null;
		try {
			buff = encoder.encode(CharBuffer.wrap(pro));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buff;
	}

	private String convertByteBufferToString(ByteBuffer buffer) {
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		String data = "";
		try {
			int old_position = buffer.position();
			data = decoder.decode(buffer).toString();
			// reset buffer's position to its original so it is not altered:
			buffer.position(old_position);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return data;
	}

	private SubjQuestionResult getSubjQuestionResult(SubjQuestionResultPojo pojo, QuesResponse questionresponse) {
		SubjQuestionResult subjResult = new SubjQuestionResult();
		subjResult.setClassName(pojo.getClassName());
		// subjResult.setConsolidatedoutput(pojo.getConsolidatedoutput());
		subjResult.setKey(pojo.getKey());
		subjResult.setProgram(convertStringToByteBuffer(pojo.getProgram()));

		if (questionresponse != null) {
			subjResult.setConsolidatedoutput(questionresponse.getUserInput());
		}

		return subjResult;
	}

	@Override
	public boolean addSubjQResult(SubjQuestionResultPojo subjQResPojo) {
		QuesResponse questionresponse = this.getCompilationsStatus(subjQResPojo);

		SubjQuestionResult subjQRes = this.getSubjQuestionResult(subjQResPojo, questionresponse);
		subjQRes.setCompilationStatus(questionresponse.getCompilationsStatus());

		if (null != questionresponse) {
			subjQRes.setCompilationStatus(questionresponse.getCompilationsStatus());
			subjResultRepository.save(subjQRes);
			return (null != questionresponse.getCompilationsStatus()
					&& questionresponse.getCompilationsStatus().equalsIgnoreCase("SUCCESS")) ? true : false;
		}
		return false;
	}

	private QuesResponse getCompilationsStatus(SubjQuestionResultPojo subjQRes) {
		final String uri = cctServiceHostPort + "validateSubjQues";
		ValidateSubjQuestions request = new ValidateSubjQuestions();
		QuesResponse qr = new QuesResponse();
		qr.setqId(subjQRes.getKey().getQid());
		qr.setUserInput(subjQRes.getProgram().toString());
		request.setUserId(subjQRes.getKey().getUserId());
		request.setQuesResponseObj(qr);
		request.setClassName(subjQRes.getClassName());
		RestTemplate restTemplate = new RestTemplate();
		QuesResponse questionResponse = null;
		try {
			questionResponse = restTemplate.postForObject(uri, request, QuesResponse.class);

		} catch (Exception e) {
			System.out.println(e);
		}
		// QuesResponse
		// questionResponse=restTemplate.getForObject(uri,QuesResponse.class);
		return questionResponse;
	}

	@Override
	public boolean addSubjQResultList(List<SubjQuestionResultPojo> subjQResList) {
		List<SubjQuestionResult> subQResultList = new ArrayList<SubjQuestionResult>();
		for (int i = 0; i < subjQResList.size(); i++) {

			SubjQuestionResultPojo subjQRes = subjQResList.get(i);

			QuesResponse questionresponse = this.getCompilationsStatus(subjQRes);
			SubjQuestionResult subjQResult = this.getSubjQuestionResult(subjQRes, questionresponse);
			subQResultList.add(subjQResult);
		}
		subjResultRepository.saveAll(subQResultList);
		return true;
	}

	@Override
	public boolean removeSubjQResult(SubjQuestionResult subjQRes) {
		getSubjQResult(subjQRes).forEach(subjQ -> subjResultRepository.deleteById(subjQ.getKey().toString()));
		return true;
	}

	@Override
	public List<SubjQuestionResult> getSubjQResult() {
		List<SubjQuestionResult> subjQuesList = new ArrayList<SubjQuestionResult>();
		Iterable<SubjQuestionResult> iter = subjResultRepository.findAll();
		iter.forEach(subjQuesList::add);
		return subjQuesList;
	}

	@Override
	public List<SubjQuestionResult> getSubjQResult(SubjQuestionResult subjQResult) {

		List<SubjQuestionResult> subjQList = null;

		if (subjQResult.getKey().getUserId() != null && subjQResult.getKey().getQid() != null) {
			subjQList = subjResultRepository.findByKeyUserIdAndKeyQid(subjQResult.getKey().getUserId(),
					subjQResult.getKey().getQid());
		} else if (subjQResult.getKey().getUserId() == null && subjQResult.getKey().getQid() != null) {
			subjQList = subjResultRepository.findByKeyQid(subjQResult.getKey().getQid());
		} else if (subjQResult.getKey().getUserId() != null && subjQResult.getKey().getQid() == null) {
			subjQList = subjResultRepository.findByKeyUserId(subjQResult.getKey().getUserId());
		}
		return subjQList;

	}

	@Override
	public List<SubjQuestionResult> getSubjQResultByUserId(String userId) {
		return subjResultRepository.findByKeyUserId(userId);
	}

	@Override
	public List<SubjQuestionResult> getSubjQResultByQId(String qId) {
		return subjResultRepository.findByKeyQid(qId);
	}

	@Override
	public List<QuestionSchedView> getQuestionsByUserId(String userId) {
		List<QuestionSchedView> questionIdList = questionScheduleRepository.findByAssigneduid(userId);
		return questionIdList;
	}

	@Override
	public List<ScheduledRequest> scheduleRequest(List<ScheduledRequest> scheduleRequests) {
		List<ScheduledRequest> response = new ArrayList<ScheduledRequest>();
		for (ScheduledRequest scheduleRequest : scheduleRequests) {
			String userId = scheduleRequest.getCandidateEmailId();
			List<ScheduledRequest> oldRequest = scheduleRequestRepository.findByCandidateEmailId(userId);
			boolean isAnyMatched = oldRequest.stream().anyMatch(
					request -> request.getRequirementId().equalsIgnoreCase(scheduleRequest.getRequirementId()));
			if (!isAnyMatched) {
				saveOrUpdateCandidateInUserTable(scheduleRequest);
				scheduleRequest.setId(getId("SchReq"));
				scheduleRequest.setRequestedDate(getCurrentDate());
				ScheduledRequest scheduledRequest = scheduleRequestRepository.save(scheduleRequest);
				response.add(scheduledRequest);
			} else {
				throw new DuplicateScheduleRequestException("Schedule Request is already in process with this user: { "
						+ userId + " } " + "and this requirementId: { " + scheduleRequest.getRequirementId() + " }");
			}
		}
		return response;
	}

	private String getCurrentDate() {
		Date d1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - hh:mm:ss a");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		return d1.toString();
	}

	private String getId(String questionType) {
		String id = questionType + "X" + new Random().nextInt(100000) + "X" + System.currentTimeMillis();
		return id;
	}

	private String saveOrUpdateCandidateInUserTable(ScheduledRequest scheduleRequest) {
		String userJson = makeJson(scheduleRequest);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(userJson, headers);
		String url = aaServiceHostPort + "user";
		ResponseEntity<String> resultJson = null;
		try {
			resultJson = restTemplate.postForEntity(url, request, String.class);
			return resultJson.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private String generatePassword(String userId) {
		String values = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int passwordLength = 20;
		Random random = new Random();
		char[] pwd = new char[passwordLength];
		for (int i = 0; i < passwordLength; i++) {
			pwd[i] = values.charAt(random.nextInt(values.length()));
		}
		return new String(pwd) + "@V1s9p7l";
	}

	private String makeJson(ScheduledRequest scheduleRequest) {
		String userId = scheduleRequest.getCandidateEmailId();
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("name", scheduleRequest.getCandidateName());
		map.put("userId", userId);
		map.put("experience", scheduleRequest.getCandidateExperience());
		map.put("password", generatePassword(userId));
		map.put("roleId", "CANDIDATE");
		return returnJson(map);
	}

	private String returnJson(Map<String, String> map) {
		try {
			return new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public List<ScheduledRequest> getAllScheduledRequest() {
		Iterator<ScheduledRequest> itr = scheduleRequestRepository.findAll().iterator();
		List<ScheduledRequest> allData = new ArrayList<ScheduledRequest>();
		while (itr.hasNext()) {
			allData.add(itr.next());
		}
		return allData;
	}

	@Override
	public List<ScheduledRequest> rescheduleRequest(List<ScheduledRequest> scheduleRequestList) {
		List<ScheduledRequest> response = new ArrayList<ScheduledRequest>();
		for (ScheduledRequest schRequest : scheduleRequestList) {
			saveOrUpdateCandidateInUserTable(schRequest);
			response.add(rescheduleRequest(schRequest));
		}
		return response;
	}

	private ScheduledRequest rescheduleRequest(ScheduledRequest scheduleRequest) {
		saveOrUpdateCandidateInUserTable(scheduleRequest);
		ScheduledRequest scheduledRequest = scheduleRequestRepository.save(scheduleRequest);
		return scheduledRequest;
	}

	@Override
	public List<ScheduledRequest> cancelScheduleRequest(List<String> ids) {
		List<ScheduledRequest> response = new ArrayList<ScheduledRequest>();
		for (String id : ids) {
			Optional<ScheduledRequest> scheduleRequest = scheduleRequestRepository.findById(id);
			if (scheduleRequest.isPresent()) {
				deleteCandidateFromUserTable(scheduleRequest.get());
				scheduleRequestRepository.deleteById(id);
				response.add(scheduleRequest.get());
			} else {
				throw new InvalidScheduleRequestIdException("Invalid schedule request Id");
			}
		}
		return response;
	}

	@Override
	public byte[] getSubjObjResultReport(String candidateId) {
		List<SubjectiveResultReport> subjReport = new ArrayList<SubjectiveResultReport>();
		List<SubjQuestionResult> subjQResults = null;
		try {
			subjQResults = subjResultRepository.findByKeyUserId(candidateId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (SubjQuestionResult subj : subjQResults) {
			// Optional<SubjQuestion>
			// subjQuestion=subjQuestionRepository.findByqId(subj.getKey().getQid());
			// String type="SUBJECTIVE";
			String url = cctServiceHostPort + "question/";
			url = url + subj.getKey().getQid();
			QuestionBase subjQuestions = null;
			RestTemplate restTemplate = new RestTemplate();
			try {
				subjQuestions = restTemplate.getForObject(url, QuestionBase.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SubjectiveResultReport sReport = new SubjectiveResultReport();
			if (subjQuestions != null) {
				sReport.setProgram(subjQuestions.getStatement());
			}

			sReport.setAnsSubmitted(convertByteBufferToString(subj.getProgram()));
			sReport.setConsolidatedOutput(subj.getConsolidatedoutput());
			subjReport.add(sReport);
		}
		List<ObjQuestionResult> objresults = objResultRepository.findByKeyUserId(candidateId);
		List<ObjectiveResultReport> objReports = new ArrayList<ObjectiveResultReport>();
		for (ObjQuestionResult objresult : objresults) {
			ObjectiveResultReport objreport = new ObjectiveResultReport();
			// Optional<ObjQuestion>
			// objquestion=objQuestionRepository.findByqId(objresult.getKey().getQid());
			String url = cctServiceHostPort + "question/";
			url = url + objresult.getKey().getQid();
			QuestionBase objQuestions = null;
			RestTemplate restTemplate = new RestTemplate();
			try {
				objQuestions = restTemplate.getForObject(url, QuestionBase.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (objQuestions != null) {
				objreport.setProblem(objQuestions.getStatement());
				List<String> options = objQuestions.getOptions();
				objreport.setOptions(options);
				objreport.setCorrectAnswer(objQuestions.getCorrectOption());
			}

			// objreport.setCorrectAnswer(objquestion.get().getCorrect_option());
			// objreport.setSlectedAnswer(objresult.getSelectedoption());

			objreport.setSlectedAnswer(objresult.getSelectedoption());
			objReports.add(objreport);
		}
		List<ScheduledRequest> userlist = scheduleRequestRepository.findByCandidateEmailId(candidateId);
		/*
		 * User user=getUserDetailsFromUserTable(format); //User us=user.get();
		 * List<User> userlist=new ArrayList<User>(); userlist.add(user);
		 * 
		 * String path = "C:\\mydownloads\\"; Path pathDir = Paths.get(path);
		 * 
		 * try { if (!Files.exists(pathDir)) { Files.createDirectories(pathDir);
		 * System.out.println("Directory created"); } else {
		 * System.out.println("Directory already exists"); } } catch(Exception e) {
		 * e.printStackTrace(); } String destFileName=path;
		 */
		byte arr[] = {};
		try {
			File file = ResourceUtils.getFile("classpath:Report.jrxml");
			JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("datasource1", subjReport);
			parameters.put("datasource2", objReports);
			parameters.put("datasource3", userlist);
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));
			arr = JasperExportManager.exportReportToPdf(print);
			// JasperExportManager.exportReportToPdfFile(print,
			// destFileName+"\\candidate.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arr;
	}

	private List<QuestionScheduler> getScheduled() {
		List<QuestionScheduler> scheduled = (List<QuestionScheduler>) questionScheduleRepository.findAll();
		return scheduled;
	}

	private List<QuestionScheduler> getScheduledById(String id) {
		List<QuestionScheduler> scheduled = (List<QuestionScheduler>) questionScheduleRepository
				.findAllByAssigneruid(id);
		return scheduled;
	}

	@Override
	public List<CandidateResult> getCandidateReports(String id) {

		List<CandidateResult> candidateResults = new ArrayList<CandidateResult>();
		List<QuestionScheduler> scheduled = null;
		String role = this.getUserDetailsFromUserTable(id).getRoleId();
		if (role.equals("1") || role.equals("2")) {
			scheduled = this.getScheduled();
		} else {
			scheduled = this.getScheduledById(id);
		}

		Map<String, String> candidates = new HashMap<String, String>();
		for (QuestionScheduler scheduledQ : scheduled) {
			candidates.put(scheduledQ.getAssigneduid(), scheduledQ.getAssigneruid());
		}
		Set<Entry<String, String>> candidateEntry = candidates.entrySet();
		for (Entry<String, String> candidate : candidateEntry) {
			String status = "Scheduled";
			String testScheduler = this.getUserDetailsFromUserTable(candidate.getValue()).getName();
			CandidateResult result = new CandidateResult();
			int noOfObjQ = 0;
			int correctAns = 0;
			String candidatename = this.getUserDetailsFromUserTable(candidate.getKey()).getName();
			List<ObjQuestionResult> objresults = objResultRepository.findByKeyUserId(candidate.getKey());
			for (ObjQuestionResult objResult : objresults) {
				String url = cctServiceHostPort + "question/";
				url = url + objResult.getKey().getQid();
				QuestionBase objQuestions = null;
				RestTemplate restTemplate = new RestTemplate();
				try {
					objQuestions = restTemplate.getForObject(url, QuestionBase.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// Optional<ObjQuestion>
				// objquestion=objQuestionRepository.findByqId(objResult.getKey().getQid());

				noOfObjQ++;
				if (objResult != null && objResult.getSelectedoption() != null) {
					if (objResult.getSelectedoption().equals(objQuestions.getCorrectOption())) {
						correctAns++;
					}
				}
			}

			String objResult = correctAns + " Correct Out Of " + noOfObjQ + " Objective Questions  # ";
			int finalsubjResult = 0;
			int subjectiveQResult = 0;
			int nofSubjectiveq = 0;
			List<SubjQuestionResult> subjResults = subjResultRepository.findByKeyUserId(candidate.getKey());
			for (SubjQuestionResult subjresult : subjResults) {
				nofSubjectiveq++;
				String subjResult = subjresult.getConsolidatedoutput();
				String subjper[] = {};
				if (subjResult != null) {
					subjper = subjResult.split(" ");
				}
				if (subjper.length > 0) {
					int nofTestcase = Integer.parseInt(subjper[2].substring(0, 1));
					int passedtest = (nofTestcase - Integer.parseInt(subjper[4].substring(0, 1)));
					subjectiveQResult = ((passedtest * 100) / nofTestcase);
				}
			}
			if (nofSubjectiveq != 0) {
				finalsubjResult = subjectiveQResult / nofSubjectiveq;
			}

			int percentage = 0;
			if (noOfObjQ != 0) {
				percentage = (((correctAns * 100) / noOfObjQ) + finalsubjResult) / 2;
			} else {
				percentage = (finalsubjResult);
			}

			if (!objresults.isEmpty() || !subjResults.isEmpty()) {
				status = "Submitted";
			}

			String finalResult = objResult + " Subjective- " + finalsubjResult + "%";
			result.setCandidateName(candidatename);
			result.setTestScheduler(testScheduler);
			result.setTestcaseReport(finalResult);
			result.setStatus(status);
			result.setTestCasePercentage(percentage);
			result.setId(candidate.getKey());
			candidateResults.add(result);
		}

		return candidateResults;
	}

	@Scheduled(cron = "${vcc.cnadidate.interview.date.cron}")
	@Override
	public List<String> sendTestLinkToCandidates() {
		Iterable<ScheduleChallenge> scheduleChallenges = scheduleChallengeRepository.findAll();
		List<ScheduleChallenge> challengList = new ArrayList<ScheduleChallenge>();
		scheduleChallenges.forEach(data -> challengList.add(data));
		List<ScheduleChallenge> filterChallengList = challengList.stream()
				.filter(data -> data.getStatus().equalsIgnoreCase("scheduled"))
				.collect(Collectors.toList());
		List<String> users = new ArrayList<String>();
		for(ScheduleChallenge challenge : filterChallengList) {
			if(null != challenge.getAssigneduid()) {
				if(fileterBasedOnCron(challenge)) {
					User user = getUserDetailsFromUserTable(challenge.getAssigneduid());
					if(null != user) {
						String email = sendEmailToCandidates(user);
						users.add(email);
					}
				}
			}
		}
		return users;
	}

	private boolean fileterBasedOnCron(ScheduleChallenge challenge) {
		long timeInMilliSec = challenge.getScheduleTime().getTime();
		long currTimeInMillSec = System.currentTimeMillis();
		long millSecForNext24Hours = currTimeInMillSec + millSecFor24Hours;
		return (timeInMilliSec >= currTimeInMillSec && timeInMilliSec <= millSecForNext24Hours) ? true : false;
	}
	
	private User getUserDetailsFromUserTable(String userId) {
		RestTemplate restTemplate = new RestTemplate();
		String url = aaServiceHostPort + "user/userid/" + userId;
		ResponseEntity<User> resultJson = null;
		try {
			resultJson = restTemplate.getForEntity(url, User.class);
			return resultJson.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private String sendEmailToCandidates(User user) {
		String json = prepareJsonForTestLink(user);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(json, headers);
		String url = emailServiceHostPort + "send-mail";
		try {
			restTemplate.postForEntity(url, request, String.class);
			return "Congratulations! Your mail has been send to the " + user.getUserId() + " successfully.";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}

	private String prepareJsonForTestLink(User user) {
		String temp = new String(testLinkEmailMsg + " ");
		temp = temp.replace("${name}", user.getName()).replace("${url}", userLoginUrl)
				.replace("${userId}", user.getUserId()).replace("${pass}", user.getPassword());
		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", user.getName());
		map.put("mailSubject", testLinkEmailSubject);
		map.put("toEmailAddress", user.getUserId());
		map.put("messageText", temp.trim());
		return returnJson(map);
	}

	@Override
	public List<QuestionSchedView> getQuestionsByAssignerId(String assignerId) {
		List<QuestionSchedView> questionIdList = questionScheduleRepository.findByAssigneruid(assignerId);
		return questionIdList;
	}

	@Override
	public List<String> sendCandidateReport(Interviewer interviewer) {
		List<String> interviewrIds = null;
		List<String> responseList = new ArrayList<String>();
		if (null != interviewer && null != interviewer.getToEmailIds()) {
			interviewrIds = Arrays.asList(interviewer.getToEmailIds().split(";"));
			User candidateDetails = getUserDetailsFromUserTable(interviewer.getCandidateId());
			for (String intrwrId : interviewrIds) {
				User interviewerDetails = getUserDetailsFromUserTable(intrwrId);
				if (null != interviewerDetails) {
					byte[] byteArray = getSubjObjResultReport(interviewer.getCandidateId());
					interviewerDetails.setByteAttachemenets(byteArray);
					String response = sendEmailWithDynamicAttachement(interviewerDetails, interviewer,
							candidateDetails.getName());
					responseList.add(response);
				}
			}
		}
		return responseList;
	}

	private String prepareJsonForReport(User user, Interviewer interviewer, String candidateName) {
		String msgBody = "";
		String subject = "";
		if (null != interviewer && null != interviewer.getBody()) {
			msgBody = interviewer.getBody();
		} else {
			msgBody = new String(reportToInterviewerMsg + " ");
			msgBody = msgBody.replace("${name}", user.getName());
		}

		subject = (null != interviewer && null != interviewer.getSubject()) ? subject = interviewer.getSubject()
				: reportEmailSubject;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", user.getName());
		map.put("mailSubject", subject);
		map.put("toEmailAddress", user.getUserId());
		map.put("messageText", msgBody.trim());
		map.put("attachement", user.getByteAttachemenets());
		map.put("candidateName", candidateName);
		try {
			return new ObjectMapper().writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String sendEmailWithDynamicAttachement(User user, Interviewer interviewer, String candidateName) {
		String json = prepareJsonForReport(user, interviewer, candidateName);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(json, headers);
		String url = emailServiceHostPort + "send/mail/with/dynamic/attachment";
		try {
			restTemplate.postForEntity(url, request, String.class);
			return "Successfully email sent to " + user.getUserId() + ".";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json;
	}

	private String deleteCandidateFromUserTable(ScheduledRequest scheduleRequest) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String url = aaServiceHostPort + "/user/" + scheduleRequest.getCandidateEmailId();
		try {
			restTemplate.delete(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	@Override
	public List<String> sendEamilToCandidateForTestLink(List<String> candidateEmailList) {
		List<String> users = new ArrayList<String>();
		for (String candidateId : candidateEmailList) {
			ScheduleChallenge challenge = scheduleChallengeRepository.findByAssigneduidAndStatus(candidateId,
					"Scheduled");
			if (null != challenge && null != challenge.getAssigneduid()) {
				User user = getUserDetailsFromUserTable(challenge.getAssigneduid());
				if (null != user) {
					String email = sendEmailToCandidates(user);
					users.add(email);
				}
			}
		}
		return users;
	}

	@Override
	public List<ScheduleChallenge> getChallengeRecByAssignerId(String assignerId) {
		List<ScheduleChallenge> challengeIdList = scheduleChallengeRepository.findByAssigneruid(assignerId);
		return challengeIdList;
	}

	@Override
	public String deleteChallenge(String challengeId) {
		Boolean result = scheduleChallengeRepository.existsByChallengeid(challengeId);
		scheduleChallengeRepository.deleteByChallengeid(challengeId);

		List<QuestionScheduler> quesList = questionScheduleRepository.findByChallengeid(challengeId);
		if (!quesList.isEmpty()) {
			deleteQEntry(quesList);
		}
		return "{ \"success\" : " + (result ? "true" : "false") + " }";
	}

	@Override
	public ScheduleChallenge updateChallenge(QuestionSchedulerCustom assignBulkQ) {
		QuestionScheduler assignQ;
		List<QuestionScheduler> assignQObjList = new ArrayList<QuestionScheduler>();

		ScheduleChallenge challengeRecord = scheduleChallengeRepository.findByChallengeid(assignBulkQ.getChallengeid());

		if (challengeRecord != null) {
			// challengeRecord.setChallengeid(challengeId);
			// challengeRecord.setAssigneduid(assignBulkQ.getAssigneduidList().get(0));
			// challengeRecord.setAssigneruid(assignBulkQ.getAssigneruid());
			challengeRecord.setScheduleTime(assignBulkQ.getScheduleTime());

			scheduleChallengeRepository.save(challengeRecord);

			List<QuestionScheduler> quesList = questionScheduleRepository
					.findByChallengeid(assignBulkQ.getChallengeid());
			if (!quesList.isEmpty()) {
				deleteQEntry(quesList);
			}

			List<String> qIdList = assignBulkQ.getQidList();

			for (String qId : qIdList) {
				assignQ = new QuestionScheduler(getId("SchQuest"), qId, challengeRecord.getAssigneduid(),
						challengeRecord.getAssigneruid(), challengeRecord.getChallengeid());
				assignQObjList.add(assignQ);
			}
			questionScheduleRepository.saveAll(assignQObjList);
		}
		return challengeRecord;
	}

	@Override
	public List<QuestionScheduler> getQuestionsByChallengeId(String challengeId) {
		List<QuestionScheduler> quesList = questionScheduleRepository.findByChallengeid(challengeId);
		return quesList;
	}

	@Override
	public List<QuestionSchedView> getQuestionsNotByChallengeId(String assigneduid, String challengeId) {
		List<QuestionScheduler> quesList = questionScheduleRepository.findByChallengeid(challengeId);
		if (!quesList.isEmpty()) {
			deleteQEntry(quesList);
		}

		List<QuestionSchedView> questionIdList = questionScheduleRepository.findByAssigneduid(assigneduid);
		return questionIdList;
	}

	@Override
	public List<QuestionScheduler> getQuestionsByCandidateId(String candidateId) {
		List<QuestionScheduler> questionIdList = new ArrayList<QuestionScheduler>();
		ScheduleChallenge challengeIdList = scheduleChallengeRepository.findByAssigneduidAndStatus(candidateId,
				"Scheduled");
		String challengeId = "";
		if (challengeIdList != null) {
			challengeId = challengeIdList.getChallengeid();
			questionIdList = questionScheduleRepository.findByChallengeid(challengeId);
		}
		return questionIdList;
	}
}
