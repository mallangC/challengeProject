package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.domain.dto.MemberProfileDto;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 유저가 회원 정보를 조회할 때 사용되는 서비스 메서드
     * @param userDetails 로그인한 유저의 정보
     * @return 유저 정보(로그인아이디, 이름, 닉네임, 전화번호, 이메일주소)
     */
    public MemberProfileDto getProfile(UserDetailsImpl userDetails) {
        Member member = memberRepository.findByMemberId(userDetails.getMember().getMemberId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        return new MemberProfileDto(member);
    }
}
