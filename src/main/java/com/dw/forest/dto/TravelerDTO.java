package com.dw.forest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TravelerDTO {
    private String travelerName;
    private String realName;
    private String password;
    private String email;
    private String contact;
    private String role;
}
