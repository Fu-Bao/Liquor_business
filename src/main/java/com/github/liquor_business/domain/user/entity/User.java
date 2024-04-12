package com.github.liquor_business.domain.user.entity;

import com.github.liquor_business.domain.user.dto.SignupDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity(name = "users")
@Getter
@Builder
@RequiredArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private String email;

    private String password;

    @Column(name = "username")
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "social_type")
    private String socialType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    private LocalDateTime createdAt;

    public static User toEntity(SignupDto signupDto) {
        return User.builder()
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .name(signupDto.getUsername())
                .build();
    }

    // password 암호화
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // 초기 회원가입 유저 권한은 USER
    public void authorizeUser() {
        this.role = Role.USER;
    }

}
