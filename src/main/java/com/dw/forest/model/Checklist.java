package com.dw.forest.model;

import com.dw.forest.dto.CheckListDTO;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name="checklist")
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "traveler_name")
    private Traveler traveler;

    @Column(name = "direction")
    private String direction;

    @Column(name = "response")
    private String response;

    @Column(name = "is_checked")
    private boolean isChecked;

    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;

    public CheckListDTO toDTO() {
        return new CheckListDTO(this.id, this.direction, this.response, this.isChecked,
                this.traveler.getTravelerName() , this.category.getCategoryName());
    }

    public boolean isCompleted() {
        return this.isChecked;
    }
}
