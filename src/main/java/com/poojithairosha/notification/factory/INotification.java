package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.dto.NotificationDto;
import com.poojithairosha.notification.entity.NotificationMode;

public interface INotification {

    void sendNotification(NotificationMode notificationMode, NotificationDto notificationDto);

}
