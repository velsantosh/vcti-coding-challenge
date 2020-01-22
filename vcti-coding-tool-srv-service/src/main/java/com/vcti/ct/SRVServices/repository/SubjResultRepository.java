package com.vcti.ct.SRVServices.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.SubjQuestionResult;

public interface SubjResultRepository extends CrudRepository<SubjQuestionResult, String> {

	List<SubjQuestionResult> findByKeyUserIdAndKeyQid(String userId, String qid);

	List<SubjQuestionResult> findByKeyQid(String qid);

	List<SubjQuestionResult> findByKeyUserId(String userId);
}