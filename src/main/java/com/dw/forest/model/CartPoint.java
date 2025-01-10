package com.dw.forest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="cart_point")
public class CartPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @OneToMany(mappedBy = "cartPoint")
    private List<Cart> cartItems;

    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}
