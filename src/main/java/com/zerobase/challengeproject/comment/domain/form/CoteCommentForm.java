package com.zerobase.challengeproject.comment.domain.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoteCommentForm {
  @NotBlank(message = "코테 챌린지 아이디를 입력해주세요")
  private Long coteChallengeId;
  @NotBlank(message = "코테 문제풀이를 입력해주세요")
  private String solution;
  @NotBlank(message = "문제풀이에 대한 설명을 입력해주세요")
  private String content;
}
