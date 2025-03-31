package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.domain.dto.MemberDto;
import com.zerobase.challengeproject.comment.domain.form.AccountAddForm;
import com.zerobase.challengeproject.comment.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

  private final AccountService accountService;

  /**
   * 회원 조회(계좌 확인을 위해 구현)
   */
  @GetMapping
  public ResponseEntity<MemberDto> getAccountDetail() {
    return ResponseEntity.ok(accountService.getMember());
  }

  /**
   * 회원 계좌에 금액 충전
   * TODO 토큰 파라미터 추가
   */
  @PostMapping
  public ResponseEntity<BaseResponseDto<AccountDetailDto>> addAmount(
          @Valid @RequestBody AccountAddForm form){
    return ResponseEntity.ok(accountService.addAmount(form));
  }

  /**
   * 충전했던 금액 환불
   */
  @PatchMapping
  public ResponseEntity<BaseResponseDto<AccountDetailDto>> refundAmount(
          @RequestParam Long accountDetailId){
    return ResponseEntity.ok(accountService.refundAmount(accountDetailId));
  }

}
