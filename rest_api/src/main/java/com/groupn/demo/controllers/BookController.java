package com.groupn.demo.controllers;

import com.groupn.demo.entities.Book;
import com.groupn.demo.exceptions.BookNotFoundException;
import com.groupn.demo.repositories.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
public class BookController {
    private final BookRepository repository;
    public BookController(BookRepository repository) {
        this.repository = repository;
    }
    @CrossOrigin
    @GetMapping("/")
    public String hello() {
        return "<h1>Hi there, this is the book store!</h1>";
    }

    @CrossOrigin
    @PostMapping("/createBooks")
    public Book createBook(@RequestBody Book book) {
        return repository.save(book);
    }

    @CrossOrigin
    @GetMapping("/books")
    public List<Book> getBooks() {
        return repository.findAll();
    }

    @CrossOrigin
    @GetMapping("/books/{id}")
    public Optional<Book> getBook(@PathVariable Long id) {
        var book = repository.findById(id);
        if ( book==null ) {
            throw new BookNotFoundException(id);
        }
        else {
            return book;
        }
    }
    @CrossOrigin
    @PutMapping("/books/{id}")
    public Book updateBook( @RequestBody Book book, @PathVariable Long id) {
        book.setId(id);
        return repository.save(book);
    }

}
