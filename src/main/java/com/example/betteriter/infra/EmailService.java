package com.example.betteriter.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailSender;

    // 회원가입인 경우, 인증을 위한 이메일 전송
    public void sendEmailForJoin(String toEmail,
                                 String code) {
        MimeMessage emailForm = createEmailForJoin(toEmail, code);
        this.javaMailSender.send(emailForm);
    }

    // 비밀번호 재설정인 경우, 인증을 위한 이메일 전송
    public void sendEmailForResetPassword(String toEmail,
                                          String code) {
        MimeMessage emailForm = createEmailForResetPassword(toEmail, code);
        this.javaMailSender.send(emailForm);
    }


    private MimeMessage createEmailForJoin(String toEmail, String code) {
        MimeMessage message = this.javaMailSender.createMimeMessage();

        String title = "[ITer] 인증번호를 안내해드립니다.";
        String text = "<h2>[ITer] 인증번호를 안내해드립니다.</h2><br>\n" +
                "    <p> 저희 ITer 서비스 <u><strong>신규 등록을 위해</strong></u> 이메일 인증이 필요합니다.</p>" +
                "    <p> 아래 인증 번호를 확인하신 후 이메일 인증절차를 완료해주세요. </p><br>" +
                "    <h3>인증번호 : <strong>" + code + "</strong></h3><br>" +
                "    <p> 인증번호는 3분 이내에 입력하셔야 합니다. </p>" +
                "    <p>항상 ITer에 관심을 가져주시고 이용해 주셔서 감사합니다.</p>\n" +
                "    <p><em>Better team.</em></p>";

        try {
            message.setFrom(mailSender);
            message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            message.setSubject(title);
            message.setText(text, "UTF-8", "html");
        } catch (MessagingException exception) {
            log.debug("EmailService.createEmailForJoin() exception occurs");
            throw new RuntimeException("EmailService Exception Occurs");
        }
        return message;
    }


    private MimeMessage createEmailForResetPassword(String toEmail, String code) {
        MimeMessage message = this.javaMailSender.createMimeMessage();

        String title = "[ITer] 인증번호를 안내해드립니다.";
        String text = "<h2>[ITer] 인증번호를 안내해드립니다.</h2><br>\n" +
                "    <p> 저희 ITer 서비스 <u><strong>비밀번호 재설정을 위해</strong></u> 이메일 인증이 필요합니다.</p>" +
                "    <p> 아래 인증 번호를 확인하신 후 이메일 인증절차를 완료해주세요.</p><br>" +
                "    <h3>인증 번호 : <strong>" + code + "</strong></h3><br>" +
                "    <p> 인증번호는 3분 이내에 입력하셔야 합니다.</p>" +
                "    <p>항상 ITer에 관심을 가져주시고 이용해 주셔서 감사합니다.</p>\n" +
                "    <p><em>Better team.</em></p>";

        try {
            message.setFrom(mailSender);
            message.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            message.setSubject(title);
            message.setText(text, "UTF-8", "html");
        } catch (MessagingException exception) {
            log.debug("EmailService.createEmailForResetPassword() exception occurs");
            throw new RuntimeException("EmailService Exception Occurs");
        }
        return message;
    }
}
