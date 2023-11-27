package com.gameloft.demo.controllers;


import com.gameloft.demo.models.dtos.NewsDTO;
import com.gameloft.demo.services.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(Pageable pageable) {
        return ResponseEntity.ok(newsService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsItem(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findOne(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NewsDTO>> searchNewsItems(
            @RequestParam String query, Pageable pageable) {
        return ResponseEntity.ok(newsService.search(query, pageable));
    }
}