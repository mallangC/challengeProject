package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.domain.form.AccountAddForm;
import com.zerobase.challengeproject.comment.entity.AccountDetail;
import com.zerobase.challengeproject.comment.entity.Member;
import com.zerobase.challengeproject.comment.repository.AccountDetailRepository;
import com.zerobase.challengeproject.comment.repository.MemberRepository;
import com.zerobase.challengeproject.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static com.zerobase.challengeproject.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private AccountDetailRepository accountDetailRepository;

  @InjectMocks
  private AccountService accountService;

  Member memberBase = Member.builder()
          .id(1L)
          .memberId("test@company.com")
          .account(10000L)
          .accountDetails(List.of())
          .build();

  Member memberSearch = Member.builder()
          .id(1L)
          .memberId("test@company.com")
          .account(10000L)
          .accountDetails(List.of(AccountDetail.builder()
                          .amount(5000L)
                          .isCharge(true)
                          .isRefunded(false)
                          .build(),
                  AccountDetail.builder()
                          .amount(5000L)
                          .isCharge(true)
                          .isRefunded(false)
                          .build(),
                  AccountDetail.builder()
                          .amount(5000L)
                          .isCharge(true)
                          .isRefunded(false)
                          .build()))
          .build();

  AccountAddForm form = AccountAddForm.builder()
          .chargeAmount(5000L)
          .build();

  @Test
  @DisplayName("금액 충전 성공")
  void addAmount() {
    //given
    given(memberRepository.findByMemberId(anyString()))
            .willReturn(Optional.of(memberBase));

    //when
    BaseResponseDto<AccountDetailDto> responseDto = accountService.addAmount(form);

    //then
    assertEquals(HttpStatus.OK, responseDto.getStatus());
    assertEquals("test@company.com", responseDto.getData().getMemberId());
    assertEquals(10000L, responseDto.getData().getPreAmount());
    assertEquals(15000L, responseDto.getData().getCurAmount());
    assertEquals(5000L, responseDto.getData().getAmount());
    assertTrue(responseDto.getData().isCharge());
    assertFalse(responseDto.getData().isRefunded());
    verify(accountDetailRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("금액 충전 실패(회원을 찾을 수 없음)")
  void addAmountFailure() {
    //given
    given(memberRepository.findByMemberId(anyString()))
            .willReturn(Optional.empty());
    try {
      //when
      BaseResponseDto<AccountDetailDto> responseDto = accountService.addAmount(form);
    } catch (CustomException e) {
      //then
      assertEquals(NOT_FOUND_MEMBER, e.getErrorCode());
    }
  }


  @Test
  @DisplayName("금액 환불 성공")
  void refundAmount() {
    //given
    given(memberRepository.searchByEmailAndSearchByAccountDetailsToDate(anyString(), any()))
            .willReturn(memberSearch);

    given(accountDetailRepository.findById(anyLong()))
            .willReturn(Optional.of(AccountDetail.builder()
                    .id(1L)
                    .amount(5000L)
                    .preAmount(10000L)
                    .curAmount(15000L)
                    .amount(5000L)
                    .isCharge(true)
                    .isRefunded(false)
                    .build()));

    //when
    BaseResponseDto<AccountDetailDto> responseDto = accountService.refundAmount(1L);

    //then
    assertEquals(HttpStatus.OK, responseDto.getStatus());
    assertEquals("test@company.com", responseDto.getData().getMemberId());
    assertEquals(10000L, responseDto.getData().getPreAmount());
    assertEquals(5000L, responseDto.getData().getCurAmount());
    assertEquals(5000L, responseDto.getData().getAmount());
    assertFalse(responseDto.getData().isCharge());
    assertFalse(responseDto.getData().isRefunded());
    verify(accountDetailRepository, times(1)).save(any());
  }


  @Test
  @DisplayName("금액 환불 실패(계좌 내역을 찾을 수 없음)")
  void refundAmountFailure1() {
    //given
    given(accountDetailRepository.findById(anyLong()))
            .willReturn(Optional.empty());
    try {
      //when
      BaseResponseDto<AccountDetailDto> responseDto = accountService.refundAmount(1L);
    }catch (CustomException e) {
      //then
      assertEquals(NOT_FOUND_ACCOUNT_DETAIL, e.getErrorCode());
    }
  }

  @Test
  @DisplayName("금액 환불 실패(충전 내역이 아님)")
  void refundAmountFailure2() {
    //given
    given(accountDetailRepository.findById(anyLong()))
            .willReturn(Optional.of(AccountDetail.builder()
                    .id(1L)
                    .amount(5000L)
                    .preAmount(10000L)
                    .curAmount(15000L)
                    .amount(5000L)
                    .isCharge(false)
                    .isRefunded(false)
                    .build()));
    try {
      //when
      BaseResponseDto<AccountDetailDto> responseDto = accountService.refundAmount(1L);
    }catch (CustomException e) {
      //then
      assertEquals(NOT_CHARGE_DETAIL, e.getErrorCode());
    }
  }



}