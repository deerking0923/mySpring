package com.springboot.ezenbackend.config;

import java.util.Formatter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.ezenbackend.controller.LocalDateFormatter;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
    }

    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**")
    // .allowedOrigins("*")
    // .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
    // .maxAge(300)
    // .allowedHeaders("Authorization", "Content-Type", "Chache-Control");
    // }
}
