package com.vcti.ct.SRVServices.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vcti.ct.SRVServices.dao.SRVDataService;
import com.vcti.ct.SRVServices.model.ObjQuestionResult;
import com.vcti.ct.SRVServices.model.QuestionScheduler;
import com.vcti.ct.SRVServices.model.SubjQuestionResult;
import com.vcti.ct.SRVServices.repository.ObjResultRepository;
import com.vcti.ct.SRVServices.repository.QuestionSchedulerRepository;
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

	@Override
	public boolean assignUser(QuestionScheduler assignQ) {
		assignQ.setId(UUID.randomUUID().toString());
		questionScheduleRepository.save(assignQ);
		return true;
	}

	@Override
	public boolean unAssignUser(QuestionScheduler unAssignQ) {

		deleteQEntry(getQList(unAssignQ));
		return true;
	}

	private List<QuestionScheduler> getQList(QuestionScheduler schQues) {
		List<QuestionScheduler> quesList = questionScheduleRepository.findByQid(schQues.getQid());
		List<QuestionScheduler> criteriaMatchedList = new ArrayList<QuestionScheduler>();
		for (QuestionScheduler ques : quesList) {
			// when no assigner user and assigned user is provided
			if (schQues.getAssigneruid() == null && schQues.getAssigneduid() == null) {
				criteriaMatchedList.add(ques);
				continue;
			}
			if (schQues.getAssigneruid() != null && schQues.getAssigneruid().equals(ques.getAssigneruid())) {
				criteriaMatchedList.add(ques);
			}
			if (schQues.getAssigneduid() != null && schQues.getAssigneduid().equals(ques.getAssigneduid())) {
				criteriaMatchedList.add(ques);
			}
		}
		return criteriaMatchedList;
	}

	private void deleteQEntry(List<QuestionScheduler> quesList) {
		quesList.forEach(ques -> questionScheduleRepository.deleteById(ques.getId()));
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

}
