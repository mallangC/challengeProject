package com.zerobase.challengeproject.comment.domain.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoteChallengeForm {
  @NotBlank(message = "챌린지 아이디를 입력해주세요")
  private Long coteChallengeId;
  @NotBlank(message = "코테 문제의 제목 입력해주세요")
  private String title;
  @NotBlank(message = "코테 문제를 입력해주세요")
  private String question;
}
