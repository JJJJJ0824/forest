package com.dw.forest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="forum_post")
public class ForumPost {
    @Id
    @Column(name = "post_id")
    private String postID;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "type")
    private String type;

}
