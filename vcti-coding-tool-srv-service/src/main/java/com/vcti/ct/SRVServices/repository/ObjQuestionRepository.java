package com.vcti.ct.SRVServices.repository;

import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.ObjQuestion;



public interface ObjQuestionRepository extends CrudRepository<ObjQuestion, String>{

	@AllowFiltering
	Optional<ObjQuestion> findByqId(String qId);
}
