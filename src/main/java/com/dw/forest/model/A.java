package com.dw.forest.model;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.dto.QaReadDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="a")
public class A {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToOne
    @JoinColumn(name = "q_id")
    private Q q;

    public QaReadDTO toRead() {
        return new QaReadDTO(this.id, this.traveler.getTravelerName(), this.title, this.content, this.createdAt);
    }

    public QaDTO toDTO() {
        return new QaDTO(this.q.getId(), this.q.getTraveler().getTravelerName(), this.q.getTitle(), this.q.getContent(), this.q.getCreatedAt(),
                new QaReadDTO(this.id, this.traveler.getTravelerName(), this.title, this.content, this.createdAt));
    }
}
