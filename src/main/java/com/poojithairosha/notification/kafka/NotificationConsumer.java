package com.poojithairosha.notification.kafka;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.factory.NotificationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.poojithairosha.notification.entity.NotificationType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationFactory notificationFactory;

    @KafkaListener(topics = "email-notification", groupId = "notification-group")
    public void consumeEmailNotification(NotificationDto notificationDto) {
        log.info("Consumed email notification: {}", notificationDto);
        notificationFactory.getNotification(EMAIL).sendNotification(notificationDto);
    }

    @KafkaListener(topics = "sms-notification", groupId = "notification-group")
    public void consumeSmsNotification(NotificationDto notificationDto) {
        log.info("Consumed sms notification: {}", notificationDto);
        notificationFactory.getNotification(SMS).sendNotification(notificationDto);
    }

    @KafkaListener(topics = "whatsapp-notification", groupId = "notification-group")
    public void consumeWhatsappNotification(NotificationDto notificationDto) {
        log.info("Consumed whatsapp notification: {}", notificationDto);
        notificationFactory.getNotification(WHATSAPP).sendNotification(notificationDto);
    }

}
