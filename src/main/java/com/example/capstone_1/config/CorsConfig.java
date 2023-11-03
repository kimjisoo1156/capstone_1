package com.example.capstone_1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
//@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Value("${my-service-url}")
    private String serviceUrl;
    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://d10v0gqnjifjsl.cloudfront.net/");
        config.addAllowedOrigin(serviceUrl);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/auth/**", config);
        source.registerCorsConfiguration("/boards/**",config);
        source.registerCorsConfiguration("/replies/**",config);
        source.registerCorsConfiguration("/send-mail/**",config);
        source.registerCorsConfiguration("/api/member/**",config);

        return new CorsFilter(source);
    }
}
