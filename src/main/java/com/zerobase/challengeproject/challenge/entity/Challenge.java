package com.zerobase.challengeproject.challenge.entity;

import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String img;

    @Column(nullable = false)
    private Integer participant;

    private String description;

    @Column(nullable = false)
    private Integer min_deposit;

    @Column(nullable = false)
    private Integer max_deposit;

    @Column(nullable = false)
    private String standard;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private LocalDateTime createAt;
    
    private LocalDateTime updateAt;


    // 챌린지 수정시 요청받은 정보로 업데이트
    public void update(ChallengeForm dto) {
        this.title = dto.getTitle();
        this.img = dto.getImg();
        this.description = dto.getDescription();
        this.max_deposit = dto.getMax_deposit();
        this.min_deposit = dto.getMin_deposit();
        this.standard = dto.getStandard();
        this.participant = dto.getParticipant();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.updateAt = LocalDateTime.now();
    }
}
