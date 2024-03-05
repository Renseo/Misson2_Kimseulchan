package com.example.misson2.auth.jwt;

import com.example.misson2.auth.jwt.dto.JwtRequestDto;
import com.example.misson2.auth.jwt.dto.JwtResponseDto;
import com.example.misson2.auth.security.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("token")
public class TokenController {

    // 실제로 JWT를 발급하기 위해 필요한 Bean
    private final JwtTokenUtils jwtTokenUtils;
    // 사용자 정보를 확인하기 위한 Bean
    private final UserDetailsManager manager;
    // 사용자가 JWT 발급을 위해 제출하는 비밀번호가 일치하는지 확인하기 위한 암호화 Bean
    private final PasswordEncoder passwordEncoder;

    public TokenController(
            JwtTokenUtils jwtTokenUtils,
            UserDetailsManager manager,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /token/issue
    @PostMapping("issue")
    public JwtResponseDto issueJwt(
            @RequestBody
            JwtRequestDto dto
    ) {
        // 사용자가 제공한 username, password가 저장된 사용자인지
        if (!manager.userExists(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        CustomUserDetails userDetails = (CustomUserDetails) manager.loadUserByUsername(dto.getUsername());

        // 비밀번호 대조
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // JWT 발급\
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }
}
