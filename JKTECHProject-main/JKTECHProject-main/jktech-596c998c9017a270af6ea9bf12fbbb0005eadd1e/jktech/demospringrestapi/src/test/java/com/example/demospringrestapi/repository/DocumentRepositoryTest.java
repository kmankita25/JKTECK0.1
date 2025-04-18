package com.example.demospringrestapi.repository;

import com.example.demospringrestapi.entities.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void testFindByContentContainingIgnoreCase() {

        Document doc = new Document(
                null,
                "Test Title",
                "Ankita",
                "text/plain",
                LocalDate.now(),
                "This document contains the keyword for testing."
        );
        documentRepository.save(doc);


        List<Document> found = documentRepository.findByContentContainingIgnoreCase("keyword");


        assertEquals(1, found.size(), "Should find one document containing the keyword");
        assertTrue(found.get(0).getContent().toLowerCase().contains("keyword"), "Content should contain 'keyword'");
    }
}
