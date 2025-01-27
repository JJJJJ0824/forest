package com.dw.forest.repository;

import com.dw.forest.model.QA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QARepository extends JpaRepository<QA, Long> {
    @Query("SELECT q FROM QA q WHERE q.title LIKE :title")
    List<QA> findByTitleLike(String title);
    List<QA> findByContentLike(String content);
    @Query("SELECT q FROM QA q WHERE q.title LIKE :title OR q.content LIKE :content")
    List<QA> findByTitleOrContentLike(String title, String content);
    List<QA> findByTraveler_TravelerName(String travelerName);
    boolean existsByTravelerTravelerName(String travelerName);
}
