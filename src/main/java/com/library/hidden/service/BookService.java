package com.library.hidden.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.hidden.exception.FuturePublicationDateException;
import com.library.hidden.model.Book;
import com.library.hidden.model.dto.BookDTO;
import com.library.hidden.repository.BookRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getBookByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Transactional
	public void addBook(BookDTO bookDTO) {
		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setAuthor(bookDTO.getAuthor());
		book.setIsbn(bookDTO.getIsbn());
		book.setPublishedDate(bookDTO.getPublishedDate());
		if (book.getPublishedDate().isAfter(LocalDate.now())) {
			throw new FuturePublicationDateException("Publication date cannot be in the future.");
		}
		
		bookRepository.save(book);
	}

	public List<Book> getBooksByAuthor(String author) {
		return bookRepository.findByAuthor(author);
	}

	public List<Book> getBooksPublishedAfter(LocalDate date) {
		return bookRepository.findByPublishedDateAfter(date);
	}
}