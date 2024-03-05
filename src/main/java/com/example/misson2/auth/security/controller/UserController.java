package com.example.misson2.auth.security.controller;

import com.example.misson2.auth.security.dto.CustomUserDetails;
import com.example.misson2.auth.security.dto.LoginUserDto;
import com.example.misson2.auth.security.dto.UpdateUserInfoDto;
import com.example.misson2.auth.security.service.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager manager;

    // 회원가입
    @PostMapping
    public void signUpRequest(
            @RequestBody
            LoginUserDto dto
    ) {
        if (dto.getPassword().equals(dto.getPasswordCheck())) {
            // 새로운 사용자 생성
            CustomUserDetails user = CustomUserDetails.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .build();
            // 새로운 사용자 등록
            manager.createUser(user);
        }
    }

    // 회원 정보 추가
    @PutMapping
    public void update(
            Authentication authentication,
            @RequestBody
            UpdateUserInfoDto dto
    ) {
        CustomUserDetails findUser = (CustomUserDetails) manager.loadUserByUsername(authentication.getName());
        CustomUserDetails updateUser = findUser.updateUserInfo(dto);
        manager.updateUser(updateUser);
    }

    // 회원 아바타 추가
    @PutMapping("avatar")
    public void avatar(
            Authentication authentication,
            @RequestParam("file")
            MultipartFile file
    ) throws IOException {
        String findUsername = authentication.getName();
        ((JpaUserDetailsManager) manager).updateUserAvatar(findUsername, file);
    }
}
