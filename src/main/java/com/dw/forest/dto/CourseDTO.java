package com.dw.forest.dto;

import com.dw.forest.model.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CourseDTO {

    private String title;
    private String description;
    private String content;
    private String type;
    private long price;
    private LocalDate createdAt;

    public CourseDTO(Course course) {
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.content = course.getContent();
        this.type = course.getType();
        this.price = course.getPrice();
        this.createdAt = course.getCreatedAt();
    }
}
