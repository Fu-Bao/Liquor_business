package com.github.liquor_business.global.jwt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static java.lang.System.getenv;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {
    private Map<String, String> env = getenv();
    private Date now = new Date();
    private final UserDetailsService userDetailsService;

    public String createAccessToken (String email) {

        return Constants.BEARER + Jwts.builder()
                .setIssuer(env.get("ISSUER"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Constants.ACCESS_TOKEN_VAILID_MILLISECOND))
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, env.get("SECRET_KEY"))
                .compact();

    }

    public String createRefreshToken (String email) {
        return Jwts.builder()
                .setIssuer(env.get("ISSUER"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Constants.REFRESH_TOKEN_VAILID_MILLISECOND))
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, env.get("SECRET_KEY"))
                .compact();
    }

    public String resolveToken (HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.HEADER_ACCESS_TOKEN_KEY);
        if(bearerToken == null || !bearerToken.startsWith(Constants.BEARER)) {
            return null;
        }
        return bearerToken.substring(7);
    }

    public boolean validation (String token) {
        return Jwts.parser().setSigningKey(env.get("SECRET_KEY")).parseClaimsJws(token) != null;
    }

    public boolean isExpired (String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(env.get("SECRET_KEY"))
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(now);
        } catch (Exception e) {
            return false;
        }

    }

    public Authentication getAuthentication (String token) {
        String email = getUserEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail (String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(env.get("SECRET_KEY"))
                    .parseClaimsJws(token)
                    .getBody()
                    .get("email", String.class);
        } catch (Exception e) {
            return "이메일을 찾을 수 업슷비다";
        }
    }
}
