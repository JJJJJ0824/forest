package com.dw.forest.repository;

import com.dw.forest.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByTitleLike(String title);
    @Query("SELECT n FROM Notice n WHERE LOWER(n.title) LIKE LOWER(:title)")
    Notice findByTitleLike2(String title);
}