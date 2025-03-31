package com.zerobase.challengeproject.challenge.entity;

import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.comment.entity.CoteComment;
import com.zerobase.challengeproject.type.CommentType;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String title;

    private String img;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer participant;

    @Column(nullable = false)
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

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    List<CoteComment> coteComments;
//    @OneToMany(mappedBy = "callenge", fetch = FetchType.LAZY)
//    List<DrinkingComment> drinkingComments;
//    @OneToMany(mappedBy = "callenge", fetch = FetchType.LAZY)
//    List<DietComment> dietComments;

    @Column(nullable = false)
    private LocalDateTime createAt;
    
    private LocalDateTime updateAt;


    /**
     * 클라이언트로부터 받은 정보로 챌린지 생성
     * @param dto
     */
    public Challenge(ChallengeForm dto, Member member) {

        this.title = dto.getTitle();
        this.member = member;
        this.img = dto.getImg();
        this.participant = dto.getParticipant();
        this.max_deposit = dto.getMax_deposit();
        this.standard = dto.getStandard();
        this.min_deposit = dto.getMin_deposit();
        this.description = dto.getDescription();
        this.commentType = dto.getCommentType();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.createAt = LocalDateTime.now();
    }

    /**
     * 클라이언트로부터 받은 정보로 챌린지 수정
     * @param dto
     */
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
