package com.portfolio.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
public class CorsConfig  {

	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();

	        // ✅ Allow your frontend URLs
	        configuration.setAllowedOrigins(List.of(
	                "https://student-portfolio-production.up.railway.app",
	                "https://fsad-student-portfolio.vercel.app",
	                "http://localhost:5173"
	        ));

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