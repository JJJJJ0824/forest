package com.dw.forest.dto;

import com.dw.forest.model.Notice;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDate createdAt;
    private String travelerName;
}


