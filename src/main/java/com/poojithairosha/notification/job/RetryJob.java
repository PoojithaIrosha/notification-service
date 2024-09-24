package com.poojithairosha.notification.job;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.entity.NotificationLog;
import com.poojithairosha.notification.entity.NotificationMode;
import com.poojithairosha.notification.repository.NotificationLogRepository;
import com.poojithairosha.notification.repository.NotificationModeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.poojithairosha.notification.entity.NotificationStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryJob {

    private final NotificationLogRepository notificationLogRepository;
    private final NotificationModeRepository notificationModeRepository;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    // @Scheduled(cron = "${system.cron.notification.retry:0 */2 * * * ?}")
    public void retryFailedNotifications() {
        log.info("Looking for failed notifications");
        List<NotificationLog> failedNotifications = notificationLogRepository.findAllByStatus(PENDING).orElseThrow(() -> new RuntimeException("Failed notifications not found"));
        log.info("Found {} failed notifications", failedNotifications.size());
        for (NotificationLog notificationLog : failedNotifications) {
            if (notificationLog.getAttempts() < notificationLog.getMaxRetryAttempts()) {
                log.info("Retrying notification for id {}, NotificationType {}, Attempts {}", notificationLog.getNotification().getId(), notificationLog.getNotificationType(), notificationLog.getAttempts() + 1);

                var notificationDto = NotificationDto.builder()
                        .to(List.of(notificationLog.getToRecipients().split(",")))
                        .cc(List.of(notificationLog.getCcRecipients().split(",")))
                        .mobileNo(List.of(notificationLog.getMobileRecipients().split(",")))
                        .additionalParams(notificationLog.getAdditionalParams().entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                        .build();

                NotificationMode systemNotificationType = notificationModeRepository.findByNotification_IdAndNotificationType(notificationLog.getNotification().getId(), notificationLog.getNotificationType()).orElseThrow(() -> new RuntimeException("Notification mode not found"));
                notificationDto.setSubject(systemNotificationType.getSubject());
                notificationDto.setNotificationName(systemNotificationType.getNotification().getName());
                notificationDto.setTemplateUrl(systemNotificationType.getTemplateUrl());

                int attemptCount = notificationLog.getAttempts() + 1;
                try {
                    kafkaTemplate.send(systemNotificationType.getNotificationType().name().toLowerCase() + "-notification", notificationDto);
                    notificationLog.setStatus(SUCCESS);
                    log.info("Notification sent successfully with details {}", notificationDto);
                } catch (Exception e) {
                    notificationLog.setStatus(attemptCount < notificationLog.getMaxRetryAttempts() ? PENDING : FAILED);
                    log.error("Error sending failed notification {} attempt {} for id {}: {}", notificationLog.getNotificationType(), notificationLog.getAttempts() + 1, notificationLog.getNotification().getId(), e.getMessage(), e);
                } finally {
                    notificationLog.setAttempts(notificationLog.getAttempts() + 1);
                    notificationLogRepository.save(notificationLog);
                    log.info("Notification log updated");
                }
            } else {
                log.error("Max attempts reached for SystemNotificationId {}, NotificationType {}", notificationLog.getNotification().getId(), notificationLog.getNotificationType());
            }
        }

        log.info("END: Retrying failed notifications");
    }

}
