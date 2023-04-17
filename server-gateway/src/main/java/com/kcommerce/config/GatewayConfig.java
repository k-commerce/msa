package com.kcommerce.config;

import com.kcommerce.jwt.AuthenticationGatewayFilterFactory;
import com.kcommerce.jwt.AuthorizationGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                           AuthenticationGatewayFilterFactory authenticationGatewayFilterFactory,
                                           AuthorizationGatewayFilterFactory authorizationGatewayFilterFactory) {

        GatewayFilter authenticationGatewayFilter = authenticationGatewayFilterFactory.apply(new AuthenticationGatewayFilterFactory.Config());
        GatewayFilter authorizationGatewayFilter = authorizationGatewayFilterFactory.apply(new AuthorizationGatewayFilterFactory.Config());

        return builder.routes()
                .route(
                        r -> r.path("/api/auth")
                                .filters(f -> f.filter(authenticationGatewayFilter))
                                .uri("lb://server-member")
                )
                .route(
                        r -> r.path("/api/members")
                                .uri("lb://server-member")
                )
                .route(
                        r -> r.path("/api/addresses/**")
                                .filters(f -> f.filter(authorizationGatewayFilter))
                                .uri("lb://server-member")
                )
                .route(
                        r -> r.path("/api/items/**")
                                .uri("lb://server-item")
                )
                .route(
                        r -> r.path("/api/categories/**")
                                .uri("lb://server-item")
                )
                .route(
                        r -> r.path("/api/orders/**")
                                .filters(f -> f.filter(authorizationGatewayFilter))
                                .uri("lb://server-order")
                )
                .route(
                        r -> r.path("/v3/api-docs/member")
                                .filters(f -> f.rewritePath("/v3/api-docs/member", "/v3/api-docs"))
                                .uri("lb://server-member")
                )
                .route(
                        r -> r.path("/v3/api-docs/item")
                                .filters(f -> f.rewritePath("/v3/api-docs/item", "/v3/api-docs"))
                                .uri("lb://server-item")
                )
                .route(
                        r -> r.path("/v3/api-docs/order")
                                .filters(f -> f.rewritePath("/v3/api-docs/order", "/v3/api-docs"))
                                .uri("lb://server-order")
                )
                .build();
    }
}
