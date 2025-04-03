package com.zerobase.challengeproject.config;

import com.zerobase.challengeproject.member.components.jwt.JwtAuthenticationFilter;
import com.zerobase.challengeproject.member.components.jwt.JwtAuthorizationFilter;
import com.zerobase.challengeproject.member.components.jwt.JwtUtil;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * {@link AuthenticationConfiguration}을 기반으로 {@link AuthenticationManager} 인스턴스를 생성하여 제공합니다.
     *
     * 이 메서드는 인증 프로세스를 관리하는 {@link AuthenticationManager}를 제공합니다. 이를 통해 애플리케이션은
     * 로그인 과정에서 사용자를 인증하고 보안 컨텍스트를 관리할 수 있습니다. {@link AuthenticationConfiguration}을
     * 활용하여 필요한 인증 메커니즘을 제공합니다.
     *
     * @param authenticationConfiguration {@link AuthenticationConfiguration}을 사용하여 {@link AuthenticationManager}를 얻음
     * @return 사용자 인증을 위한 {@link AuthenticationManager}
     * @throws Exception {@link AuthenticationManager}를 가져오는 중에 오류가 발생할 경우 예외 발생
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * HTTP 요청을 가로채 JWT 토큰을 추출하고 검증하는 {@link JwtAuthenticationFilter} 빈을 생성하여 제공합니다.
     *
     * {@link JwtAuthenticationFilter}는 요청 헤더에서 JWT 토큰을 추출하고, {@link JwtUtil} 클래스를 사용하여
     * 토큰을 검증한 후, {@link AuthenticationManager}를 통해 사용자 인증 정보를 전달합니다.
     *
     * @param authenticationManager JWT 토큰을 기반으로 사용자를 인증하는 {@link AuthenticationManager}
     * @return JWT 기반 인증 요청을 필터링하는 {@link JwtAuthenticationFilter} 인스턴스
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtUtil, authenticationManager);
    }

    /**
     * HTTP 요청을 가로채 JWT 토큰을 기반으로 사용자의 권한을 확인하는 {@link JwtAuthorizationFilter} 빈을 생성하여 제공합니다.
     *
     * {@link JwtAuthorizationFilter}는 JWT 토큰을 검증하고 사용자 정보를 추출한 후, 사용자가 특정 엔드포인트에
     * 접근할 수 있는 권한을 가지고 있는지 확인합니다. 이를 통해 접근 제어를 enforce합니다.
     *
     * @return JWT 토큰을 기반으로 요청을 권한 검증하는 {@link JwtAuthorizationFilter} 인스턴스
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/member/sign-up",
                                "/api/member/email-auth",
                                "/api/member/login").permitAll()
                        .requestMatchers("/aip/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        http.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(jwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);



        /*
        if (!securityEnabled) {
            // 🔥 Security 비활성화 (로컬 테스트용)
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        } else {
            // ✅ Security 활성화 (운영 환경)
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/member/sign-up", "/api/member/email-auth", "/api/member/login").permitAll()
                    .anyRequest().authenticated()
            );
            http.sessionManagement(session
                    -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
            http.addFilterAt(jwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

        }

         */
        return http.build();
    }
}
