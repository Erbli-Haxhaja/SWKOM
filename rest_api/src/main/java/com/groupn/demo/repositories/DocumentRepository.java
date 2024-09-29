package com.groupn.demo.repositories;

import com.groupn.demo.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Document, Long> {


}
