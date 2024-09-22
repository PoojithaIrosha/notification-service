package com.poojithairosha.notification.service;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.entity.*;
import com.poojithairosha.notification.exception.NotificationNotFoundException;
import com.poojithairosha.notification.factory.INotification;
import com.poojithairosha.notification.factory.NotificationFactory;
import com.poojithairosha.notification.repository.NotificationLogRepository;
import com.poojithairosha.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static com.poojithairosha.notification.entity.NotificationStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationFactory notificationFactory;
    private final NotificationLogRepository notificationLogRepository;

    @Async
    public void sendNotification(NotificationDto notificationDto, String notificationName) {
        log.info("Send {} notification to {}", notificationName, notificationDto.to());

        var notification = notificationRepository.findByName(notificationName).orElseThrow(() -> new NotificationNotFoundException(String.format("Notification not found with name %s", notificationName)));

        for (NotificationMode mode : notification.getNotificationModes()) {
            log.info("Sending {} notification using {}", notificationName, mode.getNotificationType());

            INotification iNotification = notificationFactory.getNotification(mode.getNotificationType());
            if (iNotification != null) {
                int maxRetryAttempt = mode.getMaxRetryAttempts();
                try {
                    iNotification.sendNotification(mode, notificationDto);
                    logNotification(notification, mode.getNotificationType(), notificationDto, SUCCESS, maxRetryAttempt);
                } catch (Exception e) {
                    NotificationStatus status = 1 < maxRetryAttempt ? PENDING : FAILED;
                    log.error("Failed to send {} notification using {}", notificationName, mode.getNotificationType(), e);
                    logNotification(notification, mode.getNotificationType(), notificationDto, status, maxRetryAttempt);
                }
            } else {
                log.warn("No notification has been implemented for {}", mode.getNotificationType());
            }
        }
    }

    private void logNotification(Notification notification, NotificationType type, NotificationDto dto, NotificationStatus status, int maxRetryAttempt) {

        var additionalParams = dto.additionalParams().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));

        var notificationLog = NotificationLog.builder()
                .notification(notification)
                .notificationType(type)
                .toRecipients((dto.to() != null) ? String.join(", ", dto.to()) : null)
                .ccRecipients((dto.cc() != null) ? String.join(", ", dto.cc()) : null)
                .mobileRecipients((dto.mobileNo() != null) ? String.join(", ", dto.mobileNo()) : null)
                .status(status)
                .timestamp(LocalDateTime.now())
                .maxRetryAttempts(maxRetryAttempt)
                .additionalParams(additionalParams)
                .build();
        notificationLog.setAttempts(1);

        var saved = notificationLogRepository.save(notificationLog);

        log.info("Notification log saved {}", saved);
    }

}
