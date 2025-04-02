package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.domain.dto.MemberProfileDto;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getProfile() {
        //given
        Member member = Member.builder()
                .memberId("testId")
                .memberName("testName")
                .nickname("testNickname")
                .email("testEmail@email.com")
                .phoneNum("01011112222")
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        given(memberRepository.findByMemberId(userDetails.getUsername())).willReturn(Optional.of(member));
        //when
        MemberProfileDto memberProfileDto = memberService.getProfile(userDetails);
        //then
        assertNotNull(memberProfileDto);
        assertEquals(member.getMemberId(), memberProfileDto.getMemberId());
        assertEquals(member.getMemberName(), memberProfileDto.getMemberName());
    }

    @Test
    @DisplayName("회원 정보 조회 실패 - 존재하지 않는 유저")
    void getProfileFailure() {
        //given
        Member NotFoundMember = Member.builder()
                .memberId("testId")
                .memberName("testName")
                .nickname("testNickname")
                .email("testEmail@email.com")
                .phoneNum("01011112222")
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(NotFoundMember);
        when(memberRepository.findByMemberId(NotFoundMember.getMemberId()))
                .thenReturn(Optional.empty());
        //when&then
        CustomException exception = assertThrows(CustomException.class,
                () -> memberService.getProfile(userDetails));
        assertEquals(ErrorCode.NOT_FOUND_MEMBER, exception.getErrorCode());
    }

}