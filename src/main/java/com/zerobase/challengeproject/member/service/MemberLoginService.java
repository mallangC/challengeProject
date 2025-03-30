package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.jwt.JwtUtil;
import com.zerobase.challengeproject.member.domain.dto.MemberLoginResponse;
import com.zerobase.challengeproject.member.domain.form.MemberLoginForm;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberLoginResponse login(MemberLoginForm form) {
        Member member = memberRepository.findByMemberId(form.getMemberId())
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        if(!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        String token = jwtUtil.createToken(form.getMemberId());
        return new MemberLoginResponse(token, member.getMemberId());
    }
}
