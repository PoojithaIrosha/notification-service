package com.poojithairosha.notification.service;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.entity.*;
import com.poojithairosha.notification.exception.NotificationNotFoundException;
import com.poojithairosha.notification.repository.NotificationLogRepository;
import com.poojithairosha.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static com.poojithairosha.notification.entity.NotificationStatus.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    public void sendNotification(NotificationDto notificationDto) {
        log.info("Send {} notification to {}", notificationDto.getNotificationName(), notificationDto.getTo());

        var notification = notificationRepository.findByName(notificationDto.getNotificationName()).orElseThrow(() -> new NotificationNotFoundException(String.format("Notification not found with name %s", notificationDto.getNotificationName())));

        for (NotificationMode mode : notification.getNotificationModes()) {
            log.info("Sending {} notification using {}", notificationDto.getNotificationName(), mode.getNotificationType());

            notificationDto.setSubject(mode.getSubject());
            notificationDto.setTemplateUrl(mode.getTemplateUrl());

            kafkaTemplate.send(mode.getNotificationType().name().toLowerCase() + "-notification", notificationDto);
            logNotification(notification, mode.getNotificationType(), notificationDto, SUCCESS, mode.getMaxRetryAttempts());
        }
    }

    private void logNotification(Notification notification, NotificationType type, NotificationDto dto, NotificationStatus status, int maxRetryAttempt) {

        var additionalParams = dto.getAdditionalParams().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toString()));

        var notificationLog = NotificationLog.builder()
                .notification(notification)
                .notificationType(type)
                .toRecipients((dto.getTo() != null) ? String.join(", ", dto.getTo()) : null)
                .ccRecipients((dto.getCc() != null) ? String.join(", ", dto.getCc()) : null)
                .mobileRecipients((dto.getMobileNo() != null) ? String.join(", ", dto.getMobileNo()) : null)
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
