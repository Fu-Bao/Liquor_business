package com.github.liquor_business.global.jwt.util;

public final class Constants {

    private Constants() {}

    public static final String HEADER_ACCESS_TOKEN_KEY = "Authorization";
    public static final String HEADER_REFRESH_TOKEN_KEY = "refresh_token";
    public static final String BEARER = "Bearer ";
    public static final Long ACCESS_TOKEN_VAILID_MILLISECOND = 1000L * 60 * 60 * 24 * 7;
    public static final Long REFRESH_TOKEN_VAILID_MILLISECOND = 1000L * 60 * 60 * 24 + 15;


}
