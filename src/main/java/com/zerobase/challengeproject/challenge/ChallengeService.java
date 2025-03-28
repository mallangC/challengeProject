package com.zerobase.challengeproject.challenge;


import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    /**
     * 전체 챌린지조회
     */
    public ResponseEntity<BaseResponseDto<Page<Challenge>>> getAllChallenges(Pageable pageable) {

        Page<Challenge> allChallenge = challengeRepository.findAll(pageable);
        if (allChallenge.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CHALLENGES);
        }

        return ResponseEntity.ok(new BaseResponseDto<Page<Challenge>>(allChallenge, "전체 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     *  특정챌린지 상세 조회
      */
    public ResponseEntity<BaseResponseDto<Challenge>> getChallengeDetail(Long id){

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() ->  new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        return ResponseEntity.ok(new BaseResponseDto<Challenge>(challenge,"챌린지 상제정보 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 만든 챌린지 조회
     * @param id 사용자아이디
     */
    public ResponseEntity<BaseResponseDto<Page<Challenge>>> getChallengesMadeByUser(Long id, Pageable pageable){
        Page<Challenge> userChallenges = challengeRepository.findById(id, pageable);
        if (userChallenges.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CHALLENGES);
        }
        return ResponseEntity.ok(new BaseResponseDto<Page<Challenge>>(userChallenges, "유저가 생성한 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 사용자가 참여중인 챌린지 조회
     * @param id 사용자아이디
     */
    public ResponseEntity<BaseResponseDto<Page<Challenge>>> getOngoingChallenges(Long id) {

        return ResponseEntity.ok(new BaseResponseDto<Page<Challenge>>(null, "유저가 참여중인 챌린지 조회 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 생성
     * @param dto 클라이언트가 서버에 보낸 데이터
     */
    public ResponseEntity<BaseResponseDto<Challenge>> createChallenge(ChallengeForm dto){

        Challenge challenge = new Challenge(dto);
        challengeRepository.save(challenge);

        return ResponseEntity.ok(new BaseResponseDto<Challenge>(challenge, "챌린지 생성 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 수정
     * @param id 챌린지번호
     */
    public ResponseEntity<BaseResponseDto<Challenge>> updateChallenge(Long id, ChallengeForm dto) {

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        challenge.update(dto);
        challengeRepository.save(challenge);

        return ResponseEntity.ok(new BaseResponseDto<Challenge>(challenge, "챌린지 수정 성공", HttpStatus.OK));
    }

    /**
     * 챌린지 삭제
     * @param id 챌린지번호
     */
    public ResponseEntity<BaseResponseDto<Challenge>> deleteChallenge(Long id){

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));
        challengeRepository.delete(challenge);

        return ResponseEntity.ok(new BaseResponseDto<Challenge>(null, "챌린지 삭제 성공", HttpStatus.OK));
    }


}
