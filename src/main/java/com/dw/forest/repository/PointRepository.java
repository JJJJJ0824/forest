package com.dw.forest.repository;

import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByTravelerTravelerName(String travelerName);
    List<Point> findByTraveler(Traveler traveler);
    @Query("SELECT SUM(p.points) FROM Point p WHERE p.traveler.travelerName = :travelerName AND p.actionType = '충전'")
    Double getUseablePoints(String travelerName); // 총 포인트 반환

    @Query("UPDATE Point p SET p.points = p.points - :amount WHERE p.traveler.travelerName = :travelerName AND p.actionType = '충전'")
    void usePoints(String travelerName, double amount); // 포인트 차감
}
