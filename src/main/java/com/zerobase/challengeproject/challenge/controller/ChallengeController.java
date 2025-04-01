package com.zerobase.challengeproject.challenge.controller;


import com.zerobase.challengeproject.challenge.ChallengeService;
import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.dto.GetChallengeDto;
import com.zerobase.challengeproject.challenge.domain.dto.OngoingChallengeDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
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
     * @param challengeId 게시물번호
     */
    @GetMapping("/{challengeId}")
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> getChallengeDetail(@PathVariable Long challengeId){

        return challengeService.getChallengeDetail(challengeId);
    }

    /**
     * 사용자가 생성한 챌린지 조회
     * @param memberId 유저아이디
     */
    @GetMapping("/my-challenge/{memberId}")
    public ResponseEntity<BaseResponseDto<Page<GetChallengeDto>>> getChallengesMadeByUser(@PathVariable Long memberId, Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.getChallengesMadeByUser(memberId, pageable, userDetails);
    }

    /**
     * 사용자가 참여중인 챌린지 조회
     * @param memberId 유저아이디
     */
    @GetMapping("/on-going/{memberId}")
    public ResponseEntity<BaseResponseDto<Page<OngoingChallengeDto>>> ongoingChallenges(@PathVariable Long memberId, Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.getOngoingChallenges(memberId, pageable, userDetails);
    }

    /**
     * 챌린지 생성
     * @param dto 클라이언트가 서버에 보낸 정보
     */
    @PostMapping
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> createChallenge(@Valid @RequestBody ChallengeForm dto, UserDetailsImpl userDetails) {

        return challengeService.createChallenge(dto, userDetails);
    }

    /**
     * 챌린지 수정
     * @param challengeId 게시물번호
     */
    @PutMapping("/{challengeId}")
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody ChallengeForm dto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.updateChallenge(challengeId, dto, userDetails);
    }

    /**
     * 챌린지 삭제
     * @param challengeId 게시물번호
     */
    @DeleteMapping("/{challengeId}")
    public ResponseEntity<BaseResponseDto<GetChallengeDto>> deleteChallenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return challengeService.deleteChallenge(challengeId, userDetails);
    }

}
