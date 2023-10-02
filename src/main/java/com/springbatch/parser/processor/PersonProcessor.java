package com.springbatch.parser.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springbatch.parser.entity.Person;

public class PersonProcessor implements ItemProcessor<Person, Person> {

	@Override
	public Person process(Person item) throws Exception {
		return item;
	}

}
