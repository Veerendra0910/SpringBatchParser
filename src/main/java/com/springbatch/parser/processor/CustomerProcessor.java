package com.springbatch.parser.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springbatch.parser.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer customer) throws Exception {
		return customer;
	}

}
