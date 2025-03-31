package com.zerobase.challengeproject.comment.domain.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundAddForm {
  private Long accountId;
  private String content;
}
