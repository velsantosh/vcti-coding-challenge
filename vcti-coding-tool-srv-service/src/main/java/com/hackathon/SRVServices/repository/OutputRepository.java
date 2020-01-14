package com.hackathon.SRVServices.repository;

import org.springframework.data.repository.CrudRepository;

import com.hackathon.SRVServices.model.Output;

public interface OutputRepository extends CrudRepository<Output, String> {
}