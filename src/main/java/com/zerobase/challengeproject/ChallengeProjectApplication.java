package com.zerobase.challengeproject;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.crypto.SecretKey;
import java.util.Base64;

@EnableJpaAuditing
@SpringBootApplication
public class ChallengeProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChallengeProjectApplication.class, args);

    /*
    // HS256에 적합한 256비트 비밀 키 생성
    SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    // SecretKey를 Base64로 인코딩하여 문자열로 변환
    String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

    // 생성된 키 출력
    System.out.println("Encoded Secret Key: " + encodedKey);

     */
  }

}
