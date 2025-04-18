package com.example.demospringrestapi.service;

import com.example.demospringrestapi.Dto.QAResponseDTO;
import com.example.demospringrestapi.entities.Document;
import com.example.demospringrestapi.repository.DocumentRepository;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired

    private DocumentRepository documentRepository;
    @Override
    public void saveDocument(MultipartFile file,String author) {
        try {

            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();

            Tika tika = new Tika();
            String content = tika.parseToString(file.getInputStream());

            Document doc = new Document();
            doc.setTitle(fileName);
            doc.setType(fileType);
            doc.setAuthor(author);
            doc.setUploadDate(LocalDate.now());
            doc.setContent(content);

            documentRepository.save(doc);
        } catch (IOException | TikaException e) {
            throw new RuntimeException("Failed to parse and save document", e);
        }
    }

    @Override
    public List<QAResponseDTO> findMatchingSnippets(String keyword) {
        List<Document> documents = documentRepository.findByContentContainingIgnoreCase(keyword);
        List<QAResponseDTO> responses = new ArrayList<>();
        for (Document doc : documents) {
            String content = doc.getContent();
               content=content.replaceAll("\\s+"," ").trim();
                if (content.length() > 200) {
                    content = content.substring(0, 200) + "...";
                }
                QAResponseDTO dto = new QAResponseDTO(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getAuthor(),
                        content
                );
                responses.add(dto);
        }

        return responses;
    }

    public Page<QAResponseDTO> filterDocuments(String author, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Document> docsPage = documentRepository.filterByMetadata(author, pageable);

        List<QAResponseDTO> responses = docsPage.stream().map(doc -> {
            String content = doc.getContent();
            content = content.replaceAll("\\s+", " ").trim();
            if (content.length() > 200) {
                content = content.substring(0, 200) + "...";
            }
            return new QAResponseDTO(
                    doc.getId(),
                    doc.getTitle(),
                    doc.getAuthor(),
                    content
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, docsPage.getTotalElements());
    }

}
