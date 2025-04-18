package com.example.demospringrestapi.Dto;

public class QAResponseDTO {
    private Long documentId;
    private String title;
    private String author;
    private String snippet;


    public QAResponseDTO(Long documentId, String title, String author, String snippet) {
        this.documentId = documentId;
        this.title = title;
        this.author = author;
        this.snippet = snippet;
    }


    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSnippet() { return snippet; }
    public void setSnippet(String snippet) { this.snippet = snippet; }
}
