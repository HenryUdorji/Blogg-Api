package com.codemountain.blogg.service;

import com.codemountain.blogg.exception.BloggException;
import com.codemountain.blogg.payload.request.MailRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilderService mailContentBuilderService;

    @Async
    public void sendMail(MailRequest mailRequest) throws BloggException {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("blogg.mail.com");
            mimeMessageHelper.setTo(mailRequest.getRecipient());
            mimeMessageHelper.setSubject(mailRequest.getSubject());
            mimeMessageHelper.setText(mailContentBuilderService.build(mailRequest.getBody()));
        };

        try {
            mailSender.send(mimeMessagePreparator);
            log.info("Activation email sent!!");
        }catch (MailException e) {
            throw new BloggException(HttpStatus.BAD_REQUEST, "Error occurred while sending mail to: " + mailRequest.getRecipient());
        }
    }
}
