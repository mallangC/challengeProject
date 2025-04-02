package com.zerobase.challengeproject.comment.entity;

import com.zerobase.challengeproject.account.entity.BaseEntity;
import com.zerobase.challengeproject.comment.domain.form.CoteCommentForm;
import com.zerobase.challengeproject.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoteComment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;
  @ManyToOne
  @JoinColumn(name = "cote_challenge_id")
  private CoteChallenge coteChallenge;
  private String solution;
  private Boolean isPass;
  private String content;

  public static CoteComment from(CoteCommentForm form,
                                 Member member,
                                 CoteChallenge coteChallenge) {
    return CoteComment.builder()
            .member(member)
            .coteChallenge(coteChallenge)
            .solution(form.getSolution())
            .content(form.getContent())
            .build();
  }
}
