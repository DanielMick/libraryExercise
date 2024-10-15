package com.library.hidden.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.hidden.exception.FuturePublicationDateException;
import com.library.hidden.model.Book;
import com.library.hidden.model.dto.BookDTO;
import com.library.hidden.repository.BookRepository;

public class BookServiceTest {

	@InjectMocks
	private BookService bookService;

	@Mock
	private BookRepository bookRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddBook_Success() {
		BookDTO bookDTO = new BookDTO();
		bookDTO.setTitle("Sample Book");
		bookDTO.setAuthor("Author Name");
		bookDTO.setIsbn("1234567890");
		bookDTO.setPublishedDate(LocalDate.now().minusMonths(2));
		bookService.addBook(bookDTO);
		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setAuthor(bookDTO.getAuthor());
		book.setIsbn(bookDTO.getIsbn());
		book.setPublishedDate(bookDTO.getPublishedDate());
		verify(bookRepository, times(1)).save(book);
	}

	@Test
	public void testAddBook_FuturePublicationDate() {
		BookDTO bookDTO = new BookDTO();
		bookDTO.setTitle("Future Book");
		bookDTO.setAuthor("Future Author");
		bookDTO.setIsbn("0987654321");
		bookDTO.setPublishedDate(LocalDate.now().plusDays(1));

		Exception exception = assertThrows(FuturePublicationDateException.class, () -> {
			bookService.addBook(bookDTO);
		});

		assertEquals("Publication date cannot be in the future.", exception.getMessage());
	}

	@Test
	public void testGetAllBooks() {
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "Book 1", "Author 1", "12345", LocalDate.now()));
		books.add(new Book(2L, "Book 2", "Author 2", "67890", LocalDate.now()));

		when(bookRepository.findAll()).thenReturn(books);

		List<Book> result = bookService.getAllBooks();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetBooksByAuthor() {
		String author = "Author Name";
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "Book 1", author, "12345", LocalDate.now()));

		when(bookRepository.findByAuthor(author)).thenReturn(books);

		List<Book> result = bookService.getBooksByAuthor(author);
		assertEquals(1, result.size());
		assertEquals(author, result.get(0).getAuthor());
	}

	@Test
	public void testGetBooksPublishedAfter() {
		LocalDate date = LocalDate.now().minusDays(1);
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "Book 1", "Author 1", "12345", LocalDate.now()));

		when(bookRepository.findByPublishedDateAfter(date)).thenReturn(books);

		List<Book> result = bookService.getBooksPublishedAfter(date);
		assertEquals(1, result.size());
	}
}
