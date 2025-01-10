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
@Table(name="course_completion")
public class CourseCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "completion_id")
    private Long completionID;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "completion_date")
    private LocalDate completionDate;
}
