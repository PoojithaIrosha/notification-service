package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.entity.NotificationType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationFactory {

    private final EmailNotification emailNotification;
    private final SmsNotification smsNotification;
    private final WhatsappNotification whatsappNotification;

    private Map<NotificationType, INotification> notificationMap;

    @PostConstruct
    public void init() {
        log.info("START: Initializing Notification Factory");
        notificationMap = new EnumMap<>(NotificationType.class);
        notificationMap.put(NotificationType.EMAIL, emailNotification);
        notificationMap.put(NotificationType.SMS, smsNotification);
        notificationMap.put(NotificationType.WHATSAPP, whatsappNotification);
        log.info("END: Initializing Notification Factory");
    }

    public INotification getNotification(NotificationType notificationType) {
        log.info("START: Getting notification handler for type: {}", notificationType);
        INotification notification = notificationMap.get(notificationType);
        if (notification != null) {
            log.info("Found notification handler for type: {}", notificationType);
            return notification;
        } else {
            log.warn("No specific notification handler found for type: {}. Using default notification handler.", notificationType);
            return null;
        }
    }

}
