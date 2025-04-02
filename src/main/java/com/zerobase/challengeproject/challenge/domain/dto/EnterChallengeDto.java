package com.zerobase.challengeproject.challenge.domain.dto;

import com.zerobase.challengeproject.challenge.entity.Challenge;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterChallengeDto {
    private Long challengeId;
    private String title;
    private String category;
    private Integer myDeposit;
    private Integer minDeposit;



    public EnterChallengeDto(Challenge challenge, Integer memberDeposit) {
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.category = challenge.getCategory().toString();
        this.myDeposit = memberDeposit;
        this.minDeposit = challenge.getMin_deposit();
    }
}
