package com.zerobase.challengeproject.member.contoller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.domain.dto.MemberProfileDto;
import com.zerobase.challengeproject.member.domain.form.ChangePasswordForm;
import com.zerobase.challengeproject.member.domain.form.MemberProfileFrom;
import com.zerobase.challengeproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 유저가 회원 정보를 조회할 때 사용되는 컨트롤러 메서드
     * @param userDetails 로그인한 유저의 정보
     * @return 유저 정보(로그인아이디, 이름, 닉네임, 전화번호, 이메일주소)
     */
    @GetMapping("/profile")
    public ResponseEntity<BaseResponseDto<MemberProfileDto>> getProfile (
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(
                new BaseResponseDto<>(
                        memberService.getProfile(userDetails),
                        "회원 정보 불러오기를 성공했습니다",
                        HttpStatus.OK
                        ));
    }

    @PatchMapping("/profile")
    public ResponseEntity<BaseResponseDto<MemberProfileDto>> updateProfile (
            @RequestBody MemberProfileFrom form,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseEntity.ok(
                new BaseResponseDto<>(
                        memberService.updateProfile(userDetails, form),
                        "회원 정보 수정 성공했습니다",
                        HttpStatus.OK
                )
        );
    }

    @PatchMapping("/change-password")
    public ResponseEntity<BaseResponseDto<String>> changePassword (
            @RequestBody ChangePasswordForm form,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(
                new BaseResponseDto<>(
                        memberService.changePassword(userDetails, form),
                        "비밀 번호 수정 성공했습니다",
                        HttpStatus.OK
                )
        );
    }
}
