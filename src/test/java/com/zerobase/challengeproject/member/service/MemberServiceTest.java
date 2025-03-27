package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.MailComponents;
import com.zerobase.challengeproject.member.domain.dto.MemberEmailAuthDto;
import com.zerobase.challengeproject.member.domain.dto.MemberSignupDto;
import com.zerobase.challengeproject.member.domain.form.MemberSignupForm;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailComponents mailComponents;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 성공")
    void signup() {
        //given
        MemberSignupForm memberSignupForm = MemberSignupForm.builder()
                .memberId("testId")
                .memberName("testName")
                .nickname("testNickname")
                .email("testEmail@email.com")
                .phoneNum("01011112222")
                .password("testtest1!")
                .confirmPassword("testtest1!")
                .build();

        when(memberRepository.existsByEmail(memberSignupForm.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(memberSignupForm.getPassword())).thenReturn("encodePassword!1");

        //when
        MemberSignupDto dto = memberService.signup(memberSignupForm);

        //then
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(mailComponents, times(1)).send(eq(memberSignupForm.getEmail()), anyString(), anyString());
        assertNotNull(dto);
        assertEquals(dto.getMemberId(), memberSignupForm.getMemberId());
        assertEquals(dto.getMemberName(), memberSignupForm.getMemberName());
        assertEquals(dto.getEmail(), memberSignupForm.getEmail());
        assertEquals(dto.getPhoneNum(), memberSignupForm.getPhoneNum());
    }

    @Test
    @DisplayName("회원 가입 실패 - 비밀번호와 확인 비빌번호 불일치")
    void signupFailure() {
        // given
        MemberSignupForm memberSignupForm = MemberSignupForm.builder()
                .memberId("testId")
                .memberName("testName")
                .nickname("testNickname")
                .email("testEmail@email.com")
                .phoneNum("01011112222")
                .password("testtest1!")
                .confirmPassword("differentPassword!")
                .build();

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.signup(memberSignupForm);
        });
        assertEquals(ErrorCode.CONFIRM_PASSWORD_MISMATCH, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 인증 성공")
    void verifyEmail() {
        // given
        String emailAuthKey = "valid-key";
        Member member = Member.builder()
                .emailAuthYn(false)
                .emailAuthKey(emailAuthKey)
                .build();
        when(memberRepository.findByEmailAuthKey(emailAuthKey)).thenReturn(Optional.of(member));

        // when
        MemberEmailAuthDto authDto = memberService.verifyEmail(emailAuthKey);

        // then
        assertTrue(member.isEmailAuthYn());
        assertNotNull(authDto);
    }

    @Test
    @DisplayName("이메일 인증 실패 - 이미 인증된 메일 ")
    void verifyEmailFailure1() {
        // given
        String emailAuthKey = "verified-key";
        Member member = Member.builder()
                .emailAuthYn(true)
                .emailAuthKey(emailAuthKey)
                .build();
        when(memberRepository.findByEmailAuthKey(emailAuthKey)).thenReturn(Optional.of(member));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.verifyEmail(emailAuthKey);
        });
        assertEquals(ErrorCode.ALREADY_VERIFY_EMAIL, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 인증 실패 - 유저를 찾을 수 없음")
    void verifyEmailFailure2() {
        // given
        String emailAuthKey = "nonexistent-key";
        when(memberRepository.findByEmailAuthKey(emailAuthKey)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.verifyEmail(emailAuthKey);
        });
        assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
    }
}