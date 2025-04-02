package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteCommentDto;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeForm;
import com.zerobase.challengeproject.comment.domain.form.CoteCommentForm;
import com.zerobase.challengeproject.comment.entity.CoteChallenge;
import com.zerobase.challengeproject.comment.entity.CoteComment;
import com.zerobase.challengeproject.comment.repository.CoteChallengeRepository;
import com.zerobase.challengeproject.comment.repository.CoteCommentRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CoteCommentRepository coteCommentRepository;
  private final CoteChallengeRepository coteChallengeRepository;
  private final MemberRepository memberRepository;


  //챌린지를 추가할 때 코테챌린지로 추가하고
  //코테챌린지에 문제 업데이트
  //챌린지쪽에 넣어야 할듯
  @Transactional
  public BaseResponseDto <CoteChallengeDto> updateCoteChallenge(CoteChallengeForm form) {
    CoteChallenge coteChallenge = searchCoteChallenge(form.getCoteChallengeId());
    coteChallenge.update(form);
    return new BaseResponseDto <CoteChallengeDto> (
            CoteChallengeDto.from(coteChallenge),
            "코테 챌린지 업데이트를 성공했습니다.",
            HttpStatus.OK);
  }


  //코테챌린지에서 댓글 추가
  public BaseResponseDto<CoteCommentDto> addComment(CoteCommentForm form, UserDetailsImpl userDetails) {
    CoteChallenge coteChallenge = searchCoteChallenge(form.getCoteChallengeId());
    Member member = memberRepository.findByMemberId(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    CoteComment coteComment = CoteComment.from(form, member, coteChallenge);
    coteCommentRepository.save(coteComment);
    return new BaseResponseDto <CoteCommentDto> (
            CoteCommentDto.from(coteComment),
            "댓글 추가를 성공했습니다.",
            HttpStatus.OK);
  }



  private CoteChallenge searchCoteChallenge(Long coteChallengeId) {
    return coteChallengeRepository.findById(coteChallengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COTE_CHALLENGE));
  }
}
