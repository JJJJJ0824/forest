package com.dw.forest.repository;

import com.dw.forest.model.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARepository extends JpaRepository<A, Long> {
    @Query("SELECT a FROM A a WHERE a.title LIKE :title")
    List<A> findByTitleLike(String title);
    @Query("SELECT a FROM A a WHERE a.content LIKE :content")
    List<A> findByContentLike(String content);
    @Query("SELECT a FROM A a WHERE a.title LIKE :title OR a.content LIKE :content")
    List<A> findByTitleOrContentLike(String title, String content);
}
