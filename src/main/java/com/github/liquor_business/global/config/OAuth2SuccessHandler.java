package com.github.liquor_business.global.config;


import com.github.liquor_business.domain.oauth.entity.CustomOAuth2User;
import com.github.liquor_business.domain.user.entity.RefreshToken;
import com.github.liquor_business.domain.user.entity.User;
import com.github.liquor_business.domain.user.repository.RefreshTokenRepository;
import com.github.liquor_business.domain.user.repository.UserRepository;
import com.github.liquor_business.exception.AppException;
import com.github.liquor_business.exception.ErrorCode;
import com.github.liquor_business.global.jwt.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // CustomOAuth2UserService에서 넘겨준 CustomOAuth2User를 onAuthenticationSuccess가 받아낸다.
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, SecurityException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//        String userId = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String accessToken = jwtTokenUtil.createAccessToken(email);
        String refreshToken;

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND)
        );

        RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(user.getId()).orElse(
                null
        );


        if (findRefreshToken == null) {
            refreshToken = jwtTokenUtil.createRefreshToken(email);
            RefreshToken refreshTokenEntity = RefreshToken.builder().refreshToken(refreshToken).user(user).build();
            refreshTokenRepository.save(refreshTokenEntity);
        } else {
            refreshToken = findRefreshToken.getRefreshToken();
        }

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost:8080")
                .query("accessToken={value1}")
                .query("refreshToken={value2}")
                .buildAndExpand(accessToken, refreshToken);

        response.sendRedirect(String.valueOf(uriComponents));

    }
}
