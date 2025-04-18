package com.example.demospringrestapi.controller;

import com.example.demospringrestapi.Dto.QAResponseDTO;
import com.example.demospringrestapi.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    void testSavedocuments() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Ankita".getBytes());

        mockMvc.perform(multipart("/api/documents/savedocuments")
                        .file(file)
                        .param("author", "Ankita"))
                .andExpect(status().isOk())
                .andExpect(content().string("Documents uploaded successfully"));
    }

    @Test
    void testGetAnswer() throws Exception {
        QAResponseDTO mockResponse = new QAResponseDTO(1L, "Test Document", "Ankita", "Sample Snippet");
        Mockito.when(documentService.findMatchingSnippets(anyString())).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/documents/qa").param("question", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers[0].title").value("Test Document"))
                .andExpect(jsonPath("$.count").value(1));
    }
}
