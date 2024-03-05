package com.example.misson2.auth.config;

import com.example.misson2.auth.jwt.JwtTokenFilter;
import com.example.misson2.auth.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

// @Bean을 비롯해서 여러 설정을 하기 위한 Bean 객체
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    // 메서드의 결과를 Bean 객체로 관리해주는 어노테이션
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                // csrf 보안 해제
                .csrf(AbstractHttpConfigurer::disable)
                // url에 따른 요청 인가
                .authorizeHttpRequests(
                        auth -> auth
                                // 어떤 경로에 대한 설정인지
                                .requestMatchers(
                                        "/token/issue"
                                )
                                // 이 경로에 도달할 수 있는 사람에 대한 설정(모두)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/users")
                                // 미 인증 사용자에 대한 설정
                                .anonymous()
                                .requestMatchers(HttpMethod.PUT, "/users")
                                .authenticated()
                                // ROLE에 따른 접근 설정
                                .requestMatchers(
                                        "/trade/**",
                                        "/users/**"
                                )
                                // 인증된 사용자에 대한 설정
                                .hasAnyRole("REGULAR_USER", "BUSINESS_USER", "ADMIN")
                )
                // JWT를 사용하기 때문에 보안 관련 세션 해제
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                )
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
