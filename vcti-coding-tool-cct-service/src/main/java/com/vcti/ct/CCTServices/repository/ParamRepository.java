package com.vcti.ct.CCTServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.CCTServices.model.Param;

public interface ParamRepository extends CrudRepository<Param, String> {
	@Query(allowFiltering = true)
	List<Param> findByqId(String qId);

	@Query(allowFiltering = true)
	void deleteByqId(String qId);

}