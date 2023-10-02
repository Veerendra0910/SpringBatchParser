package com.springbatch.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbatch.parser.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
