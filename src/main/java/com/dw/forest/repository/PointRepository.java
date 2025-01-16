package com.dw.forest.repository;

import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByTraveler_TravelerName(String travelerName);
    List<Point> findByTraveler(Traveler traveler);
}
