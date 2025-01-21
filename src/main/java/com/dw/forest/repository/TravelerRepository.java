package com.dw.forest.repository;

import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelerRepository extends JpaRepository<Traveler, String> {

    Optional<Traveler> findByTravelerName(String travelerName);
}
