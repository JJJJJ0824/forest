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
    private String role;
    private String contact;
    private String email;
    private String password;
    private String realName;

}
