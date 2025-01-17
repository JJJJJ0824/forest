package com.dw.forest.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PointConvertResponseDTO {
    private String couponCode;
    private double pointsUsed;
    private String message;
}

