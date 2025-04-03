package com.zerobase.challengeproject.comment.service;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.comment.domain.dto.CoteChallengeDto;
import com.zerobase.challengeproject.comment.domain.dto.CoteCommentDto;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeForm;
import com.zerobase.challengeproject.comment.domain.form.CoteChallengeUpdateForm;
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

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CoteCommentRepository coteCommentRepository;
  private final CoteChallengeRepository coteChallengeRepository;
  private final ChallengeRepository challengeRepository;
  private final MemberRepository memberRepository;


  /**
   * 날짜를 기준으로 코테 문제를 추가하는 서비스 메서드
   * 폼에 적은 날짜에 이미 CoteChallenge를 추가 했거나, Challenge를 찾을 수 없거나,
   * 추가 하려는 회원이 챌린지를 만든 회원이 아닐 때 예외발생
   *
   * @param form 챌린지 아이디, 코테 제목, 코테 문제링크, 문제가 시작되는 날짜
   * @return 코테 챌린지 정보
   */
  public BaseResponseDto<CoteChallengeDto> addCoteChallenge(
          CoteChallengeForm form,
          UserDetailsImpl userDetails) {
    Challenge challenge = challengeRepository.findById(form.getChallengeId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));
    if (!Objects.equals(challenge.getMember().getMemberId(), userDetails.getUsername())) {
      throw new CustomException(ErrorCode.NOT_OWNER_OF_CHALLENGE);
    }

    boolean isExist = coteChallengeRepository.existsByStartAt(form.getStartAt());
    if (isExist) {
      throw new CustomException(ErrorCode.ALREADY_ADDED_THAT_DATE);
    }

    CoteChallenge coteChallenge = CoteChallenge.from(form, challenge);
    coteChallengeRepository.save(coteChallenge);

    return new BaseResponseDto<CoteChallengeDto>(
            CoteChallengeDto.from(coteChallenge),
            "코테 챌린지 생성을 성공했습니다.",
            HttpStatus.OK);
  }

  /**
   * 코테 챌린지를 단건 조회하는 서비스 메서드
   * 코테 챌린지 아이디로 찾을 수 없는 경우 예외 발생
   *
   * @param coteChallengeId 코테 챌린지 아이디
   * @return 댓글을 제외한 코테 챌린지의 정보
   */
  public BaseResponseDto<CoteChallengeDto> getCoteChallenge(
          Long coteChallengeId) {

    CoteChallenge coteChallenge = coteChallengeRepository.findById(coteChallengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COTE_CHALLENGE));

    return new BaseResponseDto<CoteChallengeDto>(
            CoteChallengeDto.fromWithoutComments(coteChallenge),
            "코테 챌린지 단건 조회를 성공했습니다.",
            HttpStatus.OK);
  }

  /**
   * 코테 챌린지를 수정하기 위한 서비스 메서드
   * 내가 만든 챌린지가 아닐 때, 코테 챌린지를 찾을 수 없을 때 예외 발생
   *
   * @param form        수정할 코테 챌린지 아이디, 수정할 코테 문제, 수정할 코테 링크
   * @param userDetails 자신이 만든 챌린지 인지 확인을 위한 회원 정보
   * @return 댓글을 제외한 수정된 코테 챌린지의 정보
   */
  @Transactional
  public BaseResponseDto<CoteChallengeDto> updateCoteChallenge(
          CoteChallengeUpdateForm form,
          UserDetailsImpl userDetails) {

    CoteChallenge coteChallenge = coteChallengeRepository.findById(form.getCoteChallengeId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COTE_CHALLENGE));

    boolean isOwner = coteChallenge.getChallenge().getMember().getMemberId().equals(userDetails.getUsername());
    if (!isOwner) {
      throw new CustomException(ErrorCode.NOT_OWNER_OF_CHALLENGE);
    }
    coteChallenge.update(form);
    return new BaseResponseDto<CoteChallengeDto>(
            CoteChallengeDto.fromWithoutComments(coteChallenge),
            "코테 챌린지 수정을 성공했습니다.",
            HttpStatus.OK);
  }

  /**
   * 코테 챌린지(문제) 삭제를 위한 서비스 메서드
   * 내가 만든 챌린지가 아닐 때, 코테 챌린지를 찾을 수 없을 때,
   * 댓글이 있을 때 예외 발생
   *
   * @param coteChallengeId 코테 챌린지 아이디
   * @param userDetails 자신이 만든 챌린지 인지 확인을 위한 회원 정보
   * @return 삭제된 코테 챌린지의 정보
   */
  @Transactional
  public BaseResponseDto<CoteChallengeDto> deleteCoteChallenge(
          Long coteChallengeId,
          UserDetailsImpl userDetails) {

    CoteChallenge coteChallenge = coteChallengeRepository.findById(coteChallengeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COTE_CHALLENGE));
    boolean isOwner = coteChallenge.getChallenge().getMember().getMemberId().equals(userDetails.getUsername());
    if (!isOwner) {
      throw new CustomException(ErrorCode.NOT_OWNER_OF_CHALLENGE);
    }

    if (!coteChallenge.getComments().isEmpty()){
      throw new CustomException(ErrorCode.CANNOT_DELETE_HAVE_COMMENT);
    }

    coteChallengeRepository.delete(coteChallenge);
    return new BaseResponseDto<CoteChallengeDto>(
            CoteChallengeDto.from(coteChallenge),
            "코테 챌린지 삭제를 성공했습니다.",
            HttpStatus.OK);
  }


  /**
   * 코테 챌린지 인증 작성을 위한 서비스 메서드
   * 오늘 날짜에 이미 댓글을 썼거나, 챌린지 아이디로 챌린지를 찾을 수 없거나(아이디를 잘못썼거나, 있어야할 CoteChallenge 가 없거나),
   * 챌린지에 참여하지 않은 사람이 댓글을 쓰려고 할 때 예외발생
   *
   * @param form        챌린지 아이디, 인증하기 위한 이미지주소, 설명
   * @param userDetails username 사용
   * @return 인증 댓글 반환
   */
  public BaseResponseDto<CoteCommentDto> addComment(CoteCommentForm form, UserDetailsImpl userDetails) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime today = LocalDateTime.parse(String.format("%04d-%02d-%02dT00:00:00", now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
    CoteChallenge coteChallenge = coteChallengeRepository.searchCoteChallengeByStartAt(form.getChallengeId(), userDetails.getUsername(), today);

    Member member = memberRepository.searchByEmail(userDetails.getUsername());
    boolean isEnter = member.getMemberChallenges().stream()
            .anyMatch(challenge ->
                    challenge.getChallenge()
                            .getId()
                            .equals(form.getChallengeId()));
    if (!isEnter) {
      throw new CustomException(ErrorCode.NOT_ENTERED_CHALLENGE);
    }

    CoteComment coteComment = CoteComment.from(form, member, coteChallenge);
    coteCommentRepository.save(coteComment);
    return new BaseResponseDto<CoteCommentDto>(
            CoteCommentDto.from(coteComment),
            "댓글 추가를 성공했습니다.",
            HttpStatus.OK);
  }

  //TODO
  //인증 수정 (commentId, form)
  //인증 삭제 (commentId)
  //인증 단건 확인 (commentId)

}
