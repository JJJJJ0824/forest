package com.dw.forest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CourseWithStudentsDTO {
    private CourseReadDTO course; // 강의 정보
    private List<TravelerDTO> travelers; // 수강생 정보

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class TravelerDTO {
        private String travelerName;
        private String contact;
        private String email;
        private String realName;
        private LocalDate completionDate; // 완료일
    }
}
