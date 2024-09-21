package com.poojithairosha.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class NotificationMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String subject;

    private String templateUrl;
    private int maxRetryAttempts = 3;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

}
