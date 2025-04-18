package com.example.demospringrestapi.controller;

import com.example.demospringrestapi.Dto.QAResponseDTO;
import com.example.demospringrestapi.entities.Document;
import com.example.demospringrestapi.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired

    private DocumentService documentService;

    @PostMapping("/savedocuments")
    public ResponseEntity<String> savedocuments(
            @RequestParam("file") MultipartFile[] files,
            @RequestParam("author") String author) {

        for (MultipartFile file : files) {
            documentService.saveDocument(file, author);
        }

        return ResponseEntity.ok("Documents uploaded successfully");
    }


    @GetMapping("/qa")
    public ResponseEntity<Map<String, Object>> getAnswer(@RequestParam("question") String question) {
        List<QAResponseDTO> results = documentService.findMatchingSnippets(question);

        Map<String, Object> response = new HashMap<>();
        response.put("answers", results);
        response.put("count", results.size());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/filter")
    public Page<QAResponseDTO> filterDocuments(
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "uploadDate") String sortBy
    ) {
        return documentService.filterDocuments(author, page, size, sortBy);
    }




}
