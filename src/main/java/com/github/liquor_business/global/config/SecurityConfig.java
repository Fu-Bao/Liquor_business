package com.github.liquor_business.global.config;

import com.github.liquor_business.domain.user.service.LogoutService;
import com.github.liquor_business.global.jwt.filter.JwtAuthenticationFilter;
import com.github.liquor_business.global.jwt.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final LogoutService logoutService;
    private final DefaultOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/signup", "/api/login", "/api/refreshToken", "/oauth2/**", "/api/v1").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .logout((logout) -> logout
                        .logoutUrl("/api/logout").permitAll()
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                        .invalidateHttpSession(true));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
