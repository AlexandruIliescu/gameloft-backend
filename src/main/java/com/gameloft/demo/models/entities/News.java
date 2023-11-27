package com.gameloft.demo.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String link;

    @Column(name = "publication_date", nullable = false)
    private LocalDateTime publicationDate;

    @Lob
    private String description;

    @Column(length = 500)
    private String thumbnail;

    @Column(length = 500)
    private String creator;
}