package com.github.liquor_business.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원을 찾을 수 없습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다."),
    USER_ID_UNMATCHED(HttpStatus.CONFLICT, "유저 아이디가 일치하지 않습니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 가입한 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호 입니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "유저네임을 찾을 수 없습니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    USER_PASSWORD_UNMATCHED(HttpStatus.CONFLICT, "유저 비밀번호가 일치하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_NOT_ISSUED(HttpStatus.CONFLICT, "토큰을 발급받을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
