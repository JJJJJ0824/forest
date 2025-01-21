package com.dw.forest.repository;


import com.dw.forest.model.Completion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletionRepository extends JpaRepository<Completion, Long> {
    List<Completion> findByTraveler_TravelerName(String travelerName);
}
