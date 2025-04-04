package com.zerobase.challengeproject.member.domain.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileFrom {
    @NotBlank
    private String nickname;
    @NotBlank
    private String phoneNum;
}
