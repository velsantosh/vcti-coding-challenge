package com.vcti.ct.SRVServices.repository;

import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;


import com.vcti.ct.SRVServices.model.SubjQuestion;

public interface SubjQuestionRepository extends CrudRepository<SubjQuestion, String> {

	@AllowFiltering
	Optional<SubjQuestion> findByqId(String userId);
}
