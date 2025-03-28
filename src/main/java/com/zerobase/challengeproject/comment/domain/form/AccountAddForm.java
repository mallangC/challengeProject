package com.zerobase.challengeproject.comment.domain.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class AccountAddForm {
  @Min(value = 5000, message = "5천원 이상 금액으로 충전이 가능합니다.")
  @Max(value = 100000, message = "10만원 이하 금액으로 충전이 가능합니다.")
  private Long chargeAmount;
}
