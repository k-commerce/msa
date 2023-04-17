package com.kcommerce.config;

import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springdoc.core.AbstractSwaggerUiConfigProperties.SwaggerUrl;

@Configuration
public class SwaggerConfig {

    @Bean
    public SwaggerUiConfigProperties swaggerUiConfig(SwaggerUiConfigProperties config) {
        config.setUrls(
                Stream.of("member", "item", "order")
                        .map(name -> new SwaggerUrl(name, "/v3/api-docs/" + name, name))
                        .collect(Collectors.toSet())
        );
        return config;
    }
}
