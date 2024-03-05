package com.example.misson2.auth.security.dto;

import com.example.misson2.auth.security.entity.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Getter
    private Long id;
    private String username;
    private String password;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String nickname;
    @Getter
    @Setter
    private Integer age;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private String uploadFileName;
    @Getter
    @Setter
    private String storeFileName;
    @Setter
    private String authorities;

    public CustomUserDetails updateUserInfo(UpdateUserInfoDto dto) {
        this.name = dto.getName();
        this.nickname = dto.getNickname();
        this.age = dto.getAge();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.authorities = "ROLE_REGULAR_USER";
        return this;
    }

    public String getRawAuthorities() {
        return this.authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .sorted()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    // 먼 미래
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
