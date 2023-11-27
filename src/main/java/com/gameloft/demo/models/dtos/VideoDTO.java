package com.gameloft.demo.models.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoDTO {

    private Long id;
    private String title;
    private String link;
    private LocalDateTime publicationDate;
    private String description;
    private String thumbnail;
    private String creator;
}