package com.springbatch.parser.entity;

import java.util.List;

import com.springbatch.parser.json.converter.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private String isbn;
	@Column(name = "BOOK_YEAR")
	private int year;
	@Column(name = "BOOK_LANGUAGE")
	private String language;
	private int pages;
	@Convert(converter = StringListConverter.class)
	@Column(name = "AUTHORS", nullable = false)
	private List<String> authors;
	@Convert(converter = StringListConverter.class)
	@Column(name = "PUBLISHERS", nullable = false)
	private List<String> publishers;
}
