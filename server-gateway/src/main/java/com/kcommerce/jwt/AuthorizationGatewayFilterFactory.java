package com.kcommerce.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    private final JwtProvider jwtProvider;
    private final Logger log = LoggerFactory.getLogger(AuthorizationGatewayFilterFactory.class);

    public AuthorizationGatewayFilterFactory(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            try {
                String accessToken = getAccessToken(request);
                String userId = jwtProvider.getSubject(accessToken);

                if (userId == null) {
                    String refreshToken = getRefreshToken(request);
                    userId = jwtProvider.getSubject(refreshToken);

                    if (userId == null) {
                        return onError(response, "jwt has expired");
                    }

                    accessToken = jwtProvider.createJwt(userId, JwtProperties.ACCESS_TOKEN_TIME);
                    response.getHeaders().set(HttpHeaders.AUTHORIZATION, accessToken);
                }

                request.mutate().header("X-Authorization-Id", userId).build();
            } catch (Exception e) {
                return onError(response, e.getMessage());
            }

            return chain.filter(exchange);
        };
    }

    private String getAccessToken(ServerHttpRequest request) {
        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(JwtProperties.BEARER_PREFIX)) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private String getRefreshToken(ServerHttpRequest request) {
        HttpCookie refreshTokenCookie = request.getCookies().getFirst(JwtProperties.REFRESH_TOKEN_COOKIE);
        return refreshTokenCookie != null ? refreshTokenCookie.getValue() : null;
    }

    private Mono<Void> onError(ServerHttpResponse response, String message) {
        log.error(message);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
    }
}
