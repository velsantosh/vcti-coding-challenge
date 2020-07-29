package com.vcti.ct.CCTServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.CCTServices.model.QuestionTemplate;

public interface QuestionTemplateRepository extends CrudRepository<QuestionTemplate, String> {

	@AllowFiltering
	List<QuestionTemplate> findByTechnologyAndExperienceAndDifficulty(String technology, String experience, String difficulty);
	
	@AllowFiltering
	Optional<QuestionTemplate> findById(String templateId);
	
	@AllowFiltering
	List<QuestionTemplate> findByTechnologyAndExperience(String technology, String experience);
	
}
