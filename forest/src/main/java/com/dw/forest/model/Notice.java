package com.dw.forest.model;

import com.dw.forest.dto.NoticeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "created_at")
    private LocalDate createdAt;

    public NoticeDTO toDTO() {
        return new NoticeDTO(this.id, this.title, this.content, this.createdAt, this.traveler.getTravelerName());
    }
}
