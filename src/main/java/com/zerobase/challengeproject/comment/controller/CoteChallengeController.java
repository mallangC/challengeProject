package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteCommentDto;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeForm;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeUpdateForm;
import com.zerobase.challengeproject.comment.domain.form.CoteCommentForm;
import com.zerobase.challengeproject.comment.service.CommentService;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge")
public class CoteChallengeController {
  private final CommentService commentService;

  @PostMapping("/cote")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> addCoteChallenge(
          @RequestBody @Valid CoteChallengeForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(commentService.addCoteChallenge(form, userDetails));
  }

  @GetMapping("/cote/{coteChallengeId}")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> getCoteChallenge(
          @PathVariable Long coteChallengeId) {
    return ResponseEntity.ok(commentService.getCoteChallenge(coteChallengeId));
  }

  @PatchMapping("/cote")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> updateCoteChallenge(
          @RequestBody @Valid CoteChallengeUpdateForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(commentService.updateCoteChallenge(form, userDetails));
  }


  @DeleteMapping("/cote/{coteChallengeId}")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> deleteCoteChallenge(
          @PathVariable Long coteChallengeId,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(commentService.deleteCoteChallenge(coteChallengeId, userDetails));
  }

  @PostMapping("/comment")
  public ResponseEntity<BaseResponseDto<CoteCommentDto>> addComment(
          @RequestBody @Valid CoteCommentForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(commentService.addComment(form, userDetails));
  }

}
