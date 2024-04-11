package com.github.liquor_business.domain.user.service;


import com.github.liquor_business.domain.user.dto.UserDto;
import com.github.liquor_business.domain.user.entity.User;
import com.github.liquor_business.domain.user.repository.UserRepository;
import com.github.liquor_business.exception.AppException;
import com.github.liquor_business.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

import static java.lang.System.in;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

        log.info("gender = {}", userDto);

        User updateUser = user.updateUser(userDto);
        User findNewUser = userRepository.save(updateUser);

        return new UserDto(findNewUser);
    }

    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

        return new UserDto(user);
    }
}
