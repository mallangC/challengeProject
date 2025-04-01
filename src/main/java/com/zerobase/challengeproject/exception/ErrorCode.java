package com.zerobase.challengeproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
  ALREADY_REGISTER_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
  ALREADY_REGISTER_LOGIN_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  ALREADY_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이미 인증된 메일입니다."),
  EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
  CONFIRM_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "확인 비밀번호가 일치하지 않습니다."),

  NOT_ENOUGH_MONEY_TO_REFUND(HttpStatus.BAD_REQUEST, "환불할 금액이 부족합니다."),
  NOT_FOUND_ACCOUNT_DETAIL(HttpStatus.BAD_REQUEST, "계좌 내역을 찾을 수 없습니다."),
  NOT_CHARGE_DETAIL(HttpStatus.BAD_REQUEST, "충전 내역 아이디가 아닙니다."),
  ALREADY_REFUNDED(HttpStatus.BAD_REQUEST, "이미 환불된 내역입니다."),
  ALREADY_SPENT_MONEY(HttpStatus.BAD_REQUEST, "이미 사용한 금액은 환불할 수 없습니다."),
  ALREADY_DONE(HttpStatus.BAD_REQUEST, "이미 처리된 환불 신청입니다."),
  ALREADY_REFUND_REQUEST(HttpStatus.BAD_REQUEST, "이미 환불 신청한 내역입니다."),
  NOT_FOUND_REFUND(HttpStatus.BAD_REQUEST, "환불 신청을 찾을 수 없습니다."),

  NOT_FOUND_CHALLENGES(HttpStatus.BAD_REQUEST, "챌린지를 찾을 수 없습니다."),
  NOT_FOUND_CHALLENGE(HttpStatus.BAD_REQUEST, "챌린지를 조회할 수 없습니다."),

  INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다" ),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다" ),
  TOKEN_IS_INVALID_OR_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 잘 못되었거나 만료되었습니다" ),
  TOKEN_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "리프레시 토큰이 제공되지 않았습니다." )
  ;



  private final HttpStatus httpStatus;
  private final String message;
}
