package com.vcti.ct.AAServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.AAServices.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	@Query(allowFiltering = true)
	List<User> findByUserNameAndPassword(String userName, String password);
	
	@Query(allowFiltering = true)
	Optional<User> findById(String id);
	
	@Query(allowFiltering = true)
	List<User> findByUserName(String uName);
	
	@Query(allowFiltering = true)
	void deleteByUserName(String uName);
	
	@Query(allowFiltering = true)
	boolean existsByUserName (String uName);
}