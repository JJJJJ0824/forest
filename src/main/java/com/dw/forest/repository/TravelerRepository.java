package com.dw.forest.repository;

import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelerRepository extends JpaRepository<Traveler, String> {
}
