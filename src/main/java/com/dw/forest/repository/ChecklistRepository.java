package com.dw.forest.repository;

import com.dw.forest.model.Checklist;
import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByTraveler_TravelerName(String travelerName);
    List<Checklist> findByTraveler(Traveler traveler);
    List<Checklist> findByTraveler_TravelerNameAndIsCheckedFalse(String travelerName);
}
