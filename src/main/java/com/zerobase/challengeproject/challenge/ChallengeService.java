package com.zerobase.challengeproject.challenge;


import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.dto.GetChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.OngoingChallengeDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.entity.MemberChallenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.challenge.repository.MemberChallengeRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberRepository memberRepository;


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
     * @param challengeId 챌린지 아이디
      */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> getChallengeDetail(@PathVariable Long challengeId){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() ->  new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto,"챌린지 상제정보 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 만든 챌린지 조회
     * @param memberId 사용자아이디
     */
    public ResponseEntity<BaseResponseDto<Page<GetChallengeDto>>> getChallengesMadeByUser(@PathVariable Long memberId, Pageable pageable, UserDetailsImpl userDetails){

        /** 로그인시에만 가능
        if(!memberId.equals(userDetails.getMember().getId())){

            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }
         */

        Page<Challenge> userChallenges = challengeRepository.findByMemberId(memberId, pageable);

        Page<GetChallengeDto> challengeDtos = userChallenges.map(userChallenge -> new GetChallengeDto(userChallenge));

        if (challengeDtos.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CHALLENGES);
        }
        return ResponseEntity.ok(new BaseResponseDto<Page<GetChallengeDto>>(challengeDtos, "유저가 생성한 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 참여중인 챌린지 조회
     * @param memberId 사용자아이디
     */
    public ResponseEntity<BaseResponseDto<Page<OngoingChallengeDto>>> getOngoingChallenges(@PathVariable Long memberId, Pageable pageable, UserDetailsImpl userDetails) {
        
        
        /** 로그인시에만 가능
        if(!memberId.equals(userDetails.getMember().getId())){
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }
        
         */

        Page<MemberChallenge> memberChallenges = memberChallengeRepository.findByMemberId(memberId, pageable);

        Page<OngoingChallengeDto> challengeDtos = memberChallenges.map(memberChallenge -> new OngoingChallengeDto(memberChallenge.getChallenge()));
        return ResponseEntity.ok(new BaseResponseDto<Page<OngoingChallengeDto>>(challengeDtos, "유저가 참여중인 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 생성
     * @param dto 클라이언트가 서버에 보낸 데이터
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> createChallenge(@Valid @RequestBody ChallengeForm dto,
                                                                      UserDetailsImpl userDetails){

        if (dto.getMin_deposit() > dto.getMax_deposit()) {
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (dto.getParticipant() <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_NUMBER);
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_DATE_RANGE);
        }

        Member member = memberRepository.findById(dto.getMemberId())
               .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        /**
         * 생성시 클라이언트가 보낸 멤버 정보로 챌린지 생성후 챌린지와 멤버엔티티 매핑
         */
        Challenge challenge = new Challenge(dto, member);
        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        challengeRepository.save(challenge);

        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto, "챌린지 생성 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 수정
     * @param challengeId 챌린지번호
     */
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody ChallengeForm dto, UserDetailsImpl userDetails) {

        if (dto.getMin_deposit() > dto.getMax_deposit()) {
            throw new CustomException(ErrorCode.INVALID_DEPOSIT_AMOUNT);
        }
        if (dto.getParticipant() <= 0) {
            throw new CustomException(ErrorCode.INVALID_PARTICIPANT_NUMBER);
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
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
        challenge.update(dto);
        GetChallengeDto challengeDto = new GetChallengeDto(challenge);
        challengeRepository.save(challenge);

        return ResponseEntity.ok(new BaseResponseDto<GetChallengeDto>(challengeDto, "챌린지 수정 성공", HttpStatus.OK));
    }


    /**
     * 챌린지 삭제
     * @param challengeId 챌린지번호
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
