package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.domain.dto.MemberDto;
import com.zerobase.challengeproject.comment.domain.dto.RefundDto;
import com.zerobase.challengeproject.comment.domain.form.AccountAddForm;
import com.zerobase.challengeproject.comment.domain.form.RefundAddForm;
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
   * 회원이 충전했던 금액을 환불 신청
   */
  @PostMapping("/refund")
  public ResponseEntity<BaseResponseDto<RefundDto>> refundRequest(
          @RequestBody RefundAddForm form){
    return ResponseEntity.ok(accountService.addRefund(form));
  }


   //환불 신청 취소
//  @DeleteMapping("/refund")
//  public ResponseEntity<BaseResponseDto<RefundDto>> cancelRefundRequest(
//          @RequestParam Long refundId){
//    return ResponseEntity.ok(accountService.);
//  }


  //관리자는 모든 사용자의 충전내역을 환불할 수 있다.
  //사유, 충전내역 id

//  @PreAuthorize("hasRole('ROLE_ADMIN')")
//  @PatchMapping("/refund")
//  public ResponseEntity<BaseResponseDto<RefundDto>> refundAmount(
//          @RequestParam Long accountDetailId){
//    return ResponseEntity.ok(accountService.refundAmount(accountDetailId));
//  }


}
