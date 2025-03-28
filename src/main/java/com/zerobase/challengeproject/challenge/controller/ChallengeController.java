package com.zerobase.challengeproject.challenge.controller;


import com.zerobase.challengeproject.challenge.ChallengeService;
import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    // 챌린지 조회
    @GetMapping("/challenge")
    public ResponseEntity<BaseResponseDto<List<Challenge>>> getAllChallenge() {
        
        return challengeService.getAllChallenges();
    }
    
    // 챌린지 상세조회
    @GetMapping("/challenge/{id}")
    public ResponseEntity<BaseResponseDto<Challenge>> getChallengeDetail(@PathVariable Long id){

        return challengeService.getChallengeDetail(id);
    }
    
    
    // 챌린지 생성
    @PostMapping("/challenge")
    public ResponseEntity<BaseResponseDto<Challenge>> createChallenge(@Valid @RequestBody ChallengeForm dto) {

        return challengeService.createChallenge(dto);
    }

    
    // 챌린지 수정
    @PostMapping("/challenge")
    public ResponseEntity<BaseResponseDto<Challenge>> updateChallenge(@Valid @RequestBody ChallengeForm dto){

        return challengeService.updateChallenge(dto);
    }

    // 챌린지 삭제
    @DeleteMapping("/challenge")
    public ResponseEntity<BaseResponseDto<Challenge>> deleteChallenge(@PathVariable Long id){


        return challengeService.deleteChallenge(id);
    }



}
