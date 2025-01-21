package com.dw.forest.repository;


import com.dw.forest.model.Completion;
import com.dw.forest.model.Course;
import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
    Optional<Completion> findByTravelerAndCourse(Traveler traveler , Course course);
    List<Completion> findByTravelerTravelerName(String travelerName);
    List<Completion> findByCourseCourseId(Long courseId);
}
