package com.dw.forest.model;

import com.dw.forest.dto.PointDTO;
import com.dw.forest.dto.PointEventDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;
    @Column(name = "action_type") // 포인트 사용 목적
    private String actionType;
    @Column(name = "points") // 충전, 사용 다 나타낼 수 있음 (양수면 충전, 음수면 사용)
    private double points;
    @Column(name = "event_date") // 포인트 충전, 사용 일자
    private LocalDate eventDate;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart_fk;
    public String getTravelerName() {
        return traveler != null ? traveler.getTravelerName() : null;
    }

    public PointDTO toDTO() {
        return new PointDTO(
                this.getTravelerName(),
                this.points,
                this.actionType,
                this.cart_fk != null ? this.cart_fk.getCourse().getCourseId() : null, // 강의 ID
                this.cart_fk != null ? this.cart_fk.isPurchaseStatus() : false       // 결제 상태
        );
    }

    public PointEventDTO toEvent() {
        return new PointEventDTO(this.eventDate, this.points, this.actionType);
    }
}