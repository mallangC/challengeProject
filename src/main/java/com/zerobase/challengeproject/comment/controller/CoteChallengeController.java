package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.account.domain.dto.PageDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteCommentDto;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeForm;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeUpdateForm;
import com.zerobase.challengeproject.comment.domain.form.CoteCommentForm;
import com.zerobase.challengeproject.comment.domain.form.CoteCommentUpdateForm;
import com.zerobase.challengeproject.comment.service.CoteChallengeService;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge/cote")
public class CoteChallengeController {
  private final CoteChallengeService coteChallengeService;

  //코테 챌린지 추가
  @PostMapping
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> addCoteChallenge(
          @RequestBody @Valid CoteChallengeForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(coteChallengeService.addCoteChallenge(form, userDetails));
  }

  //코테 챌린지 단건 조회
  @GetMapping("/{coteChallengeId}")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> getCoteChallenge(
          @PathVariable Long coteChallengeId) {
    return ResponseEntity.ok(coteChallengeService.getCoteChallenge(coteChallengeId));
  }

  //코테 챌린지 전체 조회
  @GetMapping
  public ResponseEntity<BaseResponseDto<PageDto<CoteChallengeDto>>> getCoteChallenge(
          @RequestParam @Min(1) int page,
          @RequestParam("id") Long challengeId) {
    return ResponseEntity.ok(coteChallengeService.getAllCoteChallenge(page, challengeId));
  }

  //코테 챌린지 수정
  @PatchMapping
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> updateCoteChallenge(
          @RequestBody @Valid CoteChallengeUpdateForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(coteChallengeService.updateCoteChallenge(form, userDetails));
  }

  //코테 챌린지 삭제
  @DeleteMapping("/{coteChallengeId}")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> deleteCoteChallenge(
          @PathVariable Long coteChallengeId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(coteChallengeService.deleteCoteChallenge(coteChallengeId, userDetails));
  }

  //코테 댓글 추가
  @PostMapping("/comment")
  public ResponseEntity<BaseResponseDto<CoteCommentDto>> addComment(
          @RequestBody @Valid CoteCommentForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(coteChallengeService.addComment(form, userDetails));
  }

  //코테 댓글 조회
  @GetMapping("/comment/{commentId}")
  public ResponseEntity<BaseResponseDto<CoteCommentDto>> getComment(
          @PathVariable Long commentId) {
    return ResponseEntity.ok(coteChallengeService.getComment(commentId));
  }

  //코테 댓글 수정
  @PatchMapping("/comment")
  public ResponseEntity<BaseResponseDto<CoteCommentDto>> updateComment(
          @RequestBody @Valid CoteCommentUpdateForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(coteChallengeService.updateComment(form, userDetails));
  }

  //코테 댓글 삭제
  @DeleteMapping("/comment/{commentId}")
  public ResponseEntity<BaseResponseDto<CoteCommentDto>> deleteComment(
          @PathVariable Long commentId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(coteChallengeService.deleteComment(commentId, userDetails));
  }


}
