package com.zerobase.challengeproject.challenge.controller;


import com.zerobase.challengeproject.challenge.ChallengeService;
import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
@AllArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * 챌린지 전체 조회
     */
    @GetMapping
    public ResponseEntity<BaseResponseDto<Page<Challenge>>> getAllChallenge(Pageable pageable) {
        
        return challengeService.getAllChallenges(pageable);
    }

    /**
     * 챌린지 상세조회
     * @param id 게시물번호
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Challenge>> getChallengeDetail(@PathVariable Long id){

        return challengeService.getChallengeDetail(id);
    }

    /**
     * 사용자가 생성한 챌린지 조회
     * @param id 유저아이디
     */
    @GetMapping("/my-challenge/{id}")
    public ResponseEntity<BaseResponseDto<Page<Challenge>>> getChallengesMadeByUser(@PathVariable Long id, Pageable pageable){

        return challengeService.getChallengesMadeByUser(id, pageable);
    }

    /**
     * 사용자가 참여중인 챌린지 조회
     * @param id 유저아이디
     */
    @GetMapping("/on-going/{id}")
    public ResponseEntity<BaseResponseDto<Page<Challenge>>> ongoingChallenges(@PathVariable Long id, Pageable pageable){

        return challengeService.getOngoingChallenges(id, pageable);
    }

    /**
     * 챌린지 생성
     * @param dto 클라이언트가 서버에 보낸 정보
     */
    @PostMapping
    public ResponseEntity<BaseResponseDto<Challenge>> createChallenge(@Valid @RequestBody ChallengeForm dto, UserDetailsImpl userDetails) {

        return challengeService.createChallenge(dto, userDetails);
    }

    /**
     * 챌린지 수정
     * @param id 게시물번호
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Challenge>> updateChallenge(@PathVariable Long id, @Valid @RequestBody ChallengeForm dto){

        return challengeService.updateChallenge(id, dto);
    }

    /**
     * 챌린지 삭제
     * @param id 게시물번호
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Challenge>> deleteChallenge(@PathVariable Long id){

        return challengeService.deleteChallenge(id);
    }

}
