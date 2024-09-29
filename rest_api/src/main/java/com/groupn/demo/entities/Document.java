package com.groupn.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="Document")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Document {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private int pages;
    @Lob
    private byte[] pdfDocument;
}
