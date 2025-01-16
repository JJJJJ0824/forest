package com.dw.forest.model;

import com.dw.forest.dto.CartDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "cart_fk")
    private List<Point> points;

    @Column(name = "purchase_status")  // 구매 상태를 나타내는 필드
    private boolean purchaseStatus; //  "true" - 구매완료, true 상태가 되면 삭제하도록 설정 필요, "false" - 구매 진행 안됨

    public CartDTO toDTO() {
        return new CartDTO(this.course.getCourseId(), this.traveler.getTravelerName(), this.purchaseStatus);
    }

    public void addPoint(String actionType, double pointsValue) {
        if (this.points == null) {
            this.points = new ArrayList<>();
        }
        Point point = new Point();
        point.setActionType(actionType);
        point.setPoints(pointsValue);
        point.setEventDate(LocalDate.now());
        point.setCart_fk(this); // Point와 Cart의 연관 관계 설정
        this.points.add(point);
    }
}
