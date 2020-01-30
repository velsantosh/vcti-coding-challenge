package com.vcti.ct.SRVServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.QuestionScheduler;

public interface QuestionSchedulerRepository extends CrudRepository<QuestionScheduler, String> {
	@AllowFiltering
	List<QuestionScheduler> findByQid(String qid);
}