package com.zerobase.challengeproject.account.domain.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefundSearchForm {
  private String startAtStr;
  private Boolean done;
  private Boolean refunded;
}
