package com.zerobase.challengeproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
  ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
  ALREADY_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이미 인증된 메일입니다."),
  EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
  CONFIRM_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "확인 비밀번호가 일치하지 않습니다."),

  INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 틀렸습니다."),
  TOKEN_IS_EXPIRATION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
