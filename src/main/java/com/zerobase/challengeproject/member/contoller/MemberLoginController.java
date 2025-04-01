package com.zerobase.challengeproject.member.contoller;

import com.zerobase.challengeproject.BaseResponseDto;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginDto;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginResponse;
import com.zerobase.challengeproject.member.domain.dto.MemberLogoutDto;
import com.zerobase.challengeproject.member.domain.dto.RefreshTokenDto;
import com.zerobase.challengeproject.member.domain.form.MemberLoginForm;
import com.zerobase.challengeproject.member.service.MemberLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    /**
     * 유저가 로그인을 시도할 때 사용하는 컨트롤러 메서드
     * @param form 유저 아이디, 비밀번호
     * @return 유저아이디, 성공 메세지, HttpCode
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<MemberLoginDto>> login(@RequestBody MemberLoginForm form) {
        MemberLoginResponse response = memberLoginService.login(form);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie().toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccessToken())
                .body(new BaseResponseDto<>(new MemberLoginDto(response.getMemberId()),"로그인에 성공했습니다", HttpStatus.OK));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponseDto> logout(@RequestHeader("Authorization") String token,
                                                  @CookieValue(value = "refreshToken", required = false)
                                                  String refreshToken) {

        MemberLogoutDto dto = memberLoginService.logout(token,refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, dto.getResponseCookie().toString())
                .body(new BaseResponseDto<>(dto.getMemberId(), "로그아웃 성공했습니다.", HttpStatus.OK));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<BaseResponseDto> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken  ) {
        RefreshTokenDto dto = memberLoginService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + dto.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, dto.getRefreshToken())
                .body(new BaseResponseDto<>(null, "토큰이 재 발행되었습니다", HttpStatus.OK));
    }
}
