package com.zerobase.challengeproject.comment.domain.dto;

import com.zerobase.challengeproject.comment.entity.Refund;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
@Getter
public class RefundDto {
  private String accountDetailId;
  private String memberContent;
  private String adminContent;
  private boolean isRefunded;
  private boolean isDone;
  private LocalDateTime createdAt;

  public static RefundDto from(Refund refund) {
    return RefundDto.builder()
            .accountDetailId(refund.getId().toString())
            .memberContent(refund.getMemberContent())
            .adminContent(refund.getAdminContent())
            .isRefunded(refund.isRefunded())
            .isDone(refund.isDone())
            .createdAt(refund.getCreatedAt())
            .build();
  }
}
