package com.gameloft.demo.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FeedScrapingServiceImpl implements FeedScrapingService {

    private final NewsService newsService;

    public FeedScrapingServiceImpl(NewsService newsService) {
        this.newsService = newsService;
    }

    // Runs every 10 minutes
    @Scheduled(fixedRate = 60000)
    public void scrapeFeeds() {
        newsService.scrapeAndStoreNewsItems();
    }
}
