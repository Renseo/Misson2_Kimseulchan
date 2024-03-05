package com.example.misson2.auth.security.repository;

import com.example.misson2.auth.security.entity.RequestStatus;
import com.example.misson2.auth.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
