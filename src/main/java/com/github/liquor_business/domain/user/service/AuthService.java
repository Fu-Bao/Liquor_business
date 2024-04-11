package com.github.liquor_business.domain.user.service;

import com.github.liquor_business.domain.user.dto.*;
import com.github.liquor_business.domain.user.entity.RefreshToken;
import com.github.liquor_business.domain.user.entity.Role;
import com.github.liquor_business.domain.user.entity.User;
import com.github.liquor_business.domain.user.entity.UserRole;
import com.github.liquor_business.domain.user.repository.RefreshTokenRepository;
import com.github.liquor_business.domain.user.repository.RoleRepository;
import com.github.liquor_business.domain.user.repository.UserRepository;
import com.github.liquor_business.domain.user.repository.UserRoleRepository;
import com.github.liquor_business.exception.AppException;
import com.github.liquor_business.exception.ErrorCode;
import com.github.liquor_business.global.jwt.util.Constants;
import com.github.liquor_business.global.jwt.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public UserDto signup(SignupDto signupDTO) {
        User newUser = User.builder()
                .email(signupDTO.getEmail())
                .password(passwordEncoder.encode(signupDTO.getPassword()))
                .build();

        Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND.getMessage(), ErrorCode.ROLE_NOT_FOUND)
        );

        User saveUser = userRepository.save(newUser);
        UserRole userRole = UserRole.builder()
                .user(saveUser)
                .role(role)
                .build();

        userRoleRepository.save(userRole);

        return new UserDto(saveUser);
    }

    public LoginResDto login(LoginReqDto loginReqDto, HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User findUser = userRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

//        if (findUser.getDeletedAt() != null) {
//            throw new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND);
//        }

        if(passwordEncoder.matches(findUser.getPassword(), loginReqDto.getPassword())) {
            throw new AppException(ErrorCode.USER_PASSWORD_UNMATCHED.getMessage(), ErrorCode.USER_PASSWORD_UNMATCHED);
        }

        LoginResDto loginResponseDto = new LoginResDto(findUser);

        String accessToken = jwtTokenUtil.createAccessToken(findUser.getEmail());
        response.addHeader(Constants.HEADER_ACCESS_TOKEN_KEY, accessToken);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(findUser.getId());

        loginResponseDto.setAccessToken(accessToken);


        if(refreshToken.isEmpty()) {
            String newRefreshToken = jwtTokenUtil.createRefreshToken(findUser.getEmail());
            RefreshToken newRefreshTokenEntity = RefreshToken.builder().user(findUser).refreshToken(newRefreshToken).build();
            refreshTokenRepository.save(newRefreshTokenEntity);
            response.addHeader(Constants.HEADER_REFRESH_TOKEN_KEY, newRefreshToken);
            loginResponseDto.setRefreshToken(newRefreshToken);
        } else {
            response.addHeader(Constants.HEADER_REFRESH_TOKEN_KEY, refreshToken.get().getRefreshToken());
            loginResponseDto.setRefreshToken(refreshToken.get().getRefreshToken());
        }

        return loginResponseDto;
    }


    public String refresh(RefreshTokenDto refreshTokenDto, HttpServletResponse response) {
        if( refreshTokenDto.getRefreshToken() == null) {
            throw new AppException(ErrorCode.TOKEN_NOT_FOUND.getMessage(), ErrorCode.TOKEN_NOT_FOUND);
        }


        User user = userRepository.findByEmail(refreshTokenDto.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElseThrow(
                () -> new AppException(ErrorCode.TOKEN_NOT_FOUND.getMessage(), ErrorCode.TOKEN_NOT_FOUND)
        );

        if (jwtTokenUtil.isExpired(refreshToken.getRefreshToken())) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppException(ErrorCode.EXPIRED_TOKEN.getMessage(), ErrorCode.EXPIRED_TOKEN);
        }

        if (!refreshToken.getRefreshToken().equals(refreshTokenDto.getRefreshToken())) {
            throw new AppException(ErrorCode.TOKEN_NOT_ISSUED.getMessage(), ErrorCode.TOKEN_NOT_ISSUED);
        }

        response.addHeader(Constants.HEADER_ACCESS_TOKEN_KEY, jwtTokenUtil.createAccessToken(refreshTokenDto.getEmail()));
        return "accessToken 발급 완료";

    }

    public UserDto secession(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

        Date now = new Date();
        LocalDateTime localDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        user.setDeletedAt(localDateTime);

        User updateUser = userRepository.save(user);
        return new UserDto(updateUser);
    }
}
