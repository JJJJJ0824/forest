package com.dw.forest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FindID_PWD {
    String travelerName;
    String realName;
    String contact;
    String email;
    String newPassword;
    String newPasswordCheck;
}
