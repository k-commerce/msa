package com.kcommerce.jwt;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtProvider jwtProvider;

    public AuthenticationGatewayFilterFactory(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();

                    if (Objects.equals(response.getStatusCode(), HttpStatus.OK)) {
                        String xAuthorizationId = response.getHeaders().getFirst("X-Authorization-Id");

                        String accessToken = jwtProvider.createJwt(xAuthorizationId, JwtProperties.ACCESS_TOKEN_TIME);
                        String refreshToken = jwtProvider.createJwt(xAuthorizationId, JwtProperties.REFRESH_TOKEN_TIME);

                        ResponseCookie cookie = ResponseCookie.from(JwtProperties.REFRESH_TOKEN_COOKIE, refreshToken)
                                .maxAge(Duration.ofMillis(JwtProperties.REFRESH_TOKEN_TIME))
                                .httpOnly(true)
                                .build();

                        response.getHeaders().set(HttpHeaders.AUTHORIZATION, accessToken);
                        response.getHeaders().set(HttpHeaders.SET_COOKIE, cookie.toString());
                    }
                }));
    }

    public static class Config {
    }
}
