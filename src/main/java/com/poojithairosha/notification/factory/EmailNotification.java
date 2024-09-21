package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.entity.NotificationMode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailNotification implements INotification {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendNotification(NotificationMode notificationMode, NotificationDto notificationDto) {
        log.info("Sending Email notification: {}", notificationDto);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(notificationDto.to().toArray(new String[0]));
            helper.setSubject(notificationMode.getSubject());

            if (notificationDto.cc() != null && !notificationDto.cc().isEmpty()) {
                helper.setCc(notificationDto.cc().toArray(new String[0]));
            }

            Context context = new Context();
            for (Map.Entry<String, Object> entry : notificationDto.additionalParams().entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            String htmlContent = templateEngine.process(notificationMode.getTemplateUrl(), context);

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

            log.info("Email notification sent successfully");
        } catch (Exception ex) {
            log.error("Failed to send email notification", ex);
            throw new RuntimeException("Failed to send email", ex);
        }
    }
}
