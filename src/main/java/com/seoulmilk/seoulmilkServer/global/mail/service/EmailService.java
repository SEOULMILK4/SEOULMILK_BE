package com.seoulmilk.seoulmilkServer.global.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendOtp(String email, String otp) {
        try {
            String subject = "[서울우유협동조합] 인증 코드";

            Context context = new Context();
            context.setVariable("otpCode", otp);

            String htmlContent = templateEngine.process("email-template", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 중 오류 발생", e);
        }
    }

    public void sendAgencyInvitation(String email, String agencyName) {
           try {
               String subject = "[서울우유협동조합] 대리점 승인 완료 안내";

               Context context = new Context();
               context.setVariable("agencyName",agencyName);

               String htmlContent = templateEngine.process("agency-invitation-template", context);

               MimeMessage message = mailSender.createMimeMessage();
               MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

               helper.setTo(email);
               helper.setSubject(subject);
               helper.setText(htmlContent, true);

               mailSender.send(message);
           } catch (MessagingException e) {
               throw new RuntimeException("이메일 전송 중 오류 발생", e);
           }
       }
}


