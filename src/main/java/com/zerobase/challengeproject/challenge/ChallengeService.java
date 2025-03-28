package com.zerobase.challengeproject.challenge;


import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.dto.ChallengeGetDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    // 전체 챌린지 조회
    public ResponseEntity<BaseResponseDto<List<Challenge>>> getAllChallenges() {

        List<Challenge> allChallenge = challengeRepository.findAll();

        if (allChallenge.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CHALLENGES);
        }

        BaseResponseDto<List<Challenge>> response = new BaseResponseDto<List<Challenge>>(
                allChallenge,
                "전체 챌린지 조회 성공",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    // 챌린지 상세 조회
    public ResponseEntity<BaseResponseDto<Challenge>> getChallengeDetail(Long id){

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() ->  new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        BaseResponseDto<Challenge> response = new BaseResponseDto<Challenge>(
                challenge,
                "챌린지 상세정보 조회 성공",
                HttpStatus.OK);
        return ResponseEntity.ok(response);

    }

    // 챌린지 생성
    public ResponseEntity<BaseResponseDto<Challenge>> createChallenge(ChallengeForm dto){

        Challenge challenge = Challenge.builder()
                .title(dto.getTitle())
                .img(dto.getImg())
                .description(dto.getDescription())
                .max_deposit(dto.getMax_deposit())
                .min_deposit(dto.getMin_deposit())
                .standard(dto.getStandard())
                .participant(dto.getParticipant())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createAt(LocalDateTime.now()).
                build();

        challengeRepository.save(challenge);

        BaseResponseDto<Challenge> response = new BaseResponseDto<Challenge>(
                challenge,
                "챌린지 생성 성공",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }


    
    // 챌린지수정
    public ResponseEntity<BaseResponseDto<Challenge>> updateChallenge(ChallengeForm dto) {

        // 데이터베이스 같은 아이디의 챌린지 가져오기
        Challenge challenge = challengeRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        challenge.update(dto);
        challengeRepository.save(challenge);

        BaseResponseDto<Challenge> response = new BaseResponseDto<Challenge>(
                challenge,
                "챌린지 수정 성공",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    // 챌린지삭제
    public ResponseEntity<BaseResponseDto<Challenge>> deleteChallenge(Long id){

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHALLENGE));

        challengeRepository.delete(challenge);

        BaseResponseDto<Challenge> response = new BaseResponseDto<Challenge>(
                null,
                "챌린지 삭제 성공",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
}
