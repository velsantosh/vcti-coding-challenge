package com.vcti.ct.AAServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.AAServices.model.Role;

public interface RoleRepository extends CrudRepository<Role, String> {
	@Query(allowFiltering = true)
	List<Role> findDistinctByRoleName(String roleName);
}