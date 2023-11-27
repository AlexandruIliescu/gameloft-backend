package com.gameloft.demo.services;

import com.gameloft.demo.exceptions.ResourceNotFoundException;
import com.gameloft.demo.models.dtos.NewsDTO;
import com.gameloft.demo.models.entities.News;
import com.gameloft.demo.repositories.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final RestTemplate restTemplate;

    public NewsServiceImpl(NewsRepository newsRepository, RestTemplate restTemplate) {
        this.newsRepository = newsRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Page<NewsDTO> findAll(Pageable pageable) {
        Page<News> newsPage = newsRepository.findAll(pageable);
        return newsPage.map(this::convertToDTO);
    }

    @Override
    public NewsDTO findOne(Long id) {
        return newsRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));
    }

    @Override
    public  Page<NewsDTO> search(String query, Pageable pageable) {
        Page<News> newsPage = newsRepository.searchByTitleOrDescription(query, pageable);
        return newsPage.map(this::convertToDTO);
    }

    @Transactional
    @Override
    public void scrapeAndStoreNewsItems() {
        try {
            // Step 1: Fetch and parse the RSS feed into NewsDTOs
            List<NewsDTO> feedItems = getNewsFromUrl();

            // Step 2: Process each NewsDTO
            feedItems.forEach(newsDTO -> {
                // Step 2a: Check if the news item already exists in the database
                News existingNews = newsRepository.findByLink(newsDTO.getLink())
                        .orElse(null);

                // Step 2b: If the news item does not exist, convert DTO to entity and save
                if (existingNews == null) {
                    News newsItem = convertToEntity(newsDTO);
                    newsRepository.save(newsItem);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private News convertToEntity(NewsDTO newsDTO) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setLink(newsDTO.getLink());
        news.setCreator(newsDTO.getCreator());
        news.setDescription(newsDTO.getDescription());
        news.setThumbnail(newsDTO.getThumbnail());
        news.setPublicationDate(newsDTO.getPublicationDate());
        return news;
    }

    public List<NewsDTO> parseNewsFeed(String xmlData) throws Exception {
        List<NewsDTO> newsItems = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse the XML string and start processing it
        Document document = builder.parse(new InputSource(new StringReader(xmlData)));
        NodeList itemList = document.getElementsByTagName("item");

        for (int index = 0; index < itemList.getLength(); index++) {
            Element item = (Element) itemList.item(index);

            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setTitle(getValueFromTag(item, "title"));
            newsDTO.setLink(getValueFromTag(item, "link"));
            newsDTO.setCreator(getValueFromTag(item, "dc:creator"));
            newsDTO.setDescription(getValueFromTag(item, "description"));
            newsDTO.setThumbnail(getAttributeValueFromTag(item, "media:content", "url"));

            // Parsing the date
            String pubDate = getValueFromTag(item, "pubDate");
            DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            LocalDateTime publicationDate = LocalDateTime.parse(pubDate, formatter);
            newsDTO.setPublicationDate(publicationDate);

            newsItems.add(newsDTO);
        }

        return newsItems;
    }

    private String getValueFromTag(Element element, String tagName) {
        NodeList tags = element.getElementsByTagName(tagName);
        if (tags.getLength() > 0) {
            return tags.item(0).getTextContent();
        } else {
            return null;
        }
    }

    private String getAttributeValueFromTag(Element element, String tagName, String attributeName) {
        NodeList tags = element.getElementsByTagName(tagName);
        if (tags.getLength() > 0) {
            Element tag = (Element) tags.item(0);
            return tag.getAttribute(attributeName);
        } else {
            return null;
        }
    }

    private List<NewsDTO> getNewsFromUrl() {
        try {
            String url = "https://www.eurogamer.net/feed/news";
            String xmlData = restTemplate.getForObject(url, String.class);
            return parseNewsFeed(xmlData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    private NewsDTO convertToDTO(News newsItem) {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(newsItem.getId());
        newsDTO.setTitle(newsItem.getTitle());
        newsDTO.setLink(newsItem.getLink());
        newsDTO.setCreator(newsItem.getCreator());
        newsDTO.setDescription(newsItem.getDescription());
        newsDTO.setThumbnail(newsItem.getThumbnail());
        newsDTO.setPublicationDate(newsItem.getPublicationDate());
        return newsDTO;
    }
}