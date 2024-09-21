package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.entity.NotificationMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsNotification implements INotification {

    @Override
    public void sendNotification(NotificationMode notificationMode, NotificationDto notificationDto) {
        log.info("Sending SMS notification to: {}", notificationDto.mobileNo());
    }
}
