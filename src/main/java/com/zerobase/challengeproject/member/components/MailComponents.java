package com.zerobase.challengeproject.member.components;

import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponents {

    private final JavaMailSender mailSender;

    public boolean send(String to, String subject, String body) {

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body, true);
        };
        try {
            mailSender.send(msg);
            return true;
        }catch (Exception e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }

    }
}
