package com.vcti.ct.CCTServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.CCTServices.model.Technology;

/**
 * @author sandeepkumar.yadav
 *
 */
public interface TechnologyRepository extends CrudRepository<Technology, String> {
	@Query(allowFiltering = true)
	List<Technology> findByTechnology(String uName);
}