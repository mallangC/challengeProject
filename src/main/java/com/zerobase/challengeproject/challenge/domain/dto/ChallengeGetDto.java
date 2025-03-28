package com.zerobase.challengeproject.challenge.domain.dto;


import com.zerobase.challengeproject.challenge.entity.Challenge;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ChallengeGetDto {

    private Long id;
    private String title;
    private String img;
    private Integer participant;
    private String description;
    private Integer min_deposit;
    private Integer max_deposit;
    private String standard;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;



    public ChallengeGetDto(Challenge challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.img = challenge.getImg();
        this.participant = challenge.getParticipant();
        this.description = challenge.getDescription();
        this.min_deposit = challenge.getMin_deposit();
        this.max_deposit = challenge.getMax_deposit();
        this.standard = challenge.getStandard();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.createAt = challenge.getCreateAt();
    }
}
