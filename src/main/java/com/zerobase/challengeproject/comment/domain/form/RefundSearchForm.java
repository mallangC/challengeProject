package com.zerobase.challengeproject.comment.domain.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundSearchForm {
  private String startAtStr;
  private Boolean done;
  private Boolean refunded;
}
