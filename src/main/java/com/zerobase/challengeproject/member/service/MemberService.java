package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.MailComponents;
import com.zerobase.challengeproject.member.domain.dto.MemberSignupDto;
import com.zerobase.challengeproject.member.domain.form.MemberSignupForm;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
    private final MailComponents mailComponents;

    public MemberSignupDto signup(@Valid MemberSignupForm memberSignupForm) {
//        String password = passwordEncoder.encode(memberSignupForm.getPassword());
        String password = memberSignupForm.getPassword();
        String emailAuthKey = UUID.randomUUID().toString();
        String email = memberSignupForm.getEmail();
        if(memberRepository.existsByEmail(email)){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        Member member = Member.form(memberSignupForm, password, emailAuthKey);
        memberRepository.save(member);

        sendEmail(memberSignupForm.getEmail(), emailAuthKey);
        return new MemberSignupDto(member);
    }

    private void sendEmail(@NotBlank @Email String email, String emailAuthKey) {
        String subject = "ZerobaseChallenge에 가입해 주셔서 감사합니다";
        String text = "<p>ZerobaseChallenge 사이트 가입을 축하드립니다.</p>" +
                "<p>아래 링크를 클릭하셔서 가입을 완료하세요.</p>"
                + "<div><a href='http://localhost:8080/member/email-auth?id=" + emailAuthKey +"'>가입 완료</a></div>";
        mailComponents.send(email, subject, text);
    }
}
