package com.zerobase.challengeproject.challenge.domain.dto;


import com.zerobase.challengeproject.type.Category;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OngoingChallengeDto {

    private Long id;

    private String title;

    private String img;

    private Category category;

    private Integer participant;

    private String description;

    private Integer min_deposit;

    private Integer max_deposit;

    private String standard;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


    public OngoingChallengeDto(Challenge challenge) {
        this.id = challenge.getId();
        this.updateAt = challenge.getUpdateAt();
        this.createAt = challenge.getCreateAt();
        this.endDate = challenge.getEndDate();
        this.startDate = challenge.getStartDate();
        this.standard = challenge.getStandard();
        this.max_deposit = challenge.getMax_deposit();
        this.min_deposit = challenge.getMin_deposit();
        this.description = challenge.getDescription();
        this.participant = challenge.getParticipant();
        this.category = challenge.getCategory();
        this.img = challenge.getImg();
        this.title = challenge.getTitle();
    }
}
