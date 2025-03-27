package com.zerobase.challengeproject.member.contoller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.member.domain.dto.MemberSignupDto;
import com.zerobase.challengeproject.member.domain.form.MemberSignupForm;
import com.zerobase.challengeproject.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseDto<MemberSignupDto>> signUp(@Valid @RequestBody MemberSignupForm memberSignupForm) {
        MemberSignupDto memberSignupDto = memberService.signup(memberSignupForm);
        return ResponseEntity.ok(new BaseResponseDto(memberSignupDto, "회원 가입 요청 성공했습니다.", HttpStatus.OK));
    }
}
