package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.account.domain.dto.PageDto;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.comment.domain.dto.DietChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.DietCommentDto;
import com.zerobase.challengeproject.comment.domain.form.DietChallengeAddForm;
import com.zerobase.challengeproject.comment.domain.form.DietChallengeUpdateForm;
import com.zerobase.challengeproject.comment.domain.form.DietCommentAddForm;
import com.zerobase.challengeproject.comment.domain.form.DietCommentUpdateForm;
import com.zerobase.challengeproject.comment.entity.DietChallenge;
import com.zerobase.challengeproject.comment.entity.DietComment;
import com.zerobase.challengeproject.comment.repository.DietChallengeRepository;
import com.zerobase.challengeproject.comment.repository.DietCommentRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.type.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DietChallengeService {
  private final ChallengeRepository challengeRepository;
  private final DietChallengeRepository dietChallengeRepository;
  private final DietCommentRepository dietCommentRepository;

  //다이어트 챌린지 추가 (form, userDetails)
  public BaseResponseDto<DietChallengeDto> addDietChallenge(DietChallengeAddForm form,
                                                            UserDetailsImpl userDetails) {
    Member member = userDetails.getMember();
    Challenge challenge = challengeRepository.searchChallengeById(form.getChallengeId());
    if (challenge.getCategoryType() != CategoryType.DIET) {
      throw new CustomException(ErrorCode.NOT_DIET_CHALLENGE);
    }

    if (!Objects.equals(challenge.getMember().getMemberId(), member.getMemberId())) {
      throw new CustomException(ErrorCode.NOT_OWNER_OF_CHALLENGE);
    }

    boolean isExist = challenge.getDietChallenges().stream()
            .anyMatch(c -> c.getMember().getMemberId().equals(member.getMemberId()));
    if (isExist) {
      throw new CustomException(ErrorCode.ALREADY_ADDED_DIET_CHALLENGE);
    }

    DietChallenge dietChallenge = DietChallenge.from(form, member, challenge);
    DietComment dietComment = DietComment.builder()
            .dietChallenge(dietChallenge)
            .member(member)
            .image(form.getImage())
            .content("참여 인증")
            .build();
    dietChallengeRepository.save(dietChallenge);
    dietCommentRepository.save(dietComment);

    return new BaseResponseDto<>(DietChallengeDto.from(dietChallenge),
            "다이어트 챌린지 추가를 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 챌린지 단건 조회 (challengeId, userDetails)(DB호출 1회) 호출 1
  public BaseResponseDto<DietChallengeDto> getDietChallenge(Long challengeId,
                                                            UserDetailsImpl userDetails) {
    DietChallenge dietChallenge =
            dietChallengeRepository.searchDietChallengeByChallengeIdAndLoginId(
                    challengeId, userDetails.getUsername());
    return new BaseResponseDto<>(DietChallengeDto.from(dietChallenge),
            "다이어트 챌린지 단건 조회를 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 챌린지 전체 조회 (challengeId, userDetails)
  public BaseResponseDto<PageDto<DietChallengeDto>> getAllDietChallenge(int page,
                                                                        Long challengeId) {
    Page<DietChallengeDto> dietChallengeDtos =
            dietChallengeRepository.searchDietChallengeByChallengeId(page - 1, challengeId);
    return new BaseResponseDto<>(PageDto.from(dietChallengeDtos),
            "다이어트 챌린지 전체 조회를 성공했습니다.(" + page + "페이지)",
            HttpStatus.OK);
  }


  //다이어트 챌린지 수정 (form, userDetails)(DB호출 2회) 호출 1, 업데이트 1
  @Transactional
  public BaseResponseDto<DietChallengeDto> updateDietChallenge(DietChallengeUpdateForm form,
                                                               UserDetailsImpl userDetails) {
    DietChallenge dietChallenge =
            dietChallengeRepository.searchDietChallengeByChallengeIdAndLoginId(
                    form.getChallengeId(), userDetails.getUsername());
    if (dietChallenge.getChallenge().getStartDate().isBefore(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.CANNOT_UPDATE_AFTER_START_CHALLENGE);
    }
    dietChallenge.update(form);
    return new BaseResponseDto<>(DietChallengeDto.from(dietChallenge),
            "다이어트 챌린지 수정을 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 코멘트 추가 (form, userDetails) (DB호출 3회) 호출 1, 저장 1, 업데이트 1
  @Transactional
  public BaseResponseDto<DietCommentDto> addDietComment(DietCommentAddForm form, UserDetailsImpl userDetails) {
    Member member = userDetails.getMember();
    DietChallenge dietChallenge = dietChallengeRepository.
            searchDietChallengeByChallengeIdAndLoginId(form.getChallengeId(), member.getMemberId());
    DietComment dietComment = DietComment.from(form, dietChallenge, member);
    dietCommentRepository.save(dietComment);
    dietChallenge.updateWeight(form.getCurrentWeight());
    return new BaseResponseDto<>(DietCommentDto.from(dietComment),
            "다이어트 댓글 추가를 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 코멘트 단건 조회 (commentId)
  public BaseResponseDto<DietCommentDto> getDietComment(Long commentId) {
    DietComment dietComment = dietCommentRepository.searchDietCommentById(commentId);
    return new BaseResponseDto<>(DietCommentDto.from(dietComment),
            "다이어트 댓글 조회를 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 코멘트 수정 (form, userDetails)
  @Transactional
  public BaseResponseDto<DietCommentDto> updateDietComment(DietCommentUpdateForm form,
                                                           UserDetailsImpl userDetails) {
    Member member = userDetails.getMember();
    DietComment dietComment = dietCommentRepository.searchDietCommentById(form.getCommentId());
    checkMemberOwnerOfComment(member, dietComment);
    dietComment.update(form);
    dietComment.getDietChallenge().updateWeight(form.getCurrentWeight());
    return new BaseResponseDto<>(DietCommentDto.from(dietComment),
            "다이어트 댓글 수정을 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 코멘트 삭제 (commentId, userDetails)
  @Transactional
  public BaseResponseDto<DietCommentDto> deleteDietComment(Long commentId,
                                                           UserDetailsImpl userDetails) {
    Member member = userDetails.getMember();
    DietComment dietComment = dietCommentRepository.searchDietCommentById(commentId);
    checkMemberOwnerOfComment(member, dietComment);
    dietCommentRepository.delete(dietComment);
    return new BaseResponseDto<>(DietCommentDto.from(dietComment),
            "다이어트 댓글 삭제를 성공했습니다.",
            HttpStatus.OK);
  }

  private void checkMemberOwnerOfComment(Member member, DietComment comment) {
    if (!member.getMemberId().equals(comment.getMember().getMemberId())) {
      throw new CustomException(ErrorCode.NOT_OWNER_OF_COMMENT);
    }
  }

}