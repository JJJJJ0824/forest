package com.dw.forest.dto;

import com.dw.forest.model.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PointDTO {
    private double points;
    private String actionType;
}
