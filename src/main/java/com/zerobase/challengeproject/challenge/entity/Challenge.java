package com.zerobase.challengeproject.challenge.entity;

import com.zerobase.challengeproject.challenge.domain.form.CreateChallengeForm;
import com.zerobase.challengeproject.challenge.domain.form.UpdateChallengeForm;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.type.CategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "challenge")
    private List<MemberChallenge> challengeMembers;

    @Column(nullable = false)
    private String title;

    private String img;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CategoryType categoryType;

    @Column(nullable = false)
    private Integer participant;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer minDeposit;

    @Column(nullable = false)
    private Integer maxDeposit;

    @Column(nullable = false)
    private String standard;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

//    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
//    @OneToMany(mappedBy = "callenge", fetch = FetchType.LAZY)
//    List<DrinkingComment> drinkingComments;
//    @OneToMany(mappedBy = "callenge", fetch = FetchType.LAZY)
//    List<DietComment> dietComments;

    private LocalDateTime createAt;
    
    private LocalDateTime updateAt;


    /**
     * 클라이언트로부터 받은 정보로 챌린지 생성
     * @param form
     */
    public Challenge(CreateChallengeForm form, Member member) {

        this.title = form.getTitle();
        this.member = member;
        this.categoryType = form.getCategoryType();
        this.img = form.getImg();
        this.participant = form.getParticipant();
        this.maxDeposit = form.getMaxDeposit();
        this.standard = form.getStandard();
        this.minDeposit = form.getMinDeposit();
        this.description = form.getDescription();
        this.startDate = form.getStartDate();
        this.endDate = form.getEndDate();
        this.createAt = LocalDateTime.now();
    }

    public void update(UpdateChallengeForm form) {

        if (form.getTitle() != null) this.setTitle(form.getTitle());
        if (form.getCategoryType() != null) this.setCategoryType(form.getCategoryType());
        if (form.getStandard() != null) this.setStandard(form.getStandard());
        if (form.getImg() != null) this.setImg(form.getImg());
        if (form.getParticipant() != null) this.setParticipant(form.getParticipant());
        if (form.getDescription() != null) this.setDescription(form.getDescription());
        if (form.getMinDeposit() != null) this.setMinDeposit(form.getMinDeposit());
        if (form.getMaxDeposit() != null) this.setMaxDeposit(form.getMaxDeposit());
        if (form.getStartDate() != null) this.setStartDate(form.getStartDate());
        if (form.getEndDate() != null) this.setEndDate(form.getEndDate());
        this.updateAt = LocalDateTime.now();
    }


    /**
     * 클라이언트로부터 받은 정보로 챌린지 수정
     * @param dto
     */

}
