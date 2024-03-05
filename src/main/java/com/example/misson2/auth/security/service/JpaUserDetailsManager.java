package com.example.misson2.auth.security.service;

import com.example.misson2.auth.security.dto.CustomUserDetails;
import com.example.misson2.auth.security.entity.UserEntity;
import com.example.misson2.auth.security.repository.UserRepository;
import com.example.misson2.file.FileStore;
import com.example.misson2.file.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final FileStore fileStore;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            FileStore fileStore
    ) {
        this.userRepository = userRepository;
        this.fileStore = fileStore;
    }

    // 우선 개발 해볼 것
    @Override
    // formLogin 등 Spring Security 내부에서
    // 인증을 처리할때 사용하는 메서드이다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        UserEntity userEntity = optionalUser.get();

        return CustomUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .uploadFileName(userEntity.getAvatar().getUploadFileName())
                .storeFileName(userEntity.getAvatar().getStoreFileName())
                .authorities(userEntity.getAuthorities())
                .build();
    }

    @Override
    @Transactional
    // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .authorities("ROLE_INACTIVE_USER")
                    .build();
            userRepository.save(newUser);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    @Transactional
    public void updateUser(UserDetails user) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(user.getUsername());
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) user;
        UserEntity findUser = optionalUser.get();

        findUser.setName(customUserDetails.getName());
        findUser.setNickname(customUserDetails.getNickname());
        findUser.setAge(customUserDetails.getAge());
        findUser.setEmail(customUserDetails.getEmail());
        findUser.setPhone(customUserDetails.getPhone());
        findUser.setAuthorities(customUserDetails.getRawAuthorities());
    }

    @Transactional
    public void updateUserAvatar(String username, MultipartFile file) throws IOException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity findUser = optionalUser.get();
        UploadFile uploadFile = fileStore.storeFile(file);
        findUser.setAvatar(uploadFile);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
