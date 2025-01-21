package com.dw.forest.repository;

import com.dw.forest.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByTraveler_TravelerName(String travelerName);
    Optional<Cart> findByTraveler_TravelerNameAndCourse_CourseId(String travelerName, Long courseId);
}
