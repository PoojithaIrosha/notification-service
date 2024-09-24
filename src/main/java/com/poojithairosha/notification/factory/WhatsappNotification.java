package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.dto.NotificationDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsappNotification implements INotification {

    private final SpringTemplateEngine templateEngine;
    @Value("${twilio.account-sid}")
    private String accountSid;
    @Value("${twilio.auth-token}")
    private String authToken;
    @Value("${twilio.from-wanumber}")
    private String fromNumber;

    @PostConstruct
    private void init() {
        log.info("Initializing Twilio with accountSid: {}", accountSid);
        Twilio.init(accountSid, authToken);
    }

    @Async
    @Override
    public CompletableFuture<Void> sendNotification(NotificationDto notificationDto) {
        log.info("Sending Whatsapp notification: {}", notificationDto);
        try {
            Context context = new Context();
            for (Map.Entry<String, Object> entry : notificationDto.getAdditionalParams().entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            String smsContent = templateEngine.process(notificationDto.getTemplateUrl(), context);
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + notificationDto.getMobileNo().get(0)),
                    new PhoneNumber(fromNumber),
                    smsContent).create();
            log.info("Whatsapp notification sent successfully: {}", message.getSid());

            return CompletableFuture.completedFuture(null);
        } catch (Exception ex) {
            log.warn("Failed to send Whatsapp notification", ex);
            throw new RuntimeException("Failed to send Whatsapp", ex);
        }
    }

}
