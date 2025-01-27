package com.dw.forest.dto;

import com.dw.forest.model.QA;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class QaReadDTO {
    private Long id;
    private String traveler_name;
    private String type;
    private String title;
    private String content;
    private LocalDate createdAt;
}
