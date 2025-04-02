package com.zerobase.challengeproject.comment.domain.dto;

import com.zerobase.challengeproject.comment.entity.CoteChallenge;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CoteChallengeDto {
  private Long id;
  private Long challengeId;
  private String title;
  private String question;
  private List<CoteCommentDto> comments;

  public static CoteChallengeDto from(CoteChallenge coteChallenge) {
    return CoteChallengeDto.builder()
            .id(coteChallenge.getId())
            .challengeId(coteChallenge.getId())
            .title(coteChallenge.getTitle())
            .question(coteChallenge.getQuestion())
            .comments(coteChallenge.getComments().stream()
                    .map(CoteCommentDto::from)
                    .toList())
            .build();
  }
}
