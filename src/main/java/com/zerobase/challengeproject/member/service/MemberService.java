package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.UserDetailsImpl;
import com.zerobase.challengeproject.member.domain.dto.MemberProfileDto;
import com.zerobase.challengeproject.member.domain.form.ChangePasswordForm;
import com.zerobase.challengeproject.member.domain.form.MemberProfileFrom;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 유저가 회원 정보를 조회할 때 사용되는 서비스 메서드
     * @param userDetails 로그인한 유저의 정보
     * @return 유저 정보(로그인아이디, 이름, 닉네임, 전화번호, 이메일주소)
     */
    public MemberProfileDto getProfile(UserDetailsImpl userDetails) {
        Member member = findMemberByMemberId(userDetails.getUsername());
        return new MemberProfileDto(member);
    }

    @Transactional
    public MemberProfileDto updateProfile(UserDetailsImpl userDetails, MemberProfileFrom form) {
        Member member = findMemberByMemberId(userDetails.getUsername());
        member.updateProfile(form.getPhoneNum(),form.getNickname());
        return new MemberProfileDto(member);
    }

    @Transactional
    public String changePassword(UserDetailsImpl userDetails, ChangePasswordForm form) {
        Member member = findMemberByMemberId(userDetails.getUsername());
        if(!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        if(!form.getNewPassword().equals(form.getNewPasswordVerify())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        if(passwordEncoder.matches(form.getNewPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.MATCHES_PREVIOUS_PASSWORD);
        }
        String password = passwordEncoder.encode(form.getNewPassword());
        member.changePassword(password);
        return member.getMemberId();
    }


    private Member findMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
