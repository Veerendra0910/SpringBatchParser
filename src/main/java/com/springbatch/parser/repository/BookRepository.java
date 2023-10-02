package com.springbatch.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springbatch.parser.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
