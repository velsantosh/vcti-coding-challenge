package com.vcti.ct.SRVServices.dao.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

import javax.annotation.PostConstruct;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

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
import com.vcti.ct.SRVServices.exceptions.InvalidScheduleRequestIdException;
import com.vcti.ct.SRVServices.model.CandidateResult;
import com.vcti.ct.SRVServices.model.ObjQuestion;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.ObjectiveResultReport;
import com.vcti.ct.SRVServices.model.QuesResponse;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
import com.vcti.ct.SRVServices.model.ScheduleRequest;
import com.vcti.ct.SRVServices.model.SubjQuestion;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;
import com.vcti.ct.SRVServices.model.SubjectiveResultReport;
import com.vcti.ct.SRVServices.model.User;
import com.vcti.ct.SRVServices.model.ValidateSubjQuestions;
import com.vcti.ct.SRVServices.repository.ObjQuestionRepository;
import com.vcti.ct.SRVServices.repository.ObjResultRepository;
import com.vcti.ct.SRVServices.repository.QuestionSchedulerRepository;
import com.vcti.ct.SRVServices.repository.ScheduleRequestRepository;
import com.vcti.ct.SRVServices.repository.SubjQuestionRepository;
import com.vcti.ct.SRVServices.repository.SubjResultRepository;
import com.vcti.ct.SRVServices.repository.UserRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
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
	UserRepository userRepository;
	@Autowired 
    ObjQuestionRepository objQuestionRepository;
	@Autowired
    SubjQuestionRepository subjQuestionRepository;

	@Value("${vcc.aa.service.host.port}")
	private String aaServiceHostPort;

	@Value("${vcc.email.service.host.port}")
	private String emailServiceHostPort;
	
	@Value("${vcc.candidate.email.subject}")
	private String emailSubject;
	
	@Value("${vcc.user.login.url}")
	private String userLoginUrl;
	
	@Value("${vcc.schedule.request.cron.isEnabled}")
	private Boolean isCronEnabled;
	
	@Value("${vcc.schedule.request.cron.time}")
	private String scheduleRequestCronTime;
	
	private String emailMsg;
	
	@PostConstruct
	public void init() {
		try {
			emailMsg = new String(Files.readAllBytes(Paths.get(ResourceUtils.getFile("classpath:emailMsg.txt").toString())));
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
		QuestionScheduler assignQ;
		List<String> qIdList = assignBulkQ.getQidList();
		if (qIdList == null) {
			System.out.println("No Question Id specified.");
			return false;
		}
		List<String> assignedUserIdList = assignBulkQ.getAssigneduidList();
		for (String qId : qIdList) {
			for (String userId : assignedUserIdList) {
				assignQ = new QuestionScheduler(getId("SchQuest"), qId, userId,
						assignBulkQ.getAssigneruid());
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
				unAssignQ = new QuestionScheduler("", qId, userId, unAssignBulkQ.getAssigneruid());
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
	@Override
	public boolean addSubjQResult(SubjQuestionResult subjQRes) {
		subjResultRepository.save(subjQRes);
		return true;
	}

	@Override
	public boolean addSubjQResultList(List<SubjQuestionResult> subjQResList) {
		subjResultRepository.saveAll(subjQResList);
		return true;
	}
*/
	@Override
	public boolean addSubjQResult(SubjQuestionResult subjQRes) {
		QuesResponse questionresponse = this.getCompilationsStatus(subjQRes);
		if (null != questionresponse) {
			subjQRes.setCompilationStatus(questionresponse.getCompilationsStatus());
			subjResultRepository.save(subjQRes);
			return (null != questionresponse.getCompilationsStatus()
					&& questionresponse.getCompilationsStatus().equalsIgnoreCase("SUCCESS")) ? true : false;
		}
		return false;
	}
      private QuesResponse getCompilationsStatus(SubjQuestionResult subjQRes) {
  	  final String uri = "http://localhost:8082/validateSubjQues/";
  	  ValidateSubjQuestions request=new ValidateSubjQuestions();
  	  QuesResponse qr=new QuesResponse();
  	  qr.setqId(subjQRes.getKey().getQid());
  	  qr.setUserInput(subjQRes.getProgram().toString());
  	  request.setUserId(subjQRes.getKey().getUserId());
  	  request.setQuesResponseObj(qr);
  	  request.setClassName(subjQRes.getClassName());
  	  RestTemplate restTemplate = new RestTemplate();
  	  QuesResponse  questionResponse= null;
  	  try {
  		questionResponse=restTemplate.postForObject(uri, request, QuesResponse.class);
  	
  	  }
  		catch(Exception e)	  {
  			System.out.println(e);
  		}
  	// QuesResponse  questionResponse=restTemplate.getForObject(uri,QuesResponse.class);
  	 return questionResponse;
    }
	  @Override
	public boolean addSubjQResultList(List<SubjQuestionResult> subjQResList) {
		List<SubjQuestionResult> subQResultList=new ArrayList<SubjQuestionResult>();
		for(int i=0;i<subjQResList.size();i++) {
			SubjQuestionResult subjQRes=subjQResList.get(i);
			QuesResponse questionresponse=this.getCompilationsStatus(subjQRes);
			subjQRes.setCompilationStatus(questionresponse.getCompilationsStatus());
			subQResultList.add(subjQRes);
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
	public ScheduleRequest scheduleRequest(ScheduleRequest scheduleRequest) {
		saveCandidateInUserTable(scheduleRequest);
		scheduleRequest.setId(getId("SchReq"));
		scheduleRequest.setRequestedDate(getCurrentDate());
		ScheduleRequest scheduledRequest = scheduleRequestRepository.save(scheduleRequest);
		return scheduledRequest;
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

	private String saveCandidateInUserTable(ScheduleRequest scheduleRequest) {
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

	private String makeJson(ScheduleRequest scheduleRequest) {
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
	public List<ScheduleRequest> getAllScheduledRequest() {
		Iterator<ScheduleRequest> itr = scheduleRequestRepository.findAll().iterator();
		List<ScheduleRequest> allData = new ArrayList<ScheduleRequest>();
		while(itr.hasNext()) {
			allData.add(itr.next());
		}
		return allData;
	}

	@Override
	public ScheduleRequest updateScheduleRequest(ScheduleRequest scheduleRequest, String id) {
		scheduleRequest.setId(id);
		return updateScheduleRequest(scheduleRequest);
	}
	
	private ScheduleRequest updateScheduleRequest(ScheduleRequest scheduleRequest) {
		saveCandidateInUserTable(scheduleRequest);
		scheduleRequest.setUpdatedDate(getCurrentDate());
		ScheduleRequest scheduledRequest = scheduleRequestRepository.save(scheduleRequest);
		return scheduledRequest;
	}

	@Override
	public ScheduleRequest deleteScheduleRequest(String id) {
		Optional<ScheduleRequest> scheduleRequest = scheduleRequestRepository.findById(id);
		if(scheduleRequest.isPresent()) {
			scheduleRequestRepository.deleteById(id);
			return scheduleRequest.get();
		} else {
			throw new InvalidScheduleRequestIdException("Invalid schedule request Id");
		}
	}


	@Override
	public byte[] getSubjObjResultReport(String format)  {
		List<SubjectiveResultReport> subjReport=new ArrayList<SubjectiveResultReport>();
		List<SubjQuestionResult> subjQResults=subjResultRepository.findByKeyUserId(format);
		for(SubjQuestionResult subj:subjQResults) {
			Optional<SubjQuestion> subjQuestion=subjQuestionRepository.findByqId(subj.getKey().getQid());
			SubjectiveResultReport sReport=new SubjectiveResultReport();
			sReport.setProgram(subjQuestion.get().getStatement());
			sReport.setAnsSubmitted(subj.getProgram());
			sReport.setConsolidatedOutput(subj.getConsolidatedoutput());
			subjReport.add(sReport);
		}
		List<ObjQuestionResult> objresults=objResultRepository.findByKeyUserId(format);
		List<ObjectiveResultReport> objReports=new ArrayList<ObjectiveResultReport>();
		for(ObjQuestionResult objresult:objresults) {
			ObjectiveResultReport objreport=new ObjectiveResultReport();
			Optional<ObjQuestion> objquestion=objQuestionRepository.findByqId(objresult.getKey().getQid());
			objreport.setProblem(objquestion.get().getStatement());
			objreport.setOption(objquestion.get().getOptions());
			objreport.setCorrectAnswer(objquestion.get().getCorrect_option());
			objreport.setSlectedAnswer(objresult.getSelectedoption());
			objReports.add(objreport);
		}
		byte arr[]= {};
		try {
			File file=ResourceUtils.getFile("classpath:Report.jrxml");
			JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());
			Map<String,Object> parameters=new HashMap<String,Object>();
			parameters.put("datasource1", subjReport);
			parameters.put("datasource2", objReports);
			JasperPrint print=JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
		    arr=JasperExportManager.exportReportToPdf(print);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return arr;
	}

	private List<QuestionScheduler> getScheduled(){
		List<QuestionScheduler> scheduled=(List<QuestionScheduler>) questionScheduleRepository.findAll();
		return scheduled;
	}
	@Override
	public List<CandidateResult> getCandidateReports() {
		
		List<CandidateResult> candidateResults=new ArrayList<CandidateResult>();
		List<QuestionScheduler> scheduled=this.getScheduled();
		
		
		Map<String,String> candidates=new HashMap<String,String>();
		for(QuestionScheduler scheduledQ:scheduled) {
			candidates.put(scheduledQ.getAssigneduid(),scheduledQ.getAssigneruid());
		}
		Set<Entry<String,String>> candidateEntry=candidates.entrySet();
		for(Entry<String,String> candidate:candidateEntry) {
			String status="Scheduled";
			String testScheduler=userRepository.findById(candidate.getValue()).get().getName();
			CandidateResult result=new CandidateResult();
			int noOfObjQ=0;
			int correctAns=0;
			String candidatename=userRepository.findById(candidate.getKey()).get().getName();
			List<ObjQuestionResult> objresults=objResultRepository.findByKeyUserId(candidate.getKey());
			for(ObjQuestionResult objResult:objresults) {
				
				Optional<ObjQuestion> objquestion=objQuestionRepository.findByqId(objResult.getKey().getQid());
				
				noOfObjQ++;
				if(objResult!=null && objResult.getSelectedoption()!=null) {
				if(objResult.getSelectedoption().equals(objquestion.get().getCorrect_option())) {
					correctAns++;
				}
				}
			}
			
			String objResult= correctAns+" Correct Out Of "+noOfObjQ+" Objective Questions  # ";
			String subjResult="";
			List<SubjQuestionResult> subjResults=subjResultRepository.findByKeyUserId(candidate.getKey());
			for(SubjQuestionResult subjresult:subjResults) {
				subjResult=subjresult.getConsolidatedoutput();
			}
			if(!objresults.isEmpty() || !subjResults.isEmpty()) {
				status="Submitted";
			}
			String subjper[]=subjResult.split(" ");
			int nofTestcase=Integer.parseInt(subjper[subjper.length-1]);
			int passedtest=Integer.parseInt(subjper[0]);
			int percentage=(((correctAns*100)/noOfObjQ)+((passedtest*100)/nofTestcase))/2;
			
			String finalResult=objResult+" Subjective- "+subjResult;
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

//	@Override
//	public List<String> candidateSendEmail() {
//		Iterator<ScheduleRequest> itr = scheduleRequestRepository.findAll().iterator();
//		List<String> users = new ArrayList<String>();
//		while(itr.hasNext()) {
//			ScheduleRequest sr = itr.next();
//			if(null != sr.getCandidateEmailId()) {
//				List<QuestionSchedView> questionIdList = questionScheduleRepository.findByAssigneduid(sr.getCandidateEmailId());
//				User user = null;
//				if(null != questionIdList && !questionIdList.isEmpty()) {
//					user = getCandidateDetailsFromUserTable(sr.getCandidateEmailId());
//					if(null != user) {
//						String email = sendEmail(user);
//						users.add(email);
//					}
//				}
//			}
//		}
//		return users;
//	}
	
	@Override
	public List<String> candidateSendEmail() {
		Iterable<ScheduleRequest> itr = scheduleRequestRepository.findAll();
		List<ScheduleRequest> schRequest = new ArrayList<ScheduleRequest>();
		itr.forEach(data -> schRequest.add(data));
		if(isCronEnabled) {
			filterByScheduledDate(schRequest);
		}
		List<String> users = new ArrayList<String>();
		for(ScheduleRequest sr : schRequest) {
			if(null != sr.getCandidateEmailId()) {
				List<QuestionSchedView> questionIdList = questionScheduleRepository.findByAssigneduid(sr.getCandidateEmailId());
				User user = null;
				if(null != questionIdList && !questionIdList.isEmpty()) {
					user = getCandidateDetailsFromUserTable(sr.getCandidateEmailId());
					if(null != user) {
						String email = sendEmail(user);
						users.add(email);
					}
				}
			}
		}
		return users;
	}
	
	private void filterByScheduledDate(List<ScheduleRequest> schRequest) {
		// TODO Auto-generated method stub
		
	}

	private User getCandidateDetailsFromUserTable(String userId) {
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
	
	private String sendEmail(User user) {
		String json = prepareJson(user);
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
	
	private String prepareJson(User user) {
		String temp = new String(emailMsg + " ");
		temp = temp.replace("${name}", user.getName()).
				replace("${url}", userLoginUrl).
				replace("${userId}", user.getUserId()).
				replace("${pass}", user.getPassword());
		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", user.getName());
		map.put("mailSubject", emailSubject);
		map.put("toEmailAddress", user.getUserId());
		map.put("messageText", temp.trim());
		return returnJson(map);
	}
	
	@Override
	public List<QuestionSchedView> getQuestionsByAssignerId(String assignerId) {
		List<QuestionSchedView> questionIdList = questionScheduleRepository.findByAssigneruid(assignerId);
		return questionIdList;
	}
}
