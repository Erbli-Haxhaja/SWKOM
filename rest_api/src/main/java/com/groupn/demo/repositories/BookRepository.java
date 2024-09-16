package com.groupn.demo.repositories;

import com.groupn.demo.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {


}
