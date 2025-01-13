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
    @ManyToOne
    @JoinColumn(name = "traveler_authority")
    private Authority authority;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "contact")
    private String contact;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "real_name", nullable = false)
    private String realName;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @OneToMany(mappedBy = "traveler")
    private List<CourseCompletion> courseCompletions;
    @OneToMany(mappedBy = "traveler")
    private List<QA> QAS;
    @OneToMany(mappedBy = "traveler")
    private List<Checklist> checklists;
    @OneToMany(mappedBy = "traveler")
    private List<Point> points;
    @OneToMany(mappedBy = "traveler")
    private List<Cart> cartItems;

    public TravelerDTO toDTO() {
        return new TravelerDTO(this.travelerName, authority.getAuthorityName(), this.contact, this.email, null, this.realName);
    }
}
