package com.kcommerce.jwt;

public interface JwtProperties {

    long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30 minutes

    long REFRESH_TOKEN_TIME = 2 * 7 * 24 * 60 * 60 * 1000L; // 2 weeks

    String BEARER_PREFIX = "Bearer ";

    String REFRESH_TOKEN_COOKIE = "REFRESH_TOKEN";
}
