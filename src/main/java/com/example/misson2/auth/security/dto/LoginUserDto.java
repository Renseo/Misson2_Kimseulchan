package com.example.misson2.auth.security.dto;

import lombok.Data;

@Data
public class LoginUserDto {
    private String username;
    private String password;
    private String passwordCheck;
}
