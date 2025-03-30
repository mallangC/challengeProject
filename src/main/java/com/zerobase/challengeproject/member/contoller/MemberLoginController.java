package com.zerobase.challengeproject.member.contoller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginDto;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginResponse;
import com.zerobase.challengeproject.member.domain.form.MemberLoginForm;
import com.zerobase.challengeproject.member.service.MemberLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<MemberLoginDto>> login(@RequestBody MemberLoginForm form) {
        MemberLoginResponse response = memberLoginService.login(form);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + response.getToken())
                .body(new BaseResponseDto<>(new MemberLoginDto(response.getMemberId()),"로그인에 성공했습니다", HttpStatus.OK));
    }
}
