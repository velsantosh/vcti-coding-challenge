package com.vcti.ct.AAServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.AAServices.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	@Query(allowFiltering = true)
	List<User> findByUserIdAndPassword(String userName, String password);

	@Query(allowFiltering = true)
	Optional<User> findById(String id);

	@Query(allowFiltering = true)
	List<User> findByUserId(String uName);

	@Query(allowFiltering = true)
	List<User> findByRoleId(String roleId);

	@Query(allowFiltering = true)
	void deleteByUserId(String uName);

	@Query(allowFiltering = true)
	boolean existsByUserId(String uName);
}