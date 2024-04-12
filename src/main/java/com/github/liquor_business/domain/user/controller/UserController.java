package com.github.liquor_business.domain.user.controller;

import com.github.liquor_business.domain.user.dto.SignupDto;
import com.github.liquor_business.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> Signup(SignupDto signupDto) {
        log.info("회원가입 진입");
        userService.SignupProcess(signupDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
