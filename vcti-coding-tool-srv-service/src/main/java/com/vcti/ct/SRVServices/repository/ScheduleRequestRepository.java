package com.vcti.ct.SRVServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.ScheduledRequest;

/**
 * @author sandeepkumar.yadav
 *
 */
public interface ScheduleRequestRepository extends CrudRepository<ScheduledRequest, String> {
	
	@AllowFiltering
	public List<ScheduledRequest> findByCandidateEmailId(String id);

}