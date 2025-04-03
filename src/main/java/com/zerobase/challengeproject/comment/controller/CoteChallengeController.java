package com.zerobase.challengeproject.comment.controller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteCommentDto;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeForm;
import com.zerobase.challengeproject.comment.domain.form.CoteCommentForm;
import com.zerobase.challengeproject.comment.service.CommentService;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/challenge")
public class CoteChallengeController {
  private final CommentService commentService;

  @PostMapping("/cote")
  public ResponseEntity<BaseResponseDto<CoteChallengeDto>> createCoteChallenge(
          @RequestBody @Valid CoteChallengeForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok(commentService.addCoteChallenge(form, userDetails));
  }

  @PostMapping("/comment")
  public ResponseEntity<BaseResponseDto<CoteCommentDto>> addComment(
          @RequestBody @Valid CoteCommentForm form,
          @AuthenticationPrincipal UserDetailsImpl userDetails){
    return ResponseEntity.ok(commentService.addComment(form, userDetails));
  }

}
