package com.dw.forest.repository;

import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByTravelerTravelerName(String travelerName);
    List<Point> findByTraveler(Traveler traveler);
    @Query("SELECT SUM(p.points) FROM Point p WHERE p.traveler.travelerName = :travelerName AND p.points > 0")
    Double getUseAblePoints(String travelerName); // 총 포인트 반환
    @Transactional
    @Modifying
    @Query(value = "UPDATE point SET points = points - :amount WHERE traveler_name = :travelerName AND points > 0", nativeQuery = true)
    void usePoint(double amount, String travelerName);
}
