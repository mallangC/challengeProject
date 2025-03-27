package com.zerobase.challengeproject.member.service;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.components.MailComponents;
import com.zerobase.challengeproject.member.domain.dto.MemberEmailAuthDto;
import com.zerobase.challengeproject.member.domain.dto.MemberSignupDto;
import com.zerobase.challengeproject.member.domain.form.MemberSignupForm;
import com.zerobase.challengeproject.member.entity.Member;
import com.zerobase.challengeproject.member.repository.MemberRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailComponents mailComponents;

    public MemberSignupDto signup(@Valid MemberSignupForm memberSignupForm) {
        if(!Objects.equals(memberSignupForm.getPassword(), memberSignupForm.getConfirmPassword())) {
            throw new CustomException(ErrorCode.CONFIRM_PASSWORD_MISMATCH);
        }
        String password = passwordEncoder.encode(memberSignupForm.getPassword());
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
                + "<div><a href='http://localhost:8080/api/member/email-auth?id=" + emailAuthKey +"'>클릭하여 이메일 인증 완료 하기</a></div>";
        mailComponents.send(email, subject, text);
    }

    @Transactional
    public MemberEmailAuthDto verifyEmail(String emailAuthKey) {
        Optional<Member> memberOptional = memberRepository.findByEmailAuthKey(emailAuthKey);

        if(memberOptional.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_MEMBER);
        }

        Member member = memberOptional.get();
        if(member.isEmailAuthYn()){
            throw new CustomException(ErrorCode.ALREADY_VERIFY_EMAIL);
        }

        member.completeEmailAuth();
        return  new MemberEmailAuthDto(member);
    }
}
