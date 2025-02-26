package com.dw.forest.repository;

import com.dw.forest.model.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QRepository extends JpaRepository<Q, Long> {
    @Query("SELECT q FROM Q q WHERE q.title LIKE :title")
    List<Q> findByTitleLike(String title);
    @Query("SELECT q FROM Q q WHERE q.content LIKE :content")
    List<Q> findByContentLike(String content);
    @Query("SELECT q FROM Q q WHERE q.title LIKE :title OR q.content LIKE :content")
    List<Q> findByTitleOrContentLike(String title, String content);
    List<Q> findByTraveler_TravelerName(String travelerName);
    boolean existsByTravelerTravelerName(String travelerName);
}
