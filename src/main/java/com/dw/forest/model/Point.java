package com.dw.forest.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "point_id")
    private Long pointID;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "point_changed")
    private Long pointChanged;

    @Column(name = "balance_after_change")
    private Long balanceAfterChange;

}
