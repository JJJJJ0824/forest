package com.dw.forest.model;

import com.dw.forest.dto.QaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="q_a")
public class QA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "type")
    private String type;

    public QaDTO toDTO() {
        return new QaDTO(this.Id, this.traveler.getTravelerName(), this.content, this.traveler.getTravelerName());
    }
}
