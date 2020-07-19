package com.vcti.ct.CCTServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.QuestionTemplate;

public interface QuestionRepository extends CrudRepository<Question, String> {
	@Query(allowFiltering = true)
	List<Question> findByType(String type);

	@AllowFiltering
	List<Question> findByIdAndType(String id, String type);

	@AllowFiltering
	List<Question> findByExperienceAndDifficulty(String experience, String difficulty);

}