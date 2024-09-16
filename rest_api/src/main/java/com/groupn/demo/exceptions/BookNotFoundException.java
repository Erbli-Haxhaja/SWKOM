package com.groupn.demo.exceptions;

import lombok.Getter;

public class BookNotFoundException extends RuntimeException {
    @Getter
    private final long id;
    public BookNotFoundException(Long id) {
        super("Could not find book " + id);
        this.id = id;
    }
}