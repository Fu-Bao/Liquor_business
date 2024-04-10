package com.github.liquor_business.domain.auth.service;

import com.github.liquor_business.domain.auth.entity.CustomUserDetails;
import com.github.liquor_business.domain.user.entitiy.UserEntity;
import com.github.liquor_business.domain.user.repository.UserRepository;
import com.github.liquor_business.exception.AppException;
import com.github.liquor_business.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
