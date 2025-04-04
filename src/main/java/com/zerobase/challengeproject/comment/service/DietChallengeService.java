package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.comment.domain.dto.DietChallengeDto;
import com.zerobase.challengeproject.comment.domain.form.DietChallengeAddForm;
import com.zerobase.challengeproject.comment.domain.form.DietChallengeUpdateForm;
import com.zerobase.challengeproject.comment.entity.DietChallenge;
import com.zerobase.challengeproject.comment.entity.DietComment;
import com.zerobase.challengeproject.comment.repository.DietChallengeRepository;
import com.zerobase.challengeproject.comment.repository.DietCommentRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import com.zerobase.challengeproject.type.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DietChallengeService {
  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;
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

    return new BaseResponseDto<DietChallengeDto>(DietChallengeDto.from(dietChallenge),
            "다이어트 챌린지 추가를 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 챌린지 단건 조회 (challengeId, userDetails)(DB호출 1회) 호출 1
  public BaseResponseDto<DietChallengeDto> getDietChallenge(Long challengeId,
                                                            UserDetailsImpl userDetails) {
    DietChallenge dietChallenge =
            dietChallengeRepository.searchDietChallengeByChallengeIdAndMemberId(
                    challengeId, userDetails.getUsername());
    return new BaseResponseDto<DietChallengeDto>(DietChallengeDto.from(dietChallenge),
            "다이어트 챌린지 단건 조회를 성공했습니다.",
            HttpStatus.OK);
  }

  //다이어트 챌린지 수정 (form, userDetails)
  @Transactional
  public BaseResponseDto<DietChallengeDto> updateDietChallenge(DietChallengeUpdateForm form,
                                                               UserDetailsImpl userDetails) {
    DietChallenge dietChallenge =
            dietChallengeRepository.searchDietChallengeByChallengeIdAndMemberId(
                    form.getChallengeId(), userDetails.getUsername());
    if (dietChallenge.getChallenge().getStartDate().isBefore(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.CANNOT_UPDATE_AFTER_START_CHALLENGE);
    }
    dietChallenge.update(form);
    return new BaseResponseDto<DietChallengeDto>(DietChallengeDto.from(dietChallenge),
            "다이어트 챌린지 수정을 성공했습니다.",
            HttpStatus.OK);
  }


}
