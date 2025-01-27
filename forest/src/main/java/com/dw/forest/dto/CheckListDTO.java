package com.dw.forest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CheckListDTO {
    private Long id;
    private String direction;
    private boolean isChecked;
    private String traveler;
    private String category;
}
