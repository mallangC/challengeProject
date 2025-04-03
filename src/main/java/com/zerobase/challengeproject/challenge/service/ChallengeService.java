package com.zerobase.challengeproject.challenge.service;


import com.zerobase.challengeproject.account.entity.AccountDetail;
import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.dto.EnterChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.GetChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.ParticipationChallengeDto;
import com.zerobase.challengeproject.challenge.domain.form.CreateChallengeForm;
import com.zerobase.challengeproject.challenge.domain.form.RegistrationChallengeForm;
import com.zerobase.challengeproject.challenge.domain.form.UpdateChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.entity.MemberChallenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.challenge.repository.MemberChallengeRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.entity.Member;
import jakarta.transaction.Transactional;
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
    public ResponseEntity<BaseResponseDto<Page<ParticipationChallengeDto>>> getOngoingChallenges(Pageable pageable, UserDetailsImpl userDetails) {

        /** 로그인시에만 가능
         */
        Long memberId = userDetails.getMember().getId();
        Page<MemberChallenge> memberChallenges = memberChallengeRepository.findByMemberId(memberId, pageable);

        Page<ParticipationChallengeDto> challengeDtos = memberChallenges.map(memberChallenge -> new ParticipationChallengeDto(memberChallenge.getChallenge()));
        return ResponseEntity.ok(new BaseResponseDto<Page<ParticipationChallengeDto>>(challengeDtos, "유저가 참여중인 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 챌린지에 참여
     */
    public ResponseEntity<BaseResponseDto<EnterChallengeDto>> enterChallenge(Long challengeId, RegistrationChallengeForm form, UserDetailsImpl userDetails){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        Member member = userDetails.getMember();

        if (member.getAccount() <= form.getMemberDeposit()){
            throw new CustomException(ErrorCode.INSUFFICIENT_DEPOSIT);
        }
        if (form.getMemberDeposit() < challenge.getMinDeposit()){
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (LocalDateTime.now().isAfter(challenge.getEndDate())){
            throw new CustomException(ErrorCode.CHALLENGE_ALREADY_ENDED);
        }

        MemberChallenge memberChallenge = MemberChallenge.builder()
                .entered_at(LocalDateTime.now())
                .challenge(challenge)
                .memberDeposit(form.getMemberDeposit())
                .member(member)
                .build();

        member.setAccount(member.getAccount() - form.getMemberDeposit());
        /**
         * 이미 참여한 챌린지인 경우 예외발생
         */
        if(member.getId().equals(memberChallenge.getMember().getId())){
            throw new CustomException(ErrorCode.ALREADY_ENTERED_CHALLENGE);
        }

        EnterChallengeDto enterChallengeDto = new EnterChallengeDto(challenge, form.getMemberDeposit());
        memberChallengeRepository.save(memberChallenge);
        return ResponseEntity.ok(new BaseResponseDto<EnterChallengeDto>(enterChallengeDto,"챌린지 참여에 성공했습니다.", HttpStatus.OK));
    }

    /**
     * 챌린지 생성
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> createChallenge(@RequestBody CreateChallengeForm form,
                                                                      UserDetailsImpl userDetails){

        if (form.getMinDeposit() > form.getMaxDeposit()) {
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (form.getParticipant() <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_NUMBER);
        }
        if (form.getStartDate().isAfter(form.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }
        if (form.getMinDeposit() > form.getMemberDeposit()){
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }

        Member member = userDetails.getMember();
        Challenge challenge = new Challenge(form, member);

        if(member.getAccount() <= form.getMemberDeposit()){
            throw new CustomException(ErrorCode.INSUFFICIENT_DEPOSIT);
        }

        /**
         * 생성한 사람은 바로 참여
         */
        MemberChallenge memberChallenge = MemberChallenge.builder()
                .challenge(challenge)
                .memberDeposit(form.getMemberDeposit())
                .entered_at(LocalDateTime.now())
                .member(member)
                .build();

        member.setAccount(member.getAccount() - form.getMemberDeposit());

        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        challengeRepository.save(challenge);
        memberChallengeRepository.save(memberChallenge);

        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto, "챌린지 생성 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 수정
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> updateChallenge(@PathVariable Long challengeId, @RequestBody UpdateChallengeForm form, UserDetailsImpl userDetails) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        if (!challenge.getMember().getId().equals(userDetails.getMember().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_UPDATE_CHALLENGE);
        }

        /**
         *  예외 검증 (입력된 값이 있을 때만)
         */
        if (form.getMinDeposit() != null && form.getMaxDeposit() != null
                && form.getMinDeposit() > form.getMaxDeposit()) {
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (form.getParticipant() != null && form.getParticipant() <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_NUMBER);
        }
        if (form.getStartDate() != null && form.getEndDate() != null
                && form.getStartDate().isAfter(form.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

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

        if (!challenge.getMember().getId().equals(userDetails.getMember().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_DELETE_CHALLENGE);
        }

        challengeRepository.delete(challenge);

        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(null, "챌린지 삭제 성공", HttpStatus.OK));
    }

}
