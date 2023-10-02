package com.springbatch.parser.processor;

import org.springframework.batch.item.ItemProcessor;

import com.springbatch.parser.entity.Book;

public class BookProcessor implements ItemProcessor<Book, Book> {

	@Override
	public Book process(Book book) throws Exception {
		return book;
	}

}
