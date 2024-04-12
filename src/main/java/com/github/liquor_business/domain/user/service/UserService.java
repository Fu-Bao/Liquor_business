package com.github.liquor_business.domain.user.service;

import com.github.liquor_business.domain.user.dto.SignupDto;
import com.github.liquor_business.domain.user.entity.User;
import com.github.liquor_business.domain.user.repository.UserRepository;
import com.github.liquor_business.exception.AppException;
import com.github.liquor_business.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void SignupProcess(SignupDto signupDto) {

        Boolean emailExist = userRepository.existsByEmail(signupDto.getEmail());
        Boolean nameExist = userRepository.existsByName(signupDto.getUsername());
        if (emailExist) {
            throw new AppException(ErrorCode.EMAIL_DUPLICATED.getMessage(), ErrorCode.EMAIL_DUPLICATED);
        } else if (nameExist) {
            throw new AppException(ErrorCode.USERNAME_DUPLICATED.getMessage(), ErrorCode.USERNAME_DUPLICATED);
        }

        User user = User.toEntity(signupDto);
        user.authorizeUser();
        user.passwordEncode(passwordEncoder);

        userRepository.save(user);
    }
}
