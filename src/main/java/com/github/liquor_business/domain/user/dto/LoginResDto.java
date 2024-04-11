package com.github.liquor_business.domain.user.dto;

import com.github.liquor_business.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginResDto {

    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String gender;
    private String socialName;
    private String socialUserId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime createAt;
    private LocalDateTime deletedAt;

    public LoginResDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.gender = user.getGender();
        this.socialName = user.getSocialName();
        this.socialUserId = user.getSocialUserId();
        this.createAt = user.getCreateAt();
        this.deletedAt = user.getDeletedAt();
    }
}
