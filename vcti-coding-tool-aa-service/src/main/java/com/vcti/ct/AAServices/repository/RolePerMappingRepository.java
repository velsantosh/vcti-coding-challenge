package com.vcti.ct.AAServices.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.AAServices.model.Rolepermapping;

public interface RolePerMappingRepository extends CrudRepository<Rolepermapping, String> {
	@Query(allowFiltering = true)
	List<Rolepermapping> findByRoleId(String roleId);
	
}