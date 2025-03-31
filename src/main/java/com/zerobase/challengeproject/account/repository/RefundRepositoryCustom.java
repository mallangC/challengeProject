package com.zerobase.challengeproject.account.repository;

import com.zerobase.challengeproject.account.domain.dto.RefundDto;
import com.zerobase.challengeproject.account.entity.Refund;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface RefundRepositoryCustom {
  Page<RefundDto> searchAllRefund(int page, LocalDateTime startAt, Boolean isDone, Boolean isRefunded);

  Refund searchRefundById(Long id);

  Page<RefundDto> searchAllMyRefund(int page, String userId);
}
