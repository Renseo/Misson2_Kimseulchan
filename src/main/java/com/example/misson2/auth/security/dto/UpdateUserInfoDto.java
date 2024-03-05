package com.example.misson2.auth.security.dto;

import lombok.Data;

@Data
public class UpdateUserInfoDto {
    private String name;
    private String nickname;
    private Integer age;
    private String email;
    private String phone;
}
