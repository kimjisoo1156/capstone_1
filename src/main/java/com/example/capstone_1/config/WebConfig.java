package com.example.capstone_1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**", "/js/**","/templates/board/**")
                .addResourceLocations("classpath:/static/css/", "classpath:/static/js/","classpath:/static/templates/board/");
    }
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 기존의 StringHttpMessageConverter를 제거
//        converters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
//
//        // UTF-8 문자 인코딩을 사용하는 새로운 StringHttpMessageConverter 추가
//        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
//        converters.add(stringConverter);
//    }
}
