package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.member.components.jwt.JwtUtil;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginDto;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginResponse;
import com.zerobase.challengeproject.member.domain.form.MemberLoginForm;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberLoginServiceTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberLoginService memberLoginService;

    @Test
    @DisplayName("로그인 성공")
    void login() {
        Member mockMember = Member.builder()
                .id(1L)
                .memberId("testId")
                .memberName("testName")
                .nickname("testNickname")
                .email("testEmail@email.com")
                .phoneNum("01011112222")
                .password("testPassword1!")
                .build();
        MemberLoginForm testForm = MemberLoginForm.builder()
                .memberId("testId")
                .password("testPassword1!")
                .build();
        when(memberRepository.findByMemberId(testForm.getMemberId())).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(testForm.getPassword(), mockMember.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(testForm.getMemberId())).thenReturn("mockToken");
        //when
        MemberLoginResponse result = memberLoginService.login(testForm);
        assertNotNull(result);
        assertEquals("mockToken", result.getToken());
        assertEquals(mockMember.getMemberId(), result.getMemberId());
    }
    @Test
    @DisplayName("로그인 실패")
    void loginFailure() {
        Member mockMember = Member.builder()
                .id(1L)
                .memberId("testId")
                .memberName("testName")
                .nickname("testNickname")
                .email("testEmail@email.com")
                .phoneNum("01011112222")
                .password("testPassword1!")
                .build();
        MemberLoginForm testForm = MemberLoginForm.builder()
                .memberId("testId")
                .password("WrongPassword1!")
                .build();
        when(memberRepository.findByMemberId(testForm.getMemberId())).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(testForm.getPassword(), mockMember.getPassword())).thenReturn(false);
        //when & then
        assertThrows(CustomException.class, () -> memberLoginService.login(testForm));
    }

}