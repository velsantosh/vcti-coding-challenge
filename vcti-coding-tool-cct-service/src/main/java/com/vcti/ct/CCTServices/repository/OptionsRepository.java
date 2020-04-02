package com.vcti.ct.CCTServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.CCTServices.model.Options;

public interface OptionsRepository extends CrudRepository<Options, String> {

	@AllowFiltering
	List<Options> findByqId(String qId);

	@AllowFiltering
	void deleteByqId(String qId);

}