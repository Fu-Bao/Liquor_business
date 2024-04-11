package com.github.liquor_business.global.component;

import com.github.liquor_business.domain.user.entitiy.UserEntity;
import com.github.liquor_business.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExtractIdUtil {
    private final UserRepository userRepository;

    public Long extractUserIdFromAuthentication(Authentication authentication) {
        String email =  authentication.getName();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        long userId = user.get().getUserId();
        return userId;
    }
}
