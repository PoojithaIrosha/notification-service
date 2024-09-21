package com.poojithairosha.notification.repository;

import com.poojithairosha.notification.entity.NotificationLog;
import com.poojithairosha.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    Optional<List<NotificationLog>> findAllByStatus(NotificationStatus status);
}
