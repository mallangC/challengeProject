package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.AccountDetailDto;
import com.zerobase.challengeproject.comment.domain.dto.MemberDto;
import com.zerobase.challengeproject.comment.domain.dto.PageDto;
import com.zerobase.challengeproject.comment.domain.dto.RefundDto;
import com.zerobase.challengeproject.comment.domain.form.AccountAddForm;
import com.zerobase.challengeproject.comment.domain.form.RefundAddForm;
import com.zerobase.challengeproject.comment.entity.AccountDetail;
import com.zerobase.challengeproject.comment.entity.Member;
import com.zerobase.challengeproject.comment.entity.Refund;
import com.zerobase.challengeproject.comment.repository.AccountDetailRepository;
import com.zerobase.challengeproject.comment.repository.MemberRepository;
import com.zerobase.challengeproject.comment.repository.RefundRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final MemberRepository memberRepository;
  private final AccountDetailRepository accountDetailRepository;
  private final RefundRepository refundRepository;

  public MemberDto getMember() {
    return MemberDto.fromWithoutAccountDetails(searchMember("test@company.com"));
  }

  /**
   * 회원이 계좌 내역을 조회하기 위한 서비스 메서드
   * page를 사용하기 때문에 총 계좌내역 갯수를 알기 위한 쿼리가 따로 실행되어 쿼리가 두번 실행
   * 내역을 찾을 수 없는 경우 빈 페이지로 반환
   *
   * @param page 찾은 계좌 내역 페이지
   * @return 계좌 내역과 총 갯수, 총페이지, 현재 페이지, 한페이지에 표시되는 계좌내역의 갯수 정보
   */
  public BaseResponseDto<PageDto<AccountDetailDto>> getAllAccounts(int page) {
    //토큰 provider에서 토큰 해석
    String userId = "test@company.com";
    Page<AccountDetailDto> paging = accountDetailRepository.searchAllAccountDetail(page, userId);
    return new BaseResponseDto<>(PageDto.from(paging)
            , "계좌 내역 조회에 성공했습니다.(" + (page + 1) + "페이지)"
            , HttpStatus.OK);
  }


  /**
   * 회원이 금액을 충전하기 위한 서비스 메서드
   * 충전 내역이 DB에 저장되고 회원 계좌에 금액이 충전
   *
   * @param form 회원이 충전할 금액
   * @return id, updateAt을 제외한 모든 충전내역
   */
  @Transactional
  public BaseResponseDto<AccountDetailDto> addAmount(AccountAddForm form) {
    //토큰 provider에서 토큰 해석
    String userId = "test@company.com";
    Member member = searchMember(userId);

    Long amount = form.getChargeAmount();
    AccountDetail detail = AccountDetail.charge(member, amount);
    accountDetailRepository.save(detail);

    member.chargeAccount(amount);
    return new BaseResponseDto<AccountDetailDto>(
            AccountDetailDto.from(detail),
            amount + "원 충전을 성공했습니다.",
            HttpStatus.OK);
  }


  /**
   * 관리자가 회원이 한 환불 신청을 승인하고 환불을 해주기 위한 서비스 메서드
   * 환불할 충전 내역이 없거나, 이미 환불 받았거나, 충전 내역이 아니거나,
   * 환불할 충전 내역과 내역 이후에 충전한 전체 금액이(이미 환불된건 제외)이
   * 지금 계좌에 있는금액보다 크면(이미 사용했다고 판단) 예외 발생
   * 환불이 가능한 경우
   *
   * @param id 환불할 충전 내역 아이디
   * @return id, updateAt을 제외한 모든 환불 내역
   */
  @Transactional
  public BaseResponseDto<AccountDetailDto> refundAmount(Long id) {
    //토큰 provider에서 토큰 해석
    String userId = "test@company.com";

    AccountDetail accountDetail = accountDetailRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOUNT_DETAIL));
    if (!accountDetail.isCharge()) {
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

  /**
   * 회원이 이전에 충전한 금액을 환불하기 위한 서비스 메서드
   * 이미 신청한 환불 내역이 있거나, 충전 내역을 찾을 수 없을 때 예외 발생
   *
   * @param form 환불 신청할 내역id, 환불 사유
   * @return 환불 신청에 대한 정보 (id 제외)
   */
  public BaseResponseDto<RefundDto> addRefund(RefundAddForm form) {
    //토큰 provider에서 토큰 해석
    String userId = "test@company.com";
    boolean isExist = refundRepository.existsByAccountDetail_Id(form.getAccountId());
    if (isExist) {
      throw new CustomException(ErrorCode.ALREADY_REFUND_REQUEST);
    }
    Member member = memberRepository.searchByEmailAndAccountDetailId(userId, form.getAccountId());

    Refund refund = Refund.from(form.getContent(), member);
    refundRepository.save(refund);

    return new BaseResponseDto<RefundDto>(RefundDto.from(refund),
            "환불을 신청했습니다.",
            HttpStatus.OK);
  }

  /**
   * 회원이 이전에 신청한 환불신청을 취소하는 서비스 메서드
   * 파라미터로 받은 id의 환불신청이 없으면 예외 발생
   *
   * @param refundId 취소할 환불신청 아이디
   * @return 취소하기 전 환불 신청 정보
   */
  @Transactional
  public BaseResponseDto<RefundDto> cancelRefund(Long refundId) {
    Refund refund = refundRepository.findById(refundId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REFUND));

    refundRepository.delete(refund);
    return new BaseResponseDto<RefundDto>(RefundDto.from(refund),
            "환불 신청을 취소했습니다.",
            HttpStatus.OK);
  }


  private Member searchMember(String userId) {
    return memberRepository.findByMemberId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
  }
}
