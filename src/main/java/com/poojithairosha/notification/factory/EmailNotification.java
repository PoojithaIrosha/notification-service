package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.dto.NotificationDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotification implements INotification {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public CompletableFuture<Void> sendNotification(NotificationDto notificationDto) {
        log.info("Sending Email notification: {}", notificationDto);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(notificationDto.getTo().toArray(new String[0]));
            helper.setSubject(notificationDto.getSubject());

            if (notificationDto.getCc() != null && !notificationDto.getCc().isEmpty()) {
                helper.setCc(notificationDto.getCc().toArray(new String[0]));
            }

            Context context = new Context();
            for (Map.Entry<String, Object> entry : notificationDto.getAdditionalParams().entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            String htmlContent = templateEngine.process(notificationDto.getTemplateUrl(), context);

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Email notification sent successfully");

            return CompletableFuture.completedFuture(null);

        } catch (Exception ex) {
            log.error("Failed to send email notification", ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
