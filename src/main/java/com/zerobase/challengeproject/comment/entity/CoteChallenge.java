package com.zerobase.challengeproject.comment.entity;

import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoteChallenge {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  private Challenge challenge;
  private String title;
  private String question;
  @OneToMany
  @JoinColumn(name = "cote_comment_id")
  private List<CoteComment> comments;

  public static CoteChallenge from(CoteChallengeForm form, Challenge challenge) {
    return CoteChallenge.builder()
            .challenge(challenge)
            .title(form.getTitle())
            .question(form.getQuestion())
            .build();
  }

  public void update(CoteChallengeForm form) {
    this.title = form.getTitle();
    this.question = form.getQuestion();
  }
}
