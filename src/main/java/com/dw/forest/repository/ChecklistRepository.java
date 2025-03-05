package com.dw.forest.repository;

import com.dw.forest.model.Checklist;
import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByTraveler_TravelerName(String travelerName);
    List<Checklist> findByTraveler(Traveler traveler);
    List<Checklist> findByTraveler_TravelerNameAndIsCheckedFalse(String travelerName);
    List<Checklist> findByTravelerAndIsCheckedTrue(Traveler traveler);

    Optional<Checklist> findByTraveler_TravelerNameAndDirection(String travelerName, String direction);
}
