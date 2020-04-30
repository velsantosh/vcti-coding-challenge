package com.vcti.ct.SRVServices.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vcti.ct.SRVServices.dao.SRVDataService;
import com.vcti.ct.SRVServices.exceptions.InvalidScheduleRequestIdException;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.QuestionSchedulerCustom;
import com.vcti.ct.SRVServices.model.ScheduleRequest;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;
import com.vcti.ct.SRVServices.repository.ObjResultRepository;
import com.vcti.ct.SRVServices.repository.QuestionSchedulerRepository;
import com.vcti.ct.SRVServices.repository.ScheduleRequestRepository;
import com.vcti.ct.SRVServices.repository.SubjResultRepository;

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

	@Value("${vcc.aa.service.host.port}")
	private String aaServiceHostPort;

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
		String password = userId.substring(0, userId.indexOf("@") + 1) + "vspl";
		return password;
	}

	private String makeJson(ScheduleRequest scheduleRequest) {
		String userId = scheduleRequest.getCandidateEmailId();
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("name", scheduleRequest.getCandidateName());
		map.put("userId", userId);
		map.put("experience", scheduleRequest.getCandidateExperience());
		map.put("password", generatePassword(userId));
		map.put("roleId", "CANDIDATE");
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
}
