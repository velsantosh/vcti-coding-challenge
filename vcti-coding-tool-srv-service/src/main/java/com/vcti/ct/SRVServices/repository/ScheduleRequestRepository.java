package com.vcti.ct.SRVServices.repository;

import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.ScheduledRequest;

/**
 * @author sandeepkumar.yadav
 *
 */
public interface ScheduleRequestRepository extends CrudRepository<ScheduledRequest, String> {

}