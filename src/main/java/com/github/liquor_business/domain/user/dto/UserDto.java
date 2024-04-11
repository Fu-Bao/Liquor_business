package com.github.liquor_business.domain.user.dto;

import com.github.liquor_business.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String gender;
    private String socialName;
    private String socialUserId;
    private LocalDateTime createAt;
    private LocalDateTime deletedAt;

    public UserDto(User user) {
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
