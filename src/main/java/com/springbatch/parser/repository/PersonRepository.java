package com.springbatch.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbatch.parser.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}
