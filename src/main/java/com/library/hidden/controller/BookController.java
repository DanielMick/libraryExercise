package com.library.hidden.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.hidden.model.Book;
import com.library.hidden.model.dto.BookDTO;
import com.library.hidden.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.getBookByIsbn(isbn);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public void addBook(@Valid @RequestBody BookDTO bookDTO) {
        bookService.addBook(bookDTO);
    }
    
    @GetMapping("/author/{name}")
    public List<Book> getBooksByAuthor(@PathVariable String name) {
        return bookService.getBooksByAuthor(name);
    }

    @GetMapping("/published-after/{date}")
    public List<Book> getBooksPublishedAfter(@PathVariable String date) {
        LocalDate publishedDate = LocalDate.parse(date);
        return bookService.getBooksPublishedAfter(publishedDate);
    }
}
