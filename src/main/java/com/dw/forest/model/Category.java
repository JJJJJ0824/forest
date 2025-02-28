package com.dw.forest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Checklist> checklists;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;
}
