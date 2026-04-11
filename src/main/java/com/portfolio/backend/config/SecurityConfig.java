package com.portfolio.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Registers a CorsFilter bean that wraps the CorsConfigurationSource defined
 * in CorsConfig.  Placing CORS handling in a servlet Filter (rather than only
 * in Spring MVC's HandlerMapping) ensures that the Access-Control-* headers
 * are written for every request — including preflight OPTIONS calls, error
 * responses, and any path that bypasses the DispatcherServlet.
 */
@Configuration
public class SecurityConfig {

    /**
     * Spring Boot auto-orders this filter with HIGHEST_PRECEDENCE when the
     * bean is named "corsFilter", so it runs before any other filter in the
     * chain and always adds the required CORS headers.
     */
    @Bean
    public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
        return new CorsFilter(corsConfigurationSource);
    }
}
