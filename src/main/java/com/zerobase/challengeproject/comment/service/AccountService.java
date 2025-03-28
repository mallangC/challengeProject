package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.domain.dto.MemberDto;
import com.zerobase.challengeproject.comment.domain.form.AccountAddForm;
import com.zerobase.challengeproject.comment.entity.AccountDetail;
import com.zerobase.challengeproject.comment.entity.Member;
import com.zerobase.challengeproject.comment.repository.AccountDetailRepository;
import com.zerobase.challengeproject.comment.repository.MemberRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final MemberRepository memberRepository;
  private final AccountDetailRepository accountDetailRepository;

  public MemberDto getMember(){
    return MemberDto.fromWithoutAccountDetails(searchMember("test@company.com"));
  }

  /**
   * 회원이 금액을 충전하기 위한 서비스 메서드
   * 충전 내역이 DB에 저장되고 회원 계좌에 금액이 충전
   * @param form 회원이 충전할 금액
   * @return id, updateAt을 제외한 모든 충전내역
   */
  @Transactional
  public BaseResponseDto<AccountDetailDto> addAmount(AccountAddForm form){
    //토큰 provider에서 토큰 해석
    String userId = "test@company.com";
    Member member = searchMember(userId);

    Long amount = form.getChargeAmount();
    AccountDetail detail = AccountDetail.charge(member, amount);
    accountDetailRepository.save(detail);

    member.chargeAccount(amount);
    return new BaseResponseDto<AccountDetailDto>(
            AccountDetailDto.from(detail),
            amount+"원 충전을 성공했습니다.",
            HttpStatus.OK);
  }


  /**
   * 회원이 이전에 충전한 금액을 환불하기 위한 서비스 메서드
   * 환불할 충전 내역이 없거나, 이미 환불 받았거나, 충전 내역이 아니거나,
   * 환불할 충전 내역과 내역 이후에 충전한 전체 금액이(이미 환불된건 제외)이
   * 지금 계좌에 있는금액보다 크면(이미 사용했다고 판단) 예외 발생
   * 환불이 가능한 경우
   * @param id 환불할 충전 내역 아이디
   * @return id, updateAt을 제외한 모든 환불 내역
   */
  @Transactional
  public BaseResponseDto<AccountDetailDto> refundAmount(Long id){
    //토큰 provider에서 토큰 해석
    String userId = "test@company.com";

    AccountDetail accountDetail = accountDetailRepository.findById(id)
            .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT_DETAIL));
    if(!accountDetail.isCharge()){
      throw new CustomException(ErrorCode.NOT_CHARGE_DETAIL);
    }

    Member member = memberRepository.searchByEmailAndSearchByAccountDetailsToDate(userId, accountDetail.getCreatedAt());
    Long amount = accountDetail.getAmount();
    AccountDetail refundDetail = AccountDetail.refund(member, amount);
    accountDetailRepository.save(refundDetail);
    member.refundAccount(accountDetail);
    return new BaseResponseDto<AccountDetailDto>(AccountDetailDto.from(refundDetail),
            amount + "원 환불을 성공했습니다.",
            HttpStatus.OK);
  }

  private Member searchMember(String userId){
    return memberRepository.findByMemberId(userId)
            .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
  }
}
