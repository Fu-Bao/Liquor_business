package com.github.liquor_business.global.component;

import com.github.liquor_business.domain.user.entitiy.UserEntity;
import com.github.liquor_business.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExtractIdUtil {
    private UserRepository userRepository;

    public ExtractIdUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long extractUserIdFromAuthentication(Authentication authentication) {
        String email =  authentication.getName();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        long userId = user.get().getUserId();
        return userId;
    }
}
