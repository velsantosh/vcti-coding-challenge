package com.vcti.ct.SRVServices.repository;

import java.util.Optional;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.SRVServices.model.User;


public interface UserRepository  extends CrudRepository<User, String>{

	@Query(allowFiltering = true)
	Optional<User> findById(String id);
	Optional<User> findByUserId(String userId);
}
