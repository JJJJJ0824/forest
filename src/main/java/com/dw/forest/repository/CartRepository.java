package com.dw.forest.repository;

import com.dw.forest.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByTraveler_TravelerName(String travelerName);
    Optional<Cart> findByTraveler_TravelerNameAndCourse_CourseId(String travelerName, Long courseId);

    // 장바구니 결제 상태 업데이트 (구매 완료)
    @Query("UPDATE Cart c SET c.purchaseStatus = true WHERE c.traveler.travelerName = :travelerName AND c.purchaseStatus = false")
    void updatePurchaseStatus(String travelerName);

    @Query("SELECT c FROM Cart c WHERE c.traveler.travelerName = :travelerName AND c.purchaseStatus = false")
    List<Cart> findToPurchase(String travelerName);

    // 장바구니 결제 상태 업데이트 (모든 항목을 구매 완료로 처리)
    @Query("UPDATE Cart c SET c.purchaseStatus = true WHERE c.traveler.travelerName = :travelerName")
    void updateAllPurchaseStatus(String travelerName);
}
