package com.example.misson2.auth.config;

import com.example.misson2.auth.security.entity.UserEntity;
import com.example.misson2.auth.security.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitConfig {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void InitAdmin() {
        UserEntity admin = UserEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
    }
}
