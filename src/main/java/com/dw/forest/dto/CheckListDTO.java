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
    private String response;
    private boolean isChecked;
    private String travelerName;
    private String category;
}
