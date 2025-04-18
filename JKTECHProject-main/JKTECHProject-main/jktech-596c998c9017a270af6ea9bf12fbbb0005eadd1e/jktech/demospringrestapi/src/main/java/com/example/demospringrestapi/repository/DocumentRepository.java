package com.example.demospringrestapi.repository;


import com.example.demospringrestapi.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByContentContainingIgnoreCase(String keyword);


    @Query("SELECT d FROM Document d WHERE " +
            "(:author IS NULL OR LOWER(d.author) LIKE LOWER(CONCAT('%', :author, '%'))) ")
    Page<Document> filterByMetadata(@Param("author") String author,
                                    Pageable pageable);



}


