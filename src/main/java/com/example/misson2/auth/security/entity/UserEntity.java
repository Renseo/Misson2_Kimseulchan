package com.example.misson2.auth.security.entity;

import com.example.misson2.file.UploadFile;
import com.example.misson2.trade.entity.Trade;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    @Setter
    private String name;
    @Setter
    private String nickname;
    @Setter
    private Integer age;
    @Setter
    private String email;
    @Setter
    private String phone;

    // ROLE_INACTIVE_USER, ROLE_REGULAR_USER, ROLE_BUSINESS_USER, ROLE_ADMIN
    @Setter
    private String authorities;

    @Embedded
    @Setter
    private UploadFile avatar;

    @OneToMany
    private List<Trade> trades;

}
