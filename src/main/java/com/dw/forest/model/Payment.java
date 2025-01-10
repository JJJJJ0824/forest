package com.dw.forest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="payment")
public class Payment {
    @Id
    @Column(name = "payment_id")
    private String paymentID;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "point_used")
    private Long pointUsed;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

}
