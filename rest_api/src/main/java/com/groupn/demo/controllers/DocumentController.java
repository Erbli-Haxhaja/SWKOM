package com.groupn.demo.controllers;

import com.groupn.demo.dto.DocumentDTO;
import com.groupn.demo.entities.Document;
import com.groupn.demo.rabbitmq.RabbitMQSender;
import com.groupn.demo.repositories.DocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository repository;
    private final RabbitMQSender rabbitMQSender;

    public DocumentController(DocumentRepository repository, RabbitMQSender rabbitMQSender) {
        this.repository = repository;
        this.rabbitMQSender = rabbitMQSender;
    }

    @CrossOrigin
    @PostMapping("/save")
    public ResponseEntity<Document> saveDocument(@ModelAttribute DocumentDTO documentDTO) {
        try {
            // Convert DTO to Document entity
            Document document = Document.builder()
                    .name(documentDTO.getName())
                    .description(documentDTO.getDescription())
                    .pages(documentDTO.getPages())
                    .pdfDocument(documentDTO.getFile().getBytes())
                    .build();

            Document savedDocument = repository.save(document);
            rabbitMQSender.sendMessage("Document saved: " + savedDocument.getName());
            return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            rabbitMQSender.sendMessage("Error saving document: " + documentDTO.getName());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @GetMapping("/getAll")
    public ResponseEntity<List<Map<String, Object>>> getDocuments() {
        List<Map<String, Object>> documents = repository.findAll().stream()
                .map(document -> {
                    Map<String, Object> docMap = new HashMap<>();
                    docMap.put("id", document.getId());
                    docMap.put("name", document.getName());
                    docMap.put("description", document.getDescription());
                    docMap.put("pages", document.getPages());
                    // Encode the pdfDocument as Base64
                    docMap.put("pdfDocument", Base64.getEncoder().encodeToString(document.getPdfDocument()));
                    return docMap;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        Optional<Document> document = repository.findById(id);
        if (document.isPresent()) {
            rabbitMQSender.sendMessage("Document viewed with ID: " + id);
            return ResponseEntity.ok(document.get());
        } else {
            rabbitMQSender.sendMessage("Error viewing document with ID: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PutMapping("/update/{id}")
    public ResponseEntity<Document> updateDocument(
            @RequestBody Document document,
            @PathVariable Long id) {
        Optional<Document> documentData = repository.findById(id);

        if (documentData.isPresent()) {
            Document existingDocument = documentData.get();
            existingDocument.setName(document.getName());
            existingDocument.setDescription(document.getDescription());
            existingDocument.setPages(document.getPages());
            existingDocument.setPdfDocument(document.getPdfDocument());
            Document updatedDocument = repository.save(existingDocument);
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteDocument(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            rabbitMQSender.sendMessage("Document deleted with ID: " + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            rabbitMQSender.sendMessage("Error deleting document with ID: " + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
