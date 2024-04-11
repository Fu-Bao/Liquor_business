package com.github.liquor_business.domain.user.controller;

import com.github.liquor_business.domain.user.dto.*;
import com.github.liquor_business.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup (@Valid @RequestBody SignupDto signupDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        UserDto userDto = authService.signup(signupDTO);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginReqDto loginDTO, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        LoginResDto user = authService.login(loginDTO, response);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refresh (@RequestBody RefreshTokenDto refreshTokenDto, HttpServletResponse response) {
        String msg = authService.refresh(refreshTokenDto, response);
        return ResponseEntity.ok(msg);
    }

    @PutMapping("/deleted/{userId}")
    public ResponseEntity<UserDto> secession (@PathVariable Long userId) {
        UserDto userDto = authService.secession(userId);
        return ResponseEntity.ok(userDto);
    }

}
