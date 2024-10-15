package com.library.hidden.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.hidden.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	
    Book findByIsbn(String isbn);
    List<Book> findByAuthor(String author);
    List<Book> findByPublishedDateAfter(LocalDate date);
}