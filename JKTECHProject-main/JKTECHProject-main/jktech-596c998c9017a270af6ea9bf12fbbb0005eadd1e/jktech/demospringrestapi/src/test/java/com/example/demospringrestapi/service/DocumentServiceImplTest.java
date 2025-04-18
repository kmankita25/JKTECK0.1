package com.example.demospringrestapi.service;

import com.example.demospringrestapi.Dto.QAResponseDTO;
import com.example.demospringrestapi.entities.Document;
import com.example.demospringrestapi.repository.DocumentRepository;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDocument() throws IOException, TikaException {

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test Ankita".getBytes());


        documentService.saveDocument(file, "Testing");


        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository).save(captor.capture());

        Document saved = captor.getValue();
        assertEquals("test.txt", saved.getTitle());
        assertEquals("Testing", saved.getAuthor());
        assertNotNull(saved.getContent());
        assertEquals(LocalDate.now(), saved.getUploadDate());
    }

    @Test
    void testFindMatchingSnippets() {

        Document doc = new Document(1L, "Document Title", "Ankita", "text/plain", LocalDate.now(), "Keyword Ankita.");
        when(documentRepository.findByContentContainingIgnoreCase("keyword")).thenReturn(List.of(doc));


        List<QAResponseDTO> result = documentService.findMatchingSnippets("keyword");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Document Title", result.get(0).getTitle());
        assertTrue(result.get(0).getSnippet().toLowerCase().contains("keyword"));
    }

    @Test
    void testFilterDocuments() {

        Document doc1 = new Document(1L, "Java Basics", "Ankita", "text/plain", LocalDate.now(), "Java is a programming language.");
        Document doc2 = new Document(2L, "Spring Boot", "Ankita", "text/plain", LocalDate.now(), "Spring Boot simplifies Java development.");
        List<Document> documentList = List.of(doc1, doc2);
        Page<Document> page = new PageImpl<>(documentList);

        when(documentRepository.filterByMetadata(eq("Ankita"), any(Pageable.class))).thenReturn(page);


        Page<QAResponseDTO> result = documentService.filterDocuments("Ankita", 0, 10, "uploadDate");


        assertEquals(2, result.getContent().size());
        assertEquals("Java Basics", result.getContent().get(0).getTitle());
        assertEquals("Spring Boot", result.getContent().get(1).getTitle());

        verify(documentRepository, times(1)).filterByMetadata(eq("Ankita"), any(Pageable.class));
    }
}
