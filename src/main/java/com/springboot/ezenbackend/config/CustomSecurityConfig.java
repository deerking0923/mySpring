// 이 클래스는 Spring Security의 설정을 정의하는 클래스입니다.
// @Configuration 어노테이션을 사용하여 Spring Bean을 등록하고, CORS 및 HTTP 보안을 설정합니다.
package com.springboot.ezenbackend.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// @Configuration: 이 클래스가 설정 클래스로 사용된다는 것을 Spring에 알립니다.
@Configuration
// @RequiredArgsConstructor: 클래스 내 모든 final 필드에 대한 생성자를 자동으로 생성합니다. 현재는 필드가 없지만
// 유지보수를 위해 추가된 것으로 보입니다.
@RequiredArgsConstructor
// @Log4j2: Log4j2 로깅 프레임워크를 사용하여 로깅을 활성화합니다.
@Log4j2
public class CustomSecurityConfig {

    // SecurityFilterChain을 Bean으로 등록합니다. Spring Security 설정의 핵심 구성 요소입니다.
    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        // 콘솔에 로그 메시지를 출력합니다.
        System.out.println("security filter chain");
        // 로깅 프레임워크를 사용하여 로그 메시지를 출력합니다.
        log.info("security filter chain 적용!!!!!!");

        // CORS 설정을 활성화합니다.
        // HttpSecurity의 cors() 메서드를 사용하여 사용자 정의 CORS 설정을 적용합니다.
        http.cors(httpSecurityCorsConfigurer -> {
            // corsConfigurationSource() 메서드에서 정의한 설정을 사용합니다.
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // 세션 관리를 Stateless로 설정합니다.
        // SessionCreationPolicy.STATELESS: 서버가 세션을 생성하지 않고 클라이언트가 JWT 등을 통해 인증을 처리하도록
        // 합니다.
        http.sessionManagement(SessionConfig -> SessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CSRF(Cross-Site Request Forgery) 보호를 비활성화합니다.
        // RESTful API에서는 주로 JWT와 같은 인증 방식을 사용하므로 CSRF를 비활성화합니다.
        http.csrf(config -> config.disable());

        // 설정을 적용하고 SecurityFilterChain 객체를 반환합니다.
        return http.build();
    }

    // CORS 설정을 정의하는 메서드입니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CORS 설정 객체를 생성합니다.
        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 출처에서의 요청을 허용합니다.
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 허용할 HTTP 메서드를 지정합니다.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        // 허용할 HTTP 헤더를 지정합니다.
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        // 인증 정보(예: 쿠키, 헤더)를 허용합니다.
        configuration.setAllowCredentials(true);

        // CORS 설정을 URL 경로에 매핑합니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로("**")에 대해 위에서 정의한 CORS 설정을 적용합니다.
        source.registerCorsConfiguration("/**", configuration);

        // 구성된 CORS 설정 소스를 반환합니다.
        return source;
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        // PasswordEncoder 인터페이스를 구현한 BCryptPasswordEncoder 객체를 반환합니다.
        return new BCryptPasswordEncoder();
    }

}
