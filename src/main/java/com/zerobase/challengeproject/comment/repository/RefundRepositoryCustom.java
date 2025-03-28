package com.zerobase.challengeproject.comment.repository;

import com.zerobase.challengeproject.comment.domain.dto.RefundDto;
import com.zerobase.challengeproject.comment.entity.Refund;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface RefundRepositoryCustom {
  Page<RefundDto> searchAllRefund(int page, LocalDateTime startAt, Boolean isDone, Boolean isRefunded);

  Refund searchRefundById(Long id);
}
