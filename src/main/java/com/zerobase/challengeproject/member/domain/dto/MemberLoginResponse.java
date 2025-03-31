package com.zerobase.challengeproject.member.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginResponse {
    private String token;
    private String memberId;

    public MemberLoginResponse(String token, String memberId) {
        this.token = token;
        this.memberId = memberId;
    }
}
