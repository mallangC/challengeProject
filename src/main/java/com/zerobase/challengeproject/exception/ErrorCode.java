package com.zerobase.challengeproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
  NOT_FOUND_CHALLENGES(HttpStatus.BAD_REQUEST, "챌린지를 찾을 수 없습니다."),
  NOT_FOUND_CHALLENGE(HttpStatus.BAD_REQUEST, "챌린지를 조회할 수 없습니다.");
  private final HttpStatus httpStatus;
  private final String message;
}
