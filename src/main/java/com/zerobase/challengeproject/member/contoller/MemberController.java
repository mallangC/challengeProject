package com.zerobase.challengeproject.member.contoller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.member.domain.dto.MemberEmailAuthDto;
import com.zerobase.challengeproject.member.domain.dto.MemberSignupDto;
import com.zerobase.challengeproject.member.domain.form.MemberSignupForm;
import com.zerobase.challengeproject.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseDto<MemberSignupDto>> signUp(@Valid @RequestBody MemberSignupForm memberSignupForm) {
        MemberSignupDto memberSignupDto = memberService.signup(memberSignupForm);
        return ResponseEntity.ok(new BaseResponseDto(memberSignupDto, "회원 가입 요청 성공했습니다.", HttpStatus.OK));
    }

    @GetMapping("/email-auth")
    public ResponseEntity<BaseResponseDto<MemberEmailAuthDto>> verifyEmail(@RequestParam("id") String emailAuthKey){
        MemberEmailAuthDto isVerified = memberService.verifyEmail(emailAuthKey);
        return ResponseEntity.ok(new BaseResponseDto<>(isVerified,"이메일 인증 완료되었습니다.", HttpStatus.OK));
    }
}
