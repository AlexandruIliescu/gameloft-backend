package com.gameloft.demo.services;

import com.gameloft.demo.models.dtos.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService {

    Page<NewsDTO> findAll(Pageable pageable);
    NewsDTO findOne(Long id);
    Page<NewsDTO> search(String query, Pageable pageable);
    void scrapeAndStoreNewsItems();
}