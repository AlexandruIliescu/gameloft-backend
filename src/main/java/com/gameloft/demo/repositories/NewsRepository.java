package com.gameloft.demo.repositories;

import com.gameloft.demo.models.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findByLink(String link);
    @Query(value = "SELECT * FROM news WHERE " +
            "LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))",
            nativeQuery = true)
    Page<News> searchByTitleOrDescription(String searchTerm, Pageable pageable);
}