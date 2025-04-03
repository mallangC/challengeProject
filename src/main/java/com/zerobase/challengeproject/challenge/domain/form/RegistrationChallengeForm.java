package com.zerobase.challengeproject.challenge.domain.form;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationChallengeForm {

    @NotNull
    @Min(value = 0, message = "나의 보증금을 입력해 주세요. 챌린지의 최소보증금보다 높아야 합니다.")
    private Integer memberDeposit;

}
