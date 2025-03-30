package com.zerobase.challengeproject.config;

import com.zerobase.challengeproject.member.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF를 비활성화하여 Postman 등의 테스트 도구 사용에 용이하게 할 수 있습니다.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 회원가입과 같이 인증 없이 접근 가능한 엔드포인트를 허용합니다.
                        .requestMatchers("/api/member/sign-up",
                                "/api/member/email-auth",
                                "/api/member/login").permitAll()
                        // 그 외의 요청은 인증이 필요하도록 설정합니다.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
