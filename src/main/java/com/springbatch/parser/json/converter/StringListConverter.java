package com.springbatch.parser.json.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String>{

	private static final String SPLIT_CHAR = ";";
	
	@Override
	public String convertToDatabaseColumn(List<String> stringList) {
		return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		return dbData != null ? Arrays.asList(dbData.split(SPLIT_CHAR)) : Collections.emptyList();
	}

}
