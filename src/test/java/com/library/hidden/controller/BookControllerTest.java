package com.library.hidden.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.library.hidden.model.Book;
import com.library.hidden.model.dto.BookDTO;
import com.library.hidden.service.BookService;

public class BookControllerTest {

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void testGetAllBooks() throws Exception {
		when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/books")).andExpect(status().isOk());
	}

	@Test
	public void testGetBookByIsbn_Success() throws Exception {
		Book book = new Book(1L, "Sample Book", "Author Name", "1234567890", LocalDate.now());
		when(bookService.getBookByIsbn("1234567890")).thenReturn(book);

		mockMvc.perform(get("/api/books/1234567890")).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Sample Book"));
	}

	@Test
	public void testGetBookByIsbn_NotFound() throws Exception {
		when(bookService.getBookByIsbn("1234567890")).thenReturn(null);

		mockMvc.perform(get("/api/books/1234567890")).andExpect(status().isNotFound());
	}

	@Test
	public void testAddBook() throws Exception {
		String bookJson = "{\"title\":\"New Book\",\"author\":\"New Author\",\"isbn\":\"12345\",\"publishedDate\":\"2023-01-01\"}";

		mockMvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(bookJson))
				.andExpect(status().isOk());

		verify(bookService, times(1)).addBook(any(BookDTO.class));
	}
}
