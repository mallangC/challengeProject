package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.domain.dto.MemberDto;
import com.zerobase.challengeproject.comment.domain.dto.PageDto;
import com.zerobase.challengeproject.comment.domain.dto.RefundDto;
import com.zerobase.challengeproject.comment.domain.form.AccountAddForm;
import com.zerobase.challengeproject.comment.domain.form.RefundAddForm;
import com.zerobase.challengeproject.comment.domain.form.RefundSearchForm;
import com.zerobase.challengeproject.comment.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
  @GetMapping("/member")
  public ResponseEntity<MemberDto> getAccountDetail() {
    return ResponseEntity.ok(accountService.getMember());
  }

  /**
   * 회원 계좌에 금액 충전
   * TODO 토큰 파라미터 추가
   */
  @PostMapping
  public ResponseEntity<BaseResponseDto<AccountDetailDto>> addAmount(
          @Valid @RequestBody AccountAddForm form) {
    return ResponseEntity.ok(accountService.addAmount(form));
  }

  /**
   * 전체 계좌 내역 조회 (페이징)
   */
  @GetMapping
  public ResponseEntity<BaseResponseDto<PageDto<AccountDetailDto>>> getAllAccountDetail(
          @RequestParam int page) {
    return ResponseEntity.ok(accountService.getAllAccounts(page));
  }


  /**
   * 회원이 충전했던 금액을 환불 신청
   */
  @PostMapping("/refund")
  public ResponseEntity<BaseResponseDto<RefundDto>> refundRequest(
          @RequestBody RefundAddForm form) {
    return ResponseEntity.ok(accountService.addRefund(form));
  }


  /**
   * 회원의 환불 신청 취소
   */
  @DeleteMapping("/refund")
  public ResponseEntity<BaseResponseDto<RefundDto>> cancelRefundRequest(
          @RequestParam Long refundId) {
    return ResponseEntity.ok(accountService.cancelRefund(refundId));
  }

//  /**
//   * 회원의 환불 신청 확인
//   */
//  @GetMapping("/refund")
//  public ResponseEntity<BaseResponseDto<PageDto<RefundDto>>> getAllRefund(
//          @RequestParam @Min(1) int page,
//          @RequestParam Boolean done,
//          @RequestParam Boolean refunded) {
//    return ResponseEntity.ok(accountService.getAllRefund(page, startAtStr, done, refunded));
//  }


  /**
   * 관리자가 환불 내역 확인
   */
  //  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/refund/admin")
  public ResponseEntity<BaseResponseDto<PageDto<RefundDto>>> getAllRefund(
          @RequestParam @Min(1) int page,
          @RequestBody RefundSearchForm form) {
    return ResponseEntity.ok(accountService.getAllRefund(page, form));
  }


  /**
   * 관리자는 모든 사용자의 충전내역을 환불할 수 있다.
   */
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PatchMapping("/refund/admin")
  public ResponseEntity<BaseResponseDto<RefundDto>> refundAmount(
          @RequestParam Long refundId) {
    return ResponseEntity.ok(accountService.refundApproval(refundId));
  }


}
