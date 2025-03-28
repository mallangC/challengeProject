package com.zerobase.challengeproject.comment.domain.dto;

import com.zerobase.challengeproject.comment.entity.Refund;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class RefundDto {
  private String accountDetailId;
  private String memberContent;
  private String adminContent;
  private boolean isRefunded;
  private boolean isDone;

  public static RefundDto from(Refund refund) {
    return RefundDto.builder()
            .accountDetailId(refund.getId().toString())
            .memberContent(refund.getMemberContent())
            .adminContent(refund.getAdminContent())
            .isRefunded(refund.isRefunded())
            .isDone(refund.isDone())
            .build();
  }
}
