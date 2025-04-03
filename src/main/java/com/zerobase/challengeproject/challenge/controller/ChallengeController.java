package com.zerobase.challengeproject.challenge.controller;


import com.zerobase.challengeproject.account.service.AccountService;
import com.zerobase.challengeproject.challenge.domain.dto.EnterChallengeDto;
import com.zerobase.challengeproject.challenge.domain.form.RegistrationChallengeForm;
import com.zerobase.challengeproject.challenge.domain.form.UpdateChallengeForm;
import com.zerobase.challengeproject.challenge.service.ChallengeService;
import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.dto.GetChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.ParticipationChallengeDto;
import com.zerobase.challengeproject.challenge.domain.form.CreateChallengeForm;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenge")
@AllArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;


    /**
     * 챌린지 전체 조회
     */
    @GetMapping
    public ResponseEntity<BaseResponseDto<Page<GetChallengeDto>>> getAllChallenge(Pageable pageable) {
        
        return challengeService.getAllChallenges(pageable);
    }

    /**
     * 챌린지 상세조회
     */
    @GetMapping("/{challengeId}")
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> getChallengeDetail(@PathVariable Long challengeId){

        return challengeService.getChallengeDetail(challengeId);
    }

    /**
     * 사용자가 생성한 챌린지 조회
     */
    @GetMapping("/my-challenge")
    public ResponseEntity<BaseResponseDto<Page<GetChallengeDto>>> getChallengesMadeByUser(Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.getChallengesMadeByUser(pageable, userDetails);
    }

    /**
     * 사용자가 참여중인 챌린지 조회
     */
    @GetMapping("/participation")
    public ResponseEntity<BaseResponseDto<Page<ParticipationChallengeDto>>> ongoingChallenges(Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.getOngoingChallenges(pageable, userDetails);
    }

    /**
     * 사용자가 챌린지에 참여
     */
    @PostMapping("/registrations/{challengeId}")
    public ResponseEntity<BaseResponseDto<EnterChallengeDto>> enterChallenge(@PathVariable Long challengeId, @Valid @RequestBody RegistrationChallengeForm registrationChallengeForm, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.enterChallenge(challengeId, registrationChallengeForm, userDetails);
    }

    /**
     * 챌린지 생성
     */
    @PostMapping
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> createChallenge(@Valid @RequestBody CreateChallengeForm form, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return challengeService.createChallenge(form, userDetails);
    }

    /**
     * 챌린지 수정
     */
    @PutMapping("/{challengeId}")
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody UpdateChallengeForm form, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.updateChallenge(challengeId, form, userDetails);
    }

    /**
     * 챌린지 삭제
     */
    @DeleteMapping("/{challengeId}")
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> deleteChallenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.deleteChallenge(challengeId, userDetails);
    }

}
