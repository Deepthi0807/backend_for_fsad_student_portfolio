package com.portfolio.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOriginsRaw;

	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();

	        // ✅ Allow frontend URLs sourced from APP_CORS_ALLOWED_ORIGINS env variable
	        List<String> allowedOrigins = Arrays.asList(allowedOriginsRaw.split(","));
	        configuration.setAllowedOrigins(allowedOrigins);

	        // ✅ Allow all HTTP methods
	        configuration.setAllowedMethods(List.of(
	                "GET", "POST", "PUT", "DELETE", "OPTIONS"
	        ));

	        // ✅ Allow all headers
	        configuration.setAllowedHeaders(List.of("*"));

	        // ✅ Allow credentials (JWT / cookies)
	        configuration.setAllowCredentials(true);

	        // ✅ Cache preflight response
	        configuration.setMaxAge(3600L);

	        // Apply this config to all endpoints
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);

	        return source;
	    }
}