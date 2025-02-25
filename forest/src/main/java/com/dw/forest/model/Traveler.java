package com.dw.forest.model;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.dto.TravelerResponseDTO;
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
    private String travelerName; // 사용자가 페이지에서 사용할 이름

    @ManyToOne
    @JoinColumn(name = "traveler_authority")
    private Authority authority; // 권한

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "real_name", nullable = false)
    private String realName; // 사용자의 실제 이름, 본명

    @Column(name = "created_at")
    private LocalDate createdAt; // 만들어진 날짜, 계정 생성일

    @OneToMany(mappedBy = "traveler")
    private List<Completion> completions;

    @OneToMany(mappedBy = "traveler")
    private List<Q> QS;

    @OneToMany(mappedBy = "traveler")
    private List<A> AS;

    @OneToMany(mappedBy = "traveler")
    private List<Checklist> checklists;

    @OneToMany(mappedBy = "traveler")
    private List<Point> points;

    @OneToMany(mappedBy = "traveler")
    private List<Cart> cartItems;

    @ManyToMany(mappedBy = "traveler")
    private List<Course> courses;

    public TravelerDTO toDTO() {
        return new TravelerDTO(this.travelerName, authority.getAuthorityName(), this.contact, this.email, null, this.realName);
    }

    public TravelerResponseDTO toResponse() {
        return new TravelerResponseDTO(this.travelerName, null, this.contact, this.email, this.realName);
    }
}
