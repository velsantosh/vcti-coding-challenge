package com.vcti.ct.CCTServices.repository;

import org.springframework.data.repository.CrudRepository;

import com.vcti.ct.CCTServices.model.Question;

public interface QuestionRepository extends CrudRepository<Question, String> {
}