package com.example.misson2.auth.jwt.dto;

import lombok.Data;

@Data
public class JwtRequestDto {

    private final String username;
    private final String password;
}
