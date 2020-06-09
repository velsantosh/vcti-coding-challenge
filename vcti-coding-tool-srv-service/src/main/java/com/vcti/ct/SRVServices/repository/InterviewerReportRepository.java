package com.vcti.ct.SRVServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.InterviewerReport;

public interface InterviewerReportRepository extends CrudRepository<InterviewerReport, String>{
	
	@AllowFiltering
	List<InterviewerReport> findByInterviewerid(String interviewerid);

}
