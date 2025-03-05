package com.dw.forest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CartDTO {
    private Long cartId;
    private Long courseId;
    private String travelerName;
    private boolean purchaseStatus;
    private String title;
    private String description;
    private String content;
    private long price;
    private String categoryName;

}
