package com.vcti.ct.SRVServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.QuestionSchedView;
import com.vcti.ct.SRVServices.model.QuestionScheduler;

public interface QuestionSchedulerRepository extends CrudRepository<QuestionScheduler, String> {
	@AllowFiltering
	List<QuestionScheduler> findByQid(String qid);

	@AllowFiltering
	List<QuestionScheduler> findAllByQid(List<String> qid);

	@AllowFiltering
	List<QuestionSchedView> findByAssigneduid(String assigneduid);
	
	@AllowFiltering
	List<QuestionSchedView> findByAssigneruid(String assigneruid);
	
	@Query(allowFiltering = true)
	void deleteByChallengeid(String challengeid);
	
	@AllowFiltering
	List<QuestionScheduler> findAllByAssigneruid(String assigneruid);
	
	@AllowFiltering
	List<QuestionScheduler> findByChallengeid(String challengeid);

	

}