package com.vcti.ct.SRVServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.ScheduleChallenge;

public interface ScheduleChallengeRepository extends CrudRepository<ScheduleChallenge, String>{
	@AllowFiltering
	List<ScheduleChallenge> findByAssigneduid(String assigneduid);
	
	@Query(allowFiltering = true)
	boolean existsByChallengeid(String challengeid);
	
	@Query(allowFiltering = true)
	void deleteByChallengeid(String challengeid);
	
	@AllowFiltering
	ScheduleChallenge findByChallengeid(String challengeid);
	
	@AllowFiltering
	List<ScheduleChallenge> findByAssigneruid(String assigneruid);
	
	@AllowFiltering
	ScheduleChallenge findByAssigneduidAndStatus(String assigneduid, String status);

}
