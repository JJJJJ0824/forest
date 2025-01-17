package com.dw.forest.repository;

import com.dw.forest.model.QA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QARepository extends JpaRepository<QA, Long> {
    @Query("SELECT q FROM QA q WHERE LOWER(q.title) LIKE LOWER(:title)")
    List<QA> findByTitleLike(String title);
    List<QA> findByContentLike(String content);
    @Query("SELECT q FROM QA q WHERE q.title LIKE :title OR q.content LIKE :content")
    List<QA> findByTitleAndContentLike(String title, String content);
}
