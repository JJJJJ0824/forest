package com.dw.forest.model;

import com.dw.forest.dto.CourseDTO;
import com.dw.forest.dto.CourseReadDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "price", nullable = false) // 해당 코스 가격
    private long price;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToMany(mappedBy = "courses")
    private List<Traveler> traveler;

    @OneToMany(mappedBy = "course")
    private List<Completion> completions;

    @OneToMany(mappedBy = "course")
    private List<Cart> cartItems;

    @ManyToOne
    @JoinColumn(name = "category_name", nullable = false)
    private Category category;

    public CourseReadDTO toRead() {
        return new CourseReadDTO(
                this.courseId,
                this.title,
                this.description,
                this.content,
                this.price,
                this.createdAt,
                this.updatedAt,
                this.category != null ? this.category.getCategoryName() : "미분류"
        );
    }
}
