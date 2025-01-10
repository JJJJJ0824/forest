package com.dw.forest.model;

import com.dw.forest.dto.TravelerDTO;
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
@Table(name="traveler")
public class Traveler {
    @Id
    @Column(name = "traveler_name")
    private String travelerName;

    @Column(name = "real_name", nullable = false)
    private String realName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "total_point", nullable = false)
    private Long totalPoint;

    @OneToMany(mappedBy = "traveler")
    private List<CourseCompletion> courseCompletions;

    @OneToMany(mappedBy = "traveler")
    private List<ForumPost> forumPosts;

    @OneToMany(mappedBy = "traveler")
    private List<Checklist> checklists;

    @OneToMany(mappedBy = "traveler")
    private List<Payment> payments;

    @OneToMany(mappedBy = "traveler")
    private List<Point> points;

    @OneToMany(mappedBy = "traveler")
    private List<Cart> CartItems;

    @ManyToOne
    @JoinColumn(name = "traveler_authority")
    private Authority authority;

    public TravelerDTO toDTO() {
        return new TravelerDTO(this.travelerName, this.realName, this.password, this.email, this.contact, authority.getAuthority_name());
    }
}
