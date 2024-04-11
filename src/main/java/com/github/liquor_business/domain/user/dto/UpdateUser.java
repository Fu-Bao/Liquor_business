package com.github.liquor_business.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUser {
    public String imageUrl;
    public String password;
    public String username;
}
