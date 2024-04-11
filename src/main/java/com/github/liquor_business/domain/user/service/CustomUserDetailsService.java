package com.github.liquor_business.domain.user.service;


import com.github.liquor_business.domain.user.dto.CustomUserDetails;
import com.github.liquor_business.domain.user.entity.User;
import com.github.liquor_business.domain.user.entity.UserRole;
import com.github.liquor_business.domain.user.entity.Role;
import com.github.liquor_business.domain.user.repository.UserRepository;
import com.github.liquor_business.exception.AppException;
import com.github.liquor_business.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .user_id(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(UserRole::getRole).map(Role::getRoleName).toList())
                .build();

        return customUserDetails;
    }
}
