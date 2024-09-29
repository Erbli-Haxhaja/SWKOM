package com.groupn.demo.controllers;

import com.groupn.demo.entities.Document;
import com.groupn.demo.exceptions.BookNotFoundException;
import com.groupn.demo.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> hello() {
        String data = "<h1>Hi there, this is the book store!</h1>"
                + "<h2>Our Books:</h2>"
                + "<ul>"
                + "<li>Book 1: The Great Gatsby</li>"
                + "<li>Book 2: To Kill a Mockingbird</li>"
                + "<li>Book 3: 1984</li>"
                + "</ul>";

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/createBooks")
    public Document createBook(@RequestBody Document document) {
        return repository.save(document);
    }

    @CrossOrigin
    @GetMapping("/books")
    public List<Document> getBooks() {
        return repository.findAll();
    }

    @CrossOrigin
    @GetMapping("/books/{id}")
    public Optional<Document> getBook(@PathVariable Long id) {
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
    public Document updateBook(@RequestBody Document document, @PathVariable Long id) {
        document.setId(id);
        return repository.save(document);
    }

}
