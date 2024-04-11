package com.github.liquor_business.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenDto {

    private String email;
    private String refreshToken;

}