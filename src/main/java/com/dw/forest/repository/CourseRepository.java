package com.dw.forest.repository;

import com.dw.forest.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategoryCategoryName(String categoryName);
    List<Course> findByPriceBetween(long minPrice, long maxPrice);
}
