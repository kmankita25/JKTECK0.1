package com.example.demospringrestapi.service;

import com.example.demospringrestapi.Dto.QAResponseDTO;
import com.example.demospringrestapi.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DocumentService {
   public void saveDocument(MultipartFile file,String author);
    public List<QAResponseDTO> findMatchingSnippets(String question);

    Page<QAResponseDTO> filterDocuments(String author, int page, int size, String sortBy);

}
