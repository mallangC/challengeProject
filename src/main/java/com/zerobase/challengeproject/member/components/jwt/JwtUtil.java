package com.zerobase.challengeproject.member.components.jwt;

import com.zerobase.challengeproject.type.MemberType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000;
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    public String generateAccessToken(String username, MemberType role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username, MemberType role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    public String extractRoles(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    /**
     * 리프레시 토큰 쿠키 생성
     * @param refreshToken 리프레시 토큰
     * @return ResponseCookie
     */
    public ResponseCookie createRefreshTokenCookie(String refreshToken, int expiresIn) {
        boolean isSecure = !isLocalEnvironment();

        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge( expiresIn * 24 * 60 * 60)
                .build();
    }

    // 로컬 환경인지 확인하는 메서드
    private boolean isLocalEnvironment() {
        String profile = System.getProperty("spring.profiles.active", "default");
        return profile.equals("local") || profile.equals("default");
    }
}
