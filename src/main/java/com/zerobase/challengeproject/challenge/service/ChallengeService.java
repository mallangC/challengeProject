package com.zerobase.challengeproject.challenge.service;


import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.dto.EnterChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.GetChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.OngoingChallengeDto;
import com.zerobase.challengeproject.challenge.domain.form.CreateChallengeForm;
import com.zerobase.challengeproject.challenge.domain.form.EnterChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.entity.MemberChallenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.challenge.repository.MemberChallengeRepository;
import com.zerobase.challengeproject.comment.entity.CoteChallenge;
import com.zerobase.challengeproject.comment.repository.CoteChallengeRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import com.zerobase.challengeproject.type.Category;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final CoteChallengeRepository coteChallengeRepository;


    /**
     * 전체 챌린지조회
     */
    public ResponseEntity<BaseResponseDto<Page<GetChallengeDto>>> getAllChallenges(Pageable pageable) {

        Page<Challenge> allChallenges = challengeRepository.findAll(pageable);

        if (allChallenges.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CHALLENGES);
        }
        Page<GetChallengeDto> challengeDtos = allChallenges.map(allChallenge -> new GetChallengeDto(allChallenge));

        return ResponseEntity.ok(new BaseResponseDto<Page<GetChallengeDto>>(challengeDtos, "전체 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     *  특정챌린지 상세 조회
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> getChallengeDetail(@PathVariable Long challengeId){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() ->  new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto,"챌린지 상제정보 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 만든 챌린지 조회
     */
    public ResponseEntity<BaseResponseDto<Page<GetChallengeDto>>> getChallengesMadeByUser(Pageable pageable, UserDetailsImpl userDetails){

        Long memberId = userDetails.getMember().getId();
        Page<Challenge> userChallenges = challengeRepository.findByMemberId(memberId, pageable);

        Page<GetChallengeDto> challengeDtos = userChallenges.map(userChallenge -> new GetChallengeDto(userChallenge));

        if (challengeDtos.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CHALLENGES);
        }
        return ResponseEntity.ok(new BaseResponseDto<Page<GetChallengeDto>>(challengeDtos, "유저가 생성한 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 참여중인 챌린지 조회
     */
    public ResponseEntity<BaseResponseDto<Page<OngoingChallengeDto>>> getOngoingChallenges(Pageable pageable, UserDetailsImpl userDetails) {

        /** 로그인시에만 가능
         */
        Long memberId = userDetails.getMember().getId();
        Page<MemberChallenge> memberChallenges = memberChallengeRepository.findByMemberId(memberId, pageable);

        Page<OngoingChallengeDto> challengeDtos = memberChallenges.map(memberChallenge -> new OngoingChallengeDto(memberChallenge.getChallenge()));
        return ResponseEntity.ok(new BaseResponseDto<Page<OngoingChallengeDto>>(challengeDtos, "유저가 참여중인 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 챌린지에 참여
     */
    public ResponseEntity<BaseResponseDto<EnterChallengeDto>> enterChallenge(Long challengeId, EnterChallengeForm enterChallengeForm, UserDetailsImpl userDetails){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));


        Member member = userDetails.getMember();

        MemberChallenge memberChallenge = MemberChallenge.builder()
                .entered_at(LocalDateTime.now())
                .challenge(challenge)
                .memberDeposit(enterChallengeForm.getMyDeposit())
                .member(member)
                .build();

        EnterChallengeDto enterChallengeDto = new EnterChallengeDto(challenge, enterChallengeForm.getMyDeposit());
        memberChallengeRepository.save(memberChallenge);
        return ResponseEntity.ok(new BaseResponseDto<EnterChallengeDto>(enterChallengeDto,"챌린지 참여에 성공했습니다.", HttpStatus.OK));
    }

    /**
     * 챌린지 생성
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> createChallenge(@Valid @RequestBody CreateChallengeForm form,
                                                                      UserDetailsImpl userDetails){

        if (form.getMin_deposit() > form.getMax_deposit()) {
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (form.getParticipant() <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_NUMBER);
        }
        if (form.getStartDate().isAfter(form.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        Member member = userDetails.getMember();
        Challenge challenge = new Challenge(form, member);
        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        challengeRepository.save(challenge);
        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto, "챌린지 생성 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 수정
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody CreateChallengeForm form, UserDetailsImpl userDetails) {

        if (form.getMin_deposit() > form.getMax_deposit()) {
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (form.getParticipant() <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_NUMBER);
        }
        if (form.getStartDate().isAfter(form.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        /**
         * 로그인 되어있고 챌린지를 생성한 유저만 수정이 가능하다.

        if(!challenge.getMember().getId().equals(userDetails.getMember().getId())){
            throw new CustomException(ErrorCode.FORBIDDEN_UPDATE_CHALLENGE);
        }
        */
        challenge.update(form);
        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        challengeRepository.save(challenge);

        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto, "챌린지 수정 성공", HttpStatus.OK));
    }


    /**
     * 챌린지 삭제
     */

    public ResponseEntity<BaseResponseDto<GetChallengeDto>> deleteChallenge(@PathVariable Long challengeId, UserDetailsImpl userDetails){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        /**
         * 로그인 되어있고 챌린지를 생성한 유저만 삭제가 가능하다.

        if (!challenge.getMember().getId().equals(userDetails.getMember().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_DELETE_CHALLENGE);
        }
         */
        challengeRepository.delete(challenge);

        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(null, "챌린지 삭제 성공", HttpStatus.OK));
    }

}
