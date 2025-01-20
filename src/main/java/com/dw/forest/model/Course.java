package com.dw.forest.model;

import com.dw.forest.dto.CourseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "course")
    private List<CourseCompletion> courseCompletions;

    @OneToMany(mappedBy = "course")
    private List<Cart> cartItems;

    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;

    public CourseDTO toDTO() {
        return new CourseDTO(this.title, this.description, this.content, this.price, LocalDate.now(), this.category.getCategoryName());
    }
}
