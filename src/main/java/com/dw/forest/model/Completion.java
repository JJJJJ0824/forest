package com.dw.forest.model;

import com.dw.forest.dto.CourseReadDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="completion")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "completion_id")
    private Long completionId;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "completion_date")
    private LocalDate completionDate; // 완료일이지만, 사용일을 대조하여 추가 가능

    @ManyToOne
    @JoinColumn(name = "point_id") // 포인트 사용과 연결
    private Point pointUsed;

    public CourseReadDTO toRead() {
        Course course = this.course;
        return new CourseReadDTO(course.getCourseId(), course.getTitle(), course.getDescription(), course.getContent(),
                course.getPrice(), course.getCreatedAt(), course.getUpdatedAt(), course.getCategory().getCategoryName());
    }
}
