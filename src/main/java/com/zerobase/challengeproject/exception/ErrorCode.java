package com.zerobase.challengeproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
  NOT_FOUND_CHALLENGES(HttpStatus.BAD_REQUEST, "챌린지를 찾을 수 없습니다."),
  NOT_FOUND_CHALLENGE(HttpStatus.BAD_REQUEST, "챌린지를 조회할 수 없습니다."),
  INVALID_DEPOSIT_AMOUNT(HttpStatus.BAD_REQUEST, "최소보증금이 최대보증금보다 작아야 합니다."),
  INVALID_PARTICIPANT_NUMBER(HttpStatus.BAD_REQUEST, "최소참여인원은 1명이상이어야 합니다."),
  INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "시작날짜는 종료날짜보다 먼저이어야 합니다.")

  ;
  private final HttpStatus httpStatus;
  private final String message;
}
