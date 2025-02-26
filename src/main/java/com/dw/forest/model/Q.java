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
@Table(name="q")
public class Q {
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

    @OneToOne(mappedBy = "q")
    private A a;

    public QaReadDTO toRead() {
        return new QaReadDTO(this.id, this.traveler.getTravelerName(), this.title, this.content, this.createdAt);
    }

    public QaDTO toDTO() {

        if (a==null) {
            return new QaDTO(this.id, this.traveler.getTravelerName(), this.title, this.content, this.createdAt, null);
        }

        return new QaDTO(a.getId(), a.getTraveler().getTravelerName(), a.getTitle(), a.getContent(), a.getCreatedAt(),
                new QaReadDTO(this.id, this.traveler.getTravelerName(), this.title, this.content, this.createdAt));
    }
}
