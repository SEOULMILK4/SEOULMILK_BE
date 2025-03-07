package com.seoulmilk.seoulmilkServer.global.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
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

    public void sendAgencyInvitation(String agencyEmail, String agencyName) {
           try {
               String subject = "[서울우유협동조합] 대리점 초대 메일";

               Map<String, Object> variables = new HashMap<>();
               variables.put("agencyName", agencyName);
               variables.put("agencyEmail", agencyEmail);

               Context context = new Context();
               context.setVariables(variables);

               String htmlContent = templateEngine.process("agency-invitation-template", context);

               MimeMessage message = mailSender.createMimeMessage();
               MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

               helper.setTo(agencyEmail);
               helper.setSubject(subject);
               helper.setText(htmlContent, true);

               mailSender.send(message);
           } catch (MessagingException e) {
               throw new RuntimeException("이메일 전송 중 오류 발생", e);
           }
       }
}


