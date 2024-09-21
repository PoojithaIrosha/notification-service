package com.poojithairosha.notification.repository;

import com.poojithairosha.notification.entity.NotificationMode;
import com.poojithairosha.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationModeRepository extends JpaRepository<NotificationMode, Long> {

    Optional<NotificationMode> findByNotification_IdAndNotificationType(Long id, NotificationType type);

}
