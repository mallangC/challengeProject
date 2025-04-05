package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.DietChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.DietCommentDto;
import com.zerobase.challengeproject.comment.domain.form.DietChallengeAddForm;
import com.zerobase.challengeproject.comment.domain.form.DietChallengeUpdateForm;
import com.zerobase.challengeproject.comment.domain.form.DietCommentAddForm;
import com.zerobase.challengeproject.comment.service.DietChallengeService;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge/diet")
public class DietChallengeController {

  private final DietChallengeService dietChallengeService;

  //다이어트 챌린지 추가(참여할 때 작성)
  @PostMapping
  public ResponseEntity<BaseResponseDto<DietChallengeDto>> addDietChallenge(
          @RequestBody @Valid DietChallengeAddForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(dietChallengeService.addDietChallenge(form, userDetails));
  }

  //다이어트 챌린지 단건 조회
  @GetMapping("/{challengeId}")
  public ResponseEntity<BaseResponseDto<DietChallengeDto>> getDietChallenge(
          @PathVariable Long challengeId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(dietChallengeService.getDietChallenge(challengeId, userDetails));
  }

  //다이어트 챌린지 수정
  @PatchMapping
  public ResponseEntity<BaseResponseDto<DietChallengeDto>> updateDietChallenge(
          @RequestBody @Valid DietChallengeUpdateForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(dietChallengeService.updateDietChallenge(form, userDetails));
  }

  //다이어트 댓글 추가
  @PostMapping("/comment")
  public ResponseEntity<BaseResponseDto<DietCommentDto>> addComment(
          @RequestBody @Valid DietCommentAddForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(dietChallengeService.addDietComment(form, userDetails));
  }



  //다이어트 댓글 단건 확인
  @GetMapping("/comment/{commentId}")
  public ResponseEntity<BaseResponseDto<DietCommentDto>> getComment(
          @PathVariable Long commentId) {
    return ResponseEntity.ok(dietChallengeService.getDietComment(commentId));
  }


}
