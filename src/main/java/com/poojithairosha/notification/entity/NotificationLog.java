package com.poojithairosha.notification.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@ToString(exclude = "notification")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toRecipients;
    private String ccRecipients;
    private String mobileRecipients;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String subject;
    private LocalDateTime timestamp;
    private int attempts;
    private int maxRetryAttempts;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    @JsonBackReference
    private Notification notification;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "param_key")
    @Column(name = "param_value")
    @CollectionTable(name = "notification_log_params", joinColumns = @JoinColumn(name = "notification_log_id"))
    private Map<String, String> additionalParams;

    public void setAttempts(int attempts) {
        this.attempts += attempts;
    }
}
