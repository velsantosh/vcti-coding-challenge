package com.vcti.ct.SRVServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.ObjQuestionResult;

public interface ObjResultRepository extends CrudRepository<ObjQuestionResult, String> {
	List<ObjQuestionResult> findByKeyUserId(String userId);
	@AllowFiltering
	List<ObjQuestionResult> findByKeyQid(String userId);
	List<ObjQuestionResult> findByKeyUserIdAndKeyQid(String userId, String qId);
}