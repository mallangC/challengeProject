package com.zerobase.challengeproject.comment.domain.dto;

import com.zerobase.challengeproject.comment.entity.CoteComment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoteCommentDto {
  private Long id;
  private String userId;
  private Long coteChallengeId;
  private String solution;
  private Boolean isPass;
  private String content;

  public static CoteCommentDto from(CoteComment coteComment) {
    return CoteCommentDto.builder()
            .id(coteComment.getId())
            .userId(coteComment.getMember().getMemberId())
            .coteChallengeId(coteComment.getCoteChallenge().getId())
            .solution(coteComment.getSolution())
            .isPass(coteComment.getIsPass())
            .content(coteComment.getContent())
            .build();
  }
}
